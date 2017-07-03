package com.lee.socrates.remind.fragment

import android.app.ProgressDialog
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.lee.library.network.NetService
import com.lee.socrates.remind.R
import com.lee.socrates.remind.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.HashMap
import com.lee.socrates.remind.util.validateUserName
import com.lee.socrates.remind.util.validatePassword

/**
 * Created by socrates on 2016/4/4.
 */
@Route(path = "/remain/fragment/login")
class LoginFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {
        btnLogin.setOnClickListener {
            if (!inputEmail.text.toString().validateUserName(context)) {
                return@setOnClickListener
            }
            if (!inputPassword.text.toString().validatePassword(context)) {
                return@setOnClickListener
            }
            login()
        }
    }

    fun login() {
        val progressDialog = ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()
        val hashMap = HashMap<String, Any?>()
        hashMap.put("userName", inputEmail.text.toString())
        hashMap.put("password", inputPassword.text.toString())
        NetService<RetrofitService>(RetrofitService::class.java).netApi.login(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressDialog.dismiss()
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }, { progressDialog.dismiss() })
    }

}

