package com.lee.socrates.mvvmdemo.bluetooth

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lee.socrates.mvvmdemo.R
import com.lee.socrates.mvvmdemo.databinding.ActivityBluetoothBinding

/**
 * Created by lee on 2018/3/20.
 */
class BlueToothActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var amb: ActivityBluetoothBinding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth)
        var btvm = BluetoothViewModel()
        btvm.init(this)
        amb.viewModel = btvm
    }
}