package com.lee.library.fragment

import android.support.v4.app.Fragment
import com.lee.library.view.tabswitchwidget.SlidingTabView
import com.lee.library.view.tabswitchwidget.TabLayoutView

/**
 * Created by lee on 2018/4/9.
 */
abstract class BaseTabFragment : BaseFragment() {

    private lateinit var slidingTabView: SlidingTabView
    override fun initView() {
        initSlidingTabView()
    }


    private fun initSlidingTabView() {
        val config = SlidingTabView.Config(context)
        initConfig(config)
        slidingTabView.initSlidingTabView(fragmentManager, config)
        val tabList: List<TabLayoutView.Tab> = getTabList()
        slidingTabView.addTabList(tabList)
        val fragmentList = ArrayList<Fragment>()
        initFragmentList(fragmentList)
        slidingTabView.addFragmentList(fragmentList)
    }

    protected abstract fun getTabList(): List<TabLayoutView.Tab>
    protected abstract fun initFragmentList(fragmentList: List<Fragment>)

    protected fun initConfig(config: SlidingTabView.Config) {
        config.isTopTabStyle = false
    }

}