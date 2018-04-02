package com.lee.socrates.mvvmdemo.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.lee.socrates.mvvmdemo.R
import com.lee.socrates.mvvmdemo.databinding.ActivityBluetoothBinding

/**
 * Created by lee on 2018/3/20.
 */
class BlueToothActivity : AppCompatActivity() , BlueToothNavigator{
    override fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
    }

    override fun requestOpenBlueTooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    private lateinit var btvm: BluetoothViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var amb: ActivityBluetoothBinding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth)
        btvm = BluetoothViewModel()
        btvm.init(this, this)
        amb.viewModel = btvm
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        btvm.handleActivityResult(requestCode, resultCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        btvm.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        var REQUEST_ENABLE_BT = 1
        var MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2
    }
}