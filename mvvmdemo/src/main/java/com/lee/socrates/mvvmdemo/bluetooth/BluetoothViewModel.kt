package com.lee.socrates.mvvmdemo.bluetooth

import android.Manifest
import android.app.Activity
import android.databinding.ObservableField
import android.view.View
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.bluetooth.BluetoothDevice
import android.util.Log
import java.util.*
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGatt
import android.content.Intent
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothGattCallback
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import kotlin.experimental.and
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat


/**
 * Created by lee on 2018/3/20.
 */
public class BluetoothViewModel {
    private val TAG = this.javaClass.simpleName
    private val bleAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    val info = ObservableField<String>()
    private lateinit var mContext: Context
    private var bleGatt: BluetoothGatt? = null
    private var bleScanCallback: BluetoothAdapter.LeScanCallback? = null
    private lateinit var mNavigator: BlueToothNavigator

    fun init(context: Context, navigator: BlueToothNavigator) {
        mContext = context
        mNavigator = navigator
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == BlueToothActivity.REQUEST_ENABLE_BT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    mNavigator.requestLocationPermission()
                    return
                }
            }
            startScan()
        }
    }

    fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == BlueToothActivity.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScan()
            }
        }
    }

    fun startSearch(view: View) {
        Log.e(TAG, "startSearch ...")
        // 检测蓝牙
        if (!check()) {
            if (bleAdapter != null && !bleAdapter.isEnabled) {
                mNavigator.requestOpenBlueTooth()
            }
            return
        }
        startScan()
    }

    private fun startScan() {
        bleScanCallback = BleScanCallback()  // 回调接口
        bleAdapter != null && bleAdapter.startLeScan(bleScanCallback)
//        var pairedDevices: Set<BluetoothDevice>? = bleAdapter?.bondedDevices
//        if(pairedDevices!=null && pairedDevices.isNotEmpty()){
//            for (device in pairedDevices) {
//                Log.e(TAG, "found device name : " + device.name + " address : " + device.address)
//            }
//        }

    }

    private fun check(): Boolean {
        return null != bleAdapter && bleAdapter.isEnabled && !bleAdapter.isDiscovering
    }

    // 实现扫描回调接口
    private inner class BleScanCallback : BluetoothAdapter.LeScanCallback {
        // 扫描到新设备时，会回调该接口。可以将新设备显示在ui中，看具体需求
        override fun onLeScan(device: BluetoothDevice, rssi: Int, scanRecord: ByteArray) {
            if (device.name!=null && device.name.isNotEmpty()) {
                Log.e(TAG, "found device name : " + device.name + " address : " + device.address)
            } else{
                Log.e(TAG, "found device name : " + device.name + " address : " + device.address)
                bleAdapter?.stopLeScan(bleScanCallback)

            }
        }
    }

    // 连接蓝牙设备，device为之前扫描得到的
    fun connect(device: BluetoothDevice) {
        if (!check()) return   // 检测蓝牙
        bleAdapter?.stopLeScan(bleScanCallback)
        if (bleGatt != null && bleGatt!!.connect()) {  // 已经连接了其他设备
            // 如果是先前连接的设备，则不做处理
            if (TextUtils.equals(device.address, bleGatt?.device?.address)) {
                return
            } else {
                // 否则断开连接
                bleGatt?.close()
            }
        }
        // 连接设备，第二个参数为是否自动连接，第三个为回调函数
        bleGatt = device.connectGatt(mContext, false, BleGattCallback())
    }

    // 实现连接回调接口[关键]
    private inner class BleGattCallback : BluetoothGattCallback() {

        // 连接状态改变(连接成功或失败)时回调该接口
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {   // 连接成功
                connectSuccess()
                gatt.discoverServices()   // 则去搜索设备的服务(Service)和服务对应Characteristic
            } else {   // 连接失败
                connectFail()
            }
        }

        // 发现设备的服务(Service)回调，需要在这里处理订阅事件。
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {  // 成功订阅
                var services = gatt.services  // 获得设备所有的服务
                var characteristics = ArrayList<BluetoothGattCharacteristic>()
                var descriptors = ArrayList<BluetoothGattDescriptor>()

                // 依次遍历每个服务获得相应的Characteristic集合
                // 之后遍历Characteristic获得相应的Descriptor集合
                for (service in services) {
                    Log.e(TAG, "-- service uuid : " + service.uuid.toString() + " --")
                    for (characteristic in service.characteristics) {
                        Log.e(TAG, "characteristic uuid : " + characteristic.uuid)
                        characteristics.add(characteristic)

                        // String READ_UUID = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
                        // 判断当前的Characteristic是否想要订阅的，这个要依据各自蓝牙模块的协议而定
                        if (characteristic.uuid.toString() == "sdfadasfadfdfafdafdfadfa") {
                            // 依据协议订阅相关信息,否则接收不到数据
                            bleGatt?.setCharacteristicNotification(characteristic, true)

                            // 大坑，适配某些手机！！！比如小米...
                            bleGatt?.readCharacteristic(characteristic)
                            for (descriptor in characteristic.descriptors) {
                                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                bleGatt?.writeDescriptor(descriptor)
                            }
                        }
                        for (descriptor in characteristic.descriptors) {
                            Log.e(TAG, "descriptor uuid : " + characteristic.uuid)
                            descriptors.add(descriptor)
                        }
                    }
                }
                discoverServiceSuccess()
            } else {
                discoverServiceFail()
            }
        }

        // 发送消息结果回调
        override fun onCharacteristicWrite(gatt: BluetoothGatt,
                                           characteristic: BluetoothGattCharacteristic,
                                           status: Int) {
            val intent: Intent
            if (BluetoothGatt.GATT_SUCCESS == status) {   // 发送成功
                sendMessageSuccess()
            } else {    // 发送失败
                sendMessageFail()
            }
        }

        // 当订阅的Characteristic接收到消息时回调
        override fun onCharacteristicChanged(gatt: BluetoothGatt,
                                             characteristic: BluetoothGattCharacteristic) {
            // 数据为 characteristic.getValue())
            Log.e(TAG, "onCharacteristicChanged: " + Arrays.toString(characteristic.value))
        }
    }

    private fun connectSuccess() {
    }

    private fun connectFail() {
    }

    private fun discoverServiceSuccess() {
    }

    private fun discoverServiceFail() {
    }

    private fun sendMessageSuccess() {
    }

    private fun sendMessageFail() {
    }

    // byte转十六进制字符串
    fun bytes2HexString(bytes: ByteArray): String {
        var ret = ""
        for (item in bytes) {
            var hex = Integer.toHexString((item and 0xFF.toByte()).toInt())
            if (hex.length == 1) {
                hex = '0' + hex
            }
            ret += hex.toUpperCase(Locale.CHINA)
        }
        return ret
    }

    // 将16进制的字符串转换为字节数组
    fun getHexBytes(message: String): ByteArray {
        val len = message.length / 2
        val chars = message.toCharArray()
        val hexStr = arrayOfNulls<String>(len)
        val bytes = ByteArray(len)
        var i = 0
        var j = 0
        while (j < len) {
            hexStr[j] = "" + chars[i] + chars[i + 1]
            bytes[j] = Integer.parseInt(hexStr[j], 16).toByte()
            i += 2
            j++
        }
        return bytes
    }
}