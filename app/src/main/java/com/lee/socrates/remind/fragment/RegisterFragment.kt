package com.lee.socrates.remind.fragment

import android.app.ProgressDialog
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.lee.library.network.NetService
import com.lee.socrates.remind.R
import com.lee.socrates.remind.entity.Account
import com.lee.socrates.remind.service.RetrofitService
import com.lee.socrates.remind.util.AccountManager
import com.lee.socrates.remind.util.showToast
import com.lee.socrates.remind.util.validatePassword
import com.lee.socrates.remind.util.validateUserName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.HashMap

/**
 * Created by socrates on 2016/4/4.
 */
@Route(path = "/remain/fragment/register")
class RegisterFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initView() {
        btnRegister.setOnClickListener {
            if (!inputEmail.text.toString().validateUserName(context)) {
                return@setOnClickListener
            }
            if (!inputPassword.text.toString().validatePassword(context)) {
                return@setOnClickListener
            }
            if (confirmPassword.text.toString() != inputPassword.text.toString()) {
                context.showToast(getString(R.string.password_not_same))
                return@setOnClickListener
            }
            register()
        }
    }

    fun register() {
        val progressDialog = ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("register...")
        progressDialog.show()
        val hashMap = HashMap<String, Any?>()
        val userName = inputEmail.text.toString()
        hashMap.put("userName", userName)
        hashMap.put("password", inputPassword.text.toString())
        NetService<RetrofitService>(RetrofitService::class.java).netApi.register(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressDialog.dismiss()
                    context.showToast(it.message)
                    if (it.isSuccess){
                        activity.finish()
                    }
                }, { progressDialog.dismiss() })
    }
}