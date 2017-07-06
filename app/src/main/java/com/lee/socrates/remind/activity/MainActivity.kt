package com.lee.socrates.remind.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.lee.socrates.remind.R
import com.lee.socrates.remind.fragment.PasswordListFragment
import com.lee.socrates.remind.util.AccountManager
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by socrates on 2016/4/2.
 */
class MainActivity : BaseActivity() {

    var iconView: ImageView? = null
    var nameView: TextView? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        initNavigationView()
        initToolbar()
        initContentView()
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.mipmap.ic_launcher)
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }
        toolbar.menu.add(0, itemIdSetting, 0, getString(R.string.setting))
        toolbar.setOnMenuItemClickListener {
            menuItem ->
            when (menuItem.itemId) {
                itemIdSetting -> consume { start<SettingActivity>() }
                else -> false
            }
        }
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    inline fun consume(action: () -> Unit): Boolean {
        action()
        return true
    }

    private fun initNavigationView() {
        val headView: View = navigationView.getHeaderView(0)
        iconView = headView.findViewById(R.id.userIcon) as ImageView?
        nameView = headView.findViewById(R.id.userName) as TextView?
        iconView?.setOnClickListener {
            if (AccountManager.hasLoginUser()) {
                ARouter.getInstance().build("/remain/activity/container")
                        .withString("title", "UserInfo")
                        .withString("fragmentName", "userInfo")
                        .navigation()
            } else {
                ARouter.getInstance().build("/remain/activity/container")
                        .withString("fragmentName", "login")
                        .navigation(this, requestCodeLogin)
            }
        }

        //设置侧滑菜单选择监听事件
        navigationView.setNavigationItemSelectedListener({
            menuItem ->
            when (menuItem.itemId) {
                R.id.sub_switch -> consume {
                    //实现换肤
                    drawerLayout.closeDrawers()
                }
                R.id.sub_exit -> consume { finish() }
                else -> consume {
                    toolbar.title = menuItem.title
                    drawerLayout.closeDrawers()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeLogin) {
            if (resultCode == Activity.RESULT_OK) {
                if (!AccountManager.currentAccountName.isNullOrEmpty()) {
                    nameView?.text = AccountManager.currentAccountName
                }
            }
        }
    }

    private fun initContentView() {
        openFragment<PasswordListFragment>()
    }

    companion object {
        private val itemIdSetting: Int = 1
        private val requestCodeLogin: Int = 1 //must big than 0, otherwise ARouter will not call startActivityForResult
    }

}