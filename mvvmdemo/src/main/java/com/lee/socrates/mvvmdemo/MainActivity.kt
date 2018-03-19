package com.lee.socrates.mvvmdemo

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lee.socrates.mvvmdemo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mavm: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var amb: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mavm = MainActivityViewModel()
        mavm.title.set("ok")
        amb.viewModel = mavm
        val bytes: ByteArray = multiLayerView.drawableToByteArray(textView.background)
        val bg: Drawable = multiLayerView.byteArrayToDrawable(bytes)
        textView.background = bg
        multiLayerView.addLayer(bytes)
    }


}
