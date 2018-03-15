package com.lee.library.util

fun String?.notNullOrEmpty(f:()->Unit){
    if(this!=null && this.isNotEmpty()){
        f()
    }
}
