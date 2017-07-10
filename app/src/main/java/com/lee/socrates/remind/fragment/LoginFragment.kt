package com.lee.socrates.remind.fragment

import android.app.Activity
import android.app.ProgressDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.lee.socrates.remind.R
import com.lee.socrates.remind.entity.User
import com.lee.socrates.remind.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.HashMap

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
        linkSignUp.setOnClickListener {
            ARouter.getInstance().navigation(activity, "register")
        }
    }

    fun login() {
        val progressDialog = ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()
        val hashMap = HashMap<String, Any?>()
        val userName = inputEmail.text.toString()
        hashMap.put("userName", userName)
        hashMap.put("password", inputPassword.text.toString())
        retrofitService.login(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressDialog.dismiss()
                    context.showToast(it.message)
                        if (it.isSuccess){
                            val user: User = User()
                            user.userAccount = userName
                            UserInfoManager.addUser(user)
                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()
                        }
                }, { progressDialog.dismiss() })
    }

}

