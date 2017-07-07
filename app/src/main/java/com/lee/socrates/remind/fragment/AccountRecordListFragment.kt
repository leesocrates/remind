package com.lee.socrates.remind.fragment

import com.alibaba.android.arouter.launcher.ARouter
import com.lee.socrates.remind.R
import kotlinx.android.synthetic.main.fragment_account_list.*

/**
 * Created by socrates on 2016/4/3.
 */
class AccountRecordListFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_account_list
    }

    override fun initView() {
        fab.setOnClickListener {
            ARouter.getInstance().build("/remain/activity/container")
                    .withString("title", "AddAccountRecord")
                    .withString("fragmentName", "addAccountRecord")
                    .navigation()
        }
    }
}