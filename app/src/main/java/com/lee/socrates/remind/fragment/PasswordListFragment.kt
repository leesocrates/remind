package com.lee.socrates.remind.fragment

import com.lee.socrates.remind.R
import com.lee.socrates.remind.activity.AddPasswordActivity
import kotlinx.android.synthetic.main.fragment_password_list.*

/**
 * Created by socrates on 2016/4/3.
 */
class PasswordListFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_password_list
    }

    override fun initView() {
        fab.setOnClickListener { start<AddPasswordActivity>() }
    }
}