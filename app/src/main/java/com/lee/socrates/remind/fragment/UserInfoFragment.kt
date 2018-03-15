package com.lee.socrates.remind.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.lee.socrates.remind.R

/**
 * Created by socrates on 2016/4/4.
 */
@Route(path = "/remain/fragment/userInfo")
class UserInfoFragment : AppBaseFragment(){
    override fun getLayoutId(): Int {
       return R.layout.fragment_user_info
    }

    override fun initView() {

    }
}