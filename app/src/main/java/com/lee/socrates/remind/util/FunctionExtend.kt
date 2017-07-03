package com.lee.socrates.remind.util

import android.content.Context
import android.widget.Toast


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

fun Context.showShortToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}