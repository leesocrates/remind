package com.lee.socrates.mvvmdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.lee.library.view.tabswitchwidget.BaseTabLayoutView
import com.lee.library.view.tabswitchwidget.SlidingTabView
import com.lee.library.view.tabswitchwidget.TextImageTabStyle
import com.lee.library.view.tagwidget.TagFactory
import com.lee.library.view.tagwidget.TagLayoutConfig
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Created by lee on 2018/4/3.
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initSlidingTabView()
        initTagLayoutView()
    }

    private fun initSlidingTabView() {
        val config = SlidingTabView.Config(this.applicationContext)
        config.isTopTabStyle = false
        homeSlidingTabView.initSlidingTabView(supportFragmentManager, config)
        val tabList: List<BaseTabLayoutView.Tab> = TextImageTabStyle.TabFactory.getTabList(arrayOf("tab1", "tab2", "tab3"),
                IntArray(0), IntArray(0), 0)
        homeSlidingTabView.addTabList(tabList)
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(TestFragment())
        fragmentList.add(TestFragment())
        fragmentList.add(TestFragment())
        homeSlidingTabView.addFragmentList(fragmentList)
    }

    private fun initTagLayoutView() {
        val config = TagLayoutConfig(applicationContext)
        config.showModel = TagLayoutConfig.ShowModel.ROUNDED_RECTANGLE
        val tagList = TagFactory.getTagList(arrayOf("tag1", "tag2"))
        homeTagLayoutView.init(config, null)
                .showTags(tagList)

    }
}