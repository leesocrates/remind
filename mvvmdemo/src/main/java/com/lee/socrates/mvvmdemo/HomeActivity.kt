package com.lee.socrates.mvvmdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.lee.library.view.tabswitchwidget.BaseTabLayoutView
import com.lee.library.view.tabswitchwidget.SlidingTabView
import com.lee.library.view.tabswitchwidget.TextImageTabStyle
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Created by lee on 2018/4/3.
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val config = SlidingTabView.Config(this.applicationContext)
        config.isTopTabStyle = false
        homeSlidingTabView.initSlidingTabView(supportFragmentManager, config)
        val tabList: List<BaseTabLayoutView.Tab> = TextImageTabStyle.TabFactory.getTabList(arrayOf("tab1", "tab2", "tab3"),
                kotlin.IntArray(0), kotlin.IntArray(0),0)
        homeSlidingTabView.addTabList(tabList)
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(TestFragment())
        fragmentList.add(TestFragment())
        fragmentList.add(TestFragment())
        homeSlidingTabView.addFragmentList(fragmentList)
    }
}