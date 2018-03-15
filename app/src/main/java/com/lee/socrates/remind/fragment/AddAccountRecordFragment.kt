package com.lee.socrates.remind.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.lee.socrates.remind.R

/**
 * Created by lee on 2017/7/7.
 */
@Route(path = "/remain/fragment/addAccountRecord")
class AddAccountRecordFragment : AppBaseFragment(){

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_account
    }

    override fun initView() {
    }
}