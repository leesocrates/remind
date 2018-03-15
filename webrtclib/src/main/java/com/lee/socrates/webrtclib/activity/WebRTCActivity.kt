package com.lee.socrates.webrtclib.activity

import android.view.View
import com.lee.library.activity.BaseActivity
import com.lee.socrates.webrtclib.R
import com.lee.socrates.webrtclib.fragment.DataChannelFragment
import com.lee.socrates.webrtclib.fragment.WebRTCFragment
import kotlinx.android.synthetic.main.content_webrtc.*

import org.webrtc.*
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.VideoRenderer
import org.webrtc.MediaStream
import org.webrtc.MediaConstraints
import org.webrtc.SessionDescription
import org.webrtc.VideoCapturer
import org.webrtc.PeerConnectionFactory

class WebRTCActivity : BaseActivity() {

    private lateinit var localVideoTrack : VideoTrack
    private lateinit var videoRenderer: VideoRenderer
    private var videoCapturerAndroid: VideoCapturer? = null
    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var localAudioTrack: AudioTrack
    var localPeer: PeerConnection? = null
    var remotePeer: PeerConnection? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_webrtc
    }

    override fun init() {
//        btnStart.setOnClickListener { start() }
//        btnCall.setOnClickListener { call() }
//        btnHangUp.setOnClickListener { hangup() }

        openFragment<DataChannelFragment>(R.id.fragment_container)
    }

    fun start() {
        //Initialize PeerConnectionFactory globals.
        var options : PeerConnectionFactory.InitializationOptions =  PeerConnectionFactory.InitializationOptions.builder(applicationContext)
                .setEnableInternalTracer(false)
                .setEnableVideoHwAcceleration(true)
                .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
        peerConnectionFactory = PeerConnectionFactory(PeerConnectionFactory.Options())

        videoCapturerAndroid  = createVideoCapturer()
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

    }

    private fun call() {
        //we already have video and audio tracks. Now create peerconnections
        val iceServers = ArrayList<PeerConnection.IceServer>()

        //create sdpConstraints
        var sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))

        //creating localPeer
        localPeer = peerConnectionFactory.createPeerConnection(iceServers, sdpConstraints, localObserver)

        //creating remotePeer
        remotePeer = peerConnectionFactory.createPeerConnection(iceServers, sdpConstraints, remoteObserver)

        //creating local mediastream
        val stream = peerConnectionFactory.createLocalMediaStream("102")
//        stream.addTrack(localAudioTrack)
        stream.addTrack(localVideoTrack)
        localPeer?.addStream(stream)

        //creating Offer
        localPeer?.createOffer(object : SdpObserver {
            override fun onCreateSuccess(p0: SessionDescription?) {
                    localPeer?.setLocalDescription(sdpObserver, p0)
                    remotePeer?.setRemoteDescription(sdpObserver, p0)
                    remotePeer?.createAnswer(object : SdpObserver {
                        override fun onSetFailure(p0: String?) {
                        }

                        override fun onSetSuccess() {
                        }

                        override fun onCreateSuccess(p0: SessionDescription?) {
                            remotePeer?.setLocalDescription(sdpObserver, p0)
                            localPeer?.setRemoteDescription(sdpObserver, p0)
                        }

                        override fun onCreateFailure(p0: String?) {
                        }
                    }, MediaConstraints())
            }

            override fun onSetFailure(p0: String?) {
            }

            override fun onSetSuccess() {
            }

            override fun onCreateFailure(p0: String?) {
            }
        }, sdpConstraints)
    }

    private fun hangup() {
        localPeer?.close()
        remotePeer?.close()
        localPeer = null
        remotePeer = null
    }

    private fun gotRemoteStream(stream: MediaStream?) {
        //we have remote video stream. add to the renderer.
        val videoTrack = stream?.videoTracks?.first()
//        val audioTrack = stream?.audioTracks?.first
        runOnUiThread {
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

    fun onIceCandidateReceived(peer: PeerConnection?, iceCandidate: IceCandidate?) {
        //we have received ice candidate. We can set it to the other peer.
        if (peer === localPeer) {
            remotePeer?.addIceCandidate(iceCandidate)
        } else {
            localPeer?.addIceCandidate(iceCandidate)
        }
    }

    var localObserver = object : PeerConnection.Observer{
        override fun onIceCandidate(p0: IceCandidate?) {
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

    var remoteObserver = object : PeerConnection.Observer{
        override fun onIceCandidate(p0: IceCandidate?) {
            onIceCandidateReceived(remotePeer, p0)
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

    var sdpObserver = object : SdpObserver {
        override fun onSetFailure(p0: String?) {
        }

        override fun onSetSuccess() {
        }

        override fun onCreateSuccess(p0: SessionDescription?) {
        }

        override fun onCreateFailure(p0: String?) {
        }
    }




    private fun showCameraVideo() {
        var options: PeerConnectionFactory.InitializationOptions = PeerConnectionFactory.InitializationOptions.builder(applicationContext)
                .setEnableInternalTracer(false)
                .setEnableVideoHwAcceleration(true)
                .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
        var peerConnectionFactory: PeerConnectionFactory = PeerConnectionFactory(PeerConnectionFactory.Options())

        videoCapturerAndroid = createVideoCapturer()

        //Create a VideoSource instance
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturerAndroid)
        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource)

        videoCapturerAndroid?.startCapture(80, 80, 30)


        //create an EglBase instance
        val rootEglBase = EglBase.create()
        //init the SurfaceViewRenderer using the eglContext
        localMediaView.init(rootEglBase.eglBaseContext, null)
        //a small method to provide a mirror effect to the SurfaceViewRenderer
        localMediaView.setMirror(true)
        //Add the renderer to the video track
        videoRenderer = VideoRenderer(localMediaView)
        localVideoTrack.addRenderer(videoRenderer)
    }

//    override fun onStop() {
//        super.onStop()
//        videoCapturerAndroid?.stopCapture()
//        localMediaView.release()
//        videoRenderer.dispose()
//        localVideoTrack.removeRenderer(videoRenderer)
//        localVideoTrack.dispose()
//    }

    private fun createVideoCapturer(): VideoCapturer? {
        val videoCapturer: VideoCapturer?
        videoCapturer = createCameraCapturer(Camera1Enumerator(false))
        return videoCapturer
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
}
