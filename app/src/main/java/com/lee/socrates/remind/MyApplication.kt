package com.lee.socrates.remind

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Created by lee on 2017/6/29.
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.openLog() // 开启日志
        ARouter.openDebug() // 使用InstantRun的时候，需要打开该开关，上线之后关闭，否则有安全风险
        ARouter.printStackTrace() // 打印日志的时候打印线程堆栈
        ARouter.init(this)
    }

}