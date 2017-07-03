package com.lee.socrates.remind.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.lee.socrates.remind.R

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

    inline fun <reified T : AppCompatActivity> start() {
        startActivity(Intent(this, T::class.java))
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inline fun <reified T : Fragment> openFragment(bundle: Bundle? = null) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val f: Fragment = T::class.java.newInstance()
        f.arguments = bundle
        fragmentTransaction.add(R.id.fragment_container, f, T::class.java.canonicalName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun openFragment(f: Fragment,tag: String?, isAddToBackStack: Boolean?){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, f, tag)
        if(isAddToBackStack == true){
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
}