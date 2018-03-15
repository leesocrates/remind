package com.lee.socrates.webrtclib.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.lee.library.fragment.BaseFragment
import com.lee.library.util.notNullOrEmpty
import com.lee.socrates.webrtclib.R
import kotlinx.android.synthetic.main.fragment_webrtc.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.webrtc.*
import java.lang.Exception
import java.net.URI
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset

/**
 * Created by lee on 2018/1/17.
 */
class DataChannelFragment : BaseFragment() {

    private lateinit var webSocket: WebSocketClient
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var videoRenderer: VideoRenderer
    private var videoCapturerAndroid: VideoCapturer? = null
    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var localAudioTrack: AudioTrack
    private var localPeer: PeerConnection? = null
    private var remotePeer: PeerConnection? = null
    private var callUserName: String? = null
    private var dataChannel: DataChannel? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_webrtc
    }

    override fun initView() {
        checkPermission()
        openWebSocket()
        btnLogin.setOnClickListener { login() }
        btnCall.setOnClickListener { call() }
        btnHangUp.setOnClickListener { hangup() }
        btnSendMessage.setOnClickListener { sendMessage() }
    }

    private fun login() {
        var name = loginNameEditText.text.toString()
        name.let {
            send(SignalMessage(MESSAGE_TYPE_LOGIN, name))
        }
    }

    private fun start() {
        //Initialize PeerConnectionFactory globals.
        var options: PeerConnectionFactory.InitializationOptions = PeerConnectionFactory.InitializationOptions.builder(activity)
                .setEnableInternalTracer(false)
                .setEnableVideoHwAcceleration(true)
                .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
        peerConnectionFactory = PeerConnectionFactory(PeerConnectionFactory.Options())

        videoCapturerAndroid = createVideoCapturer()
        //Create a VideoSource instance
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturerAndroid)
        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource)

        //create an AudioSource instance
        var audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource)

        videoCapturerAndroid?.startCapture(80, 80, 30)

        //create an EglBase instance
        val rootEglBase = EglBase.create()
        //init the SurfaceViewRenderer using the eglContext
        localMediaView.init(rootEglBase.eglBaseContext, null)
        //a small method to provide a mirror effect to the SurfaceViewRenderer
        localMediaView.setMirror(true)
        localMediaView.visibility = View.VISIBLE
        //Add the renderer to the video track
        videoRenderer = VideoRenderer(localMediaView)
        localVideoTrack.addRenderer(videoRenderer)

        //we already have video and audio tracks. Now create peerconnections
        val iceServers = ArrayList<PeerConnection.IceServer>()


        //create sdpConstraints
        var sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))

        //creating localPeer
        localPeer = peerConnectionFactory.createPeerConnection(iceServers, sdpConstraints, localObserver)

        //creating local mediastream
        val stream = peerConnectionFactory.createLocalMediaStream("102")
        stream.addTrack(localAudioTrack)
        stream.addTrack(localVideoTrack)
        localPeer?.addStream(stream)


        var init = DataChannel.Init()
        init.negotiated = true
        init.id = 1000
        dataChannel = localPeer?.createDataChannel("dataChannel", init)
        dataChannel?.registerObserver(dataChannelObserver)
    }

    private fun call() {
        callUserName = callNameEditText.text.toString()
        if (callUserName.isNullOrEmpty()) {
            return
        }

        //create sdpConstraints
        var sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))

        //creating Offer
        localPeer?.createOffer(object : SdpObserver {
            override fun onCreateSuccess(p0: SessionDescription?) {
                localPeer?.setLocalDescription(sdpObserver, p0)
                send(SignalMessage(MESSAGE_TYPE_OFFER, offer = p0))
            }

            override fun onSetFailure(p0: String?) {
            }

            override fun onSetSuccess() {
            }

            override fun onCreateFailure(p0: String?) {
                Log.e(TAG, "on create offer failed")
            }
        }, sdpConstraints)
    }

    private fun hangup() {
        localPeer?.close()
        remotePeer?.close()
        localPeer = null
        remotePeer = null
    }

    private fun sendMessage(){
        val message: String = messageEditText.text.toString()
        message.notNullOrEmpty {
            dataChannel?.send(DataChannel.Buffer(ByteBuffer.wrap(message.toByteArray()), false))
        }
    }

    private fun createVideoCapturer(): VideoCapturer? {
        var videoCapturer = createCameraCapturer(Camera1Enumerator(false))
        return videoCapturer
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(Array(1) { Manifest.permission.CAMERA }, 50)
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(Array(1) { Manifest.permission.RECORD_AUDIO }, 60)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 50) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "camera permission granted")
            } else {
                Log.e(TAG, "camera permission not granted")
            }
        }
        if (requestCode == 60) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "audio permission granted")
            } else {
                Log.e(TAG, "audio permission not granted")
            }
        }
    }

    private fun createCameraCapturer(enumerator: CameraEnumerator): VideoCapturer? {
        val deviceNames = enumerator.deviceNames

        // Trying to find a front facing camera!
        for (deviceName in deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                val videoCapturer = enumerator.createCapturer(deviceName, null)

                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        // We were not able to find a front cam. Look for other cameras
        for (deviceName in deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                val videoCapturer = enumerator.createCapturer(deviceName, null)
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        return null
    }

    private fun gotRemoteStream(stream: MediaStream?) {
        //we have remote video stream. add to the renderer.
        val videoTrack = stream?.videoTracks?.first()
        val audioTrack = stream?.audioTracks?.first()
        activity.runOnUiThread {
            try {
                //create an EglBase instance
                val rootEglBase = EglBase.create()
                //init the SurfaceViewRenderer using the eglContext
                remoteMediaView.init(rootEglBase.eglBaseContext, null)
                //a small method to provide a mirror effect to the SurfaceViewRenderer
                remoteMediaView.setMirror(true)
                remoteMediaView.visibility = View.VISIBLE
                var remoteRenderer = VideoRenderer(remoteMediaView)
                videoTrack?.addRenderer(remoteRenderer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun onIceCandidateReceived(peer: PeerConnection?, iceCandidate: IceCandidate?) {
        //we have received ice candidate. We can set it to the other peer.
        send(SignalMessage(MESSAGE_TYPE_CANDIDATE, candidate = iceCandidate))
    }

    private var localObserver = object : PeerConnection.Observer {
        override fun onIceCandidate(p0: IceCandidate?) {
            Log.i(TAG, "onIceCandidate invoke")
            onIceCandidateReceived(localPeer, p0)
        }

        override fun onDataChannel(p0: DataChannel?) {
        }

        override fun onIceConnectionReceivingChange(p0: Boolean) {
        }

        override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
        }

        override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
        }

        override fun onAddStream(p0: MediaStream?) {
            Log.i(TAG, "onAddStream invoke")
            gotRemoteStream(p0)
        }

        override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
        }

        override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
        }

        override fun onRemoveStream(p0: MediaStream?) {
        }

        override fun onRenegotiationNeeded() {
        }

        override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
        }
    }

    private var sdpObserver = object : SdpObserver {
        override fun onSetFailure(p0: String?) {
            Log.e(TAG, "on set sdp failed")
        }

        override fun onSetSuccess() {
            Log.i(TAG, "on set sdp success")
        }

        override fun onCreateSuccess(p0: SessionDescription?) {
        }

        override fun onCreateFailure(p0: String?) {
        }
    }

    private fun openWebSocket() {
        webSocket = object : WebSocketClient(URI(WS_URI_ADDRESS)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.i(TAG, "webSocket opened")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
            }

            override fun onMessage(message: String?) {
                Log.i(TAG, "webSocket onMessage $message")
                activity?.runOnUiThread { handleMessage(message) }
            }

            override fun onError(ex: Exception?) {
                Log.e(TAG, "connect to webSocket error")
            }

        }
        webSocket.connect()
    }

    private fun handleMessage(message: String?) {
        var signalMessage = Gson().fromJson(message, SignalMessage::class.java)
        when (signalMessage?.type) {
            MESSAGE_TYPE_LOGIN -> handleLoginMessage(signalMessage)
            MESSAGE_TYPE_OFFER -> handleOfferMessage(signalMessage)
            MESSAGE_TYPE_ANSWER -> handleAnswerMessage(signalMessage)
            MESSAGE_TYPE_CANDIDATE -> handleIceCandidateMessage(signalMessage)
        }
    }

    private fun handleLoginMessage(signalMessage: SignalMessage?) {
        if (signalMessage?.success != null && signalMessage.success) {
            Log.i(TAG, "login success")
            start()
        } else {
            Log.i(TAG, "login fail")
        }
    }

    private fun handleOfferMessage(signalMessage: SignalMessage?) {
        callUserName = signalMessage?.name
        localPeer?.setRemoteDescription(sdpObserver, signalMessage?.offer)
        localPeer?.createAnswer(object : SdpObserver {
            override fun onSetFailure(p0: String?) {
            }

            override fun onSetSuccess() {
            }

            override fun onCreateFailure(p0: String?) {
                Log.e(TAG, "create answer failed")
            }

            override fun onCreateSuccess(p0: SessionDescription?) {
                localPeer?.setLocalDescription(sdpObserver, p0)
                send(SignalMessage(MESSAGE_TYPE_ANSWER, answer = p0))
            }
        }, MediaConstraints())
    }

    private fun handleAnswerMessage(signalMessage: SignalMessage?) {
        localPeer?.setRemoteDescription(sdpObserver, signalMessage?.answer)
    }

    private fun handleIceCandidateMessage(signalMessage: SignalMessage?) {
        localPeer?.addIceCandidate(signalMessage?.candidate)
    }

    private fun send(message: SignalMessage) {
        callUserName?.let {
            message.name = callUserName
        }
        webSocket.send(Gson().toJson(message))
    }

    companion object {
        //        private val WS_URI_ADDRESS = "ws://47.52.98.195:9090"
        private val WS_URI_ADDRESS = "ws://172.30.66.77:9090"
        private val MESSAGE_TYPE_LOGIN = "login"
        private val MESSAGE_TYPE_OFFER = "offer"
        private val MESSAGE_TYPE_ANSWER = "answer"
        private val MESSAGE_TYPE_CANDIDATE = "candidate"
    }

    data class SignalMessage(val type: String?, var name: String? = null, var offer: SessionDescription? = null,
                             var candidate: IceCandidate? = null, var answer: SessionDescription? = null, val success: Boolean? = null)

    var dataChannelObserver = object : DataChannel.Observer {
        override fun onMessage(p0: DataChannel.Buffer?) {
            Log.i(TAG, "receive message ${byteBufferToString(p0?.data)}")
        }

        override fun onBufferedAmountChange(p0: Long) {

        }

        override fun onStateChange() {
            var state = dataChannel?.state()
            Log.i(TAG, "current state is $state")
        }

    }

    fun byteBufferToString(buffer: ByteBuffer?): String? {
        var charBuffer: CharBuffer? = null
        return try {
            val charset = Charset.forName("UTF-8")
            val decoder = charset.newDecoder()
            charBuffer = decoder.decode(buffer)
            buffer?.flip()
            charBuffer!!.toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

    }
}