package com.lee.socrates.remind.fragment

import com.lee.library.fragment.BaseFragment
import com.lee.socrates.remind.service.RemindApi
import com.lee.library.network.RetrofitConfig

/**
 * Created by socrates on 2016/4/3.
 */
abstract class AppBaseFragment : BaseFragment() {
    protected val retrofitService: RemindApi by lazy { RetrofitConfig.retrofit.create(RemindApi::class.java) }

}