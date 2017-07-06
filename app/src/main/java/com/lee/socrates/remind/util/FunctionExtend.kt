package com.lee.socrates.remind.util

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import com.lee.socrates.remind.R
import com.squareup.picasso.Picasso


fun String.validateUserName(context: Context, minLength: Int = 6, maxLength: Int = 15): Boolean {
    var isValidate = true
    if (this.isNullOrEmpty()) {
        Toast.makeText(context, "userName could not be null", Toast.LENGTH_LONG).show()
        isValidate = false
    } else if (this.length < minLength) {
        isValidate = false
        Toast.makeText(context, "userName length must big than 6", Toast.LENGTH_LONG).show()
    } else if (this.length > maxLength) {
        isValidate = false
        Toast.makeText(context, "userName length must not big than 15", Toast.LENGTH_LONG).show()
    }
    return isValidate
}

fun String.validatePassword(context: Context, minLength: Int = 6, maxLength: Int = 15): Boolean {
    var isValidate = true
    if (this.isNullOrEmpty()) {
        Toast.makeText(context, "userName could not be null", Toast.LENGTH_LONG).show()
        isValidate = false
    } else if (this.length < minLength) {
        isValidate = false
        Toast.makeText(context, "userName length must big than 6", Toast.LENGTH_LONG).show()
    } else if (this.length > maxLength) {
        isValidate = false
        Toast.makeText(context, "userName length must not big than 15", Toast.LENGTH_LONG).show()
    }
    return isValidate
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}

fun ImageView.loadImg(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Picasso.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Picasso.with(context).load(imageUrl).into(this)
    }
}