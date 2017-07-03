package com.lee.socrates.remind.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by socrates on 2016/4/3.
 */
abstract class BaseFragment : Fragment() {
    protected lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(getLayoutId(), container, false) ?:
                LayoutInflater.from(context.applicationContext).inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    inline fun <reified T : AppCompatActivity> start() {
        startActivity(Intent(activity, T::class.java))
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()


}