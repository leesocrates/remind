package com.lee.library.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * Created by socrates on 2016/3/26.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        init()
    }

    abstract fun getLayoutId(): Int
    abstract fun init()

    inline fun <reified T : Fragment> openFragment(@IdRes fragmentContainer: Int, bundle: Bundle? = null) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val f: Fragment = T::class.java.newInstance()
        f.arguments = bundle
        fragmentTransaction.add(fragmentContainer, f, T::class.java.canonicalName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun openFragment(f: Fragment,tag: String?, isAddToBackStack: Boolean?, @IdRes fragmentContainer: Int){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(fragmentContainer, f, tag)
        if(isAddToBackStack == true){
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
}