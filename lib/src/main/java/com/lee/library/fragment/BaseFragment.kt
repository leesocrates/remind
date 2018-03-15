package com.lee.library.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by socrates on 2016/4/3.
 */
abstract class BaseFragment : Fragment() {
    protected lateinit var rootView: View
    protected val TAG: String= this.javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater?.inflate(getLayoutId(), container, false) ?:
                LayoutInflater.from(context.applicationContext).inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()

}