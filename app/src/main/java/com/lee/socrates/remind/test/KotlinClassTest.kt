package com.lee.socrates.remind.test

/**
 * Created by lee on 2017/7/13.
 */
class KotlinClassTest {

    lateinit var a: String

    fun show(){
        test { return@test }
        testInline { return }
    }
    fun test(body: ()->Unit){
    }

    inline fun testInline(body: ()->Unit){
    }

}