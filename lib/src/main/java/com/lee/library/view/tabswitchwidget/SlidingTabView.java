package com.lee.library.view.tabswitchwidget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lee.library.R;
import com.lee.library.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2018/4/2.
 */

public class SlidingTabView extends BaseTabLayoutView {

    private ViewPager mViewPager;
    private Config mConfig;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private ArrayList<Fragment> pagerItemList = null;

    public SlidingTabView(Context context) {
        super(context);
    }

    public SlidingTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * @param fragmentManager
     * @param config
     * @return
     */
    public SlidingTabView initSlidingTabView(FragmentManager fragmentManager, Config config) {
        setOrientation(VERTICAL);
        initConfig(config);
        tabContainerView = new LinearLayout(mContext);
        this.addOnTabChangeListener(new MyOnTabChangeListener());
        mViewPager = new ViewPager(mContext);
        mViewPager.setId(R.id.sliding_view_pager);
        pagerItemList = new ArrayList<>();
        mFragmentPagerAdapter = new SlidingFragmentPagerAdapter(fragmentManager, pagerItemList);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setOffscreenPageLimit(3);
        LayoutParams tabContainerLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mConfig.mTabLayoutHeight);
        LayoutParams viewPagerLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        if (mConfig.topTabStyle) {
            addView(tabContainerView, tabContainerLp);
            addView(mViewPager, viewPagerLp);
        } else {
            addView(mViewPager, viewPagerLp);
            addView(tabContainerView, tabContainerLp);
        }
        return this;
    }

    public SlidingTabView addOnpagerChangeListener(ViewPager.OnPageChangeListener onPageChangeListener){
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        return this;
    }

    public SlidingTabView addFragmentList(List<Fragment> fragments){
        pagerItemList.addAll(fragments);
        mFragmentPagerAdapter.notifyDataSetChanged();
        return this;
    }

    private void initConfig(Config config) {
        mConfig = config;
        if (mConfig == null) {
            mConfig = new Config(mContext);
        }
    }

    private class MyOnTabChangeListener implements OnTabChangeListener {

        @Override
        public void onTabSelected(Tab tab) {
            mViewPager.setCurrentItem(tab.index);
        }

        @Override
        public void onTabReselected(Tab tab) {

        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setCurrentIndex(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    public static class Config {
        private int mTabLayoutHeight;
        private boolean topTabStyle;

        public Config(Context mContext) {
            mTabLayoutHeight = DensityUtil.dipToPx(mContext, 48);
        }

        public int getmTabLayoutHeight() {
            return mTabLayoutHeight;
        }

        public Config setmTabLayoutHeight(int mTabLayoutHeight) {
            this.mTabLayoutHeight = mTabLayoutHeight;
            return this;
        }

        public boolean isTopTabStyle() {
            return topTabStyle;
        }

        public Config setTopTabStyle(boolean topTabStyle) {
            this.topTabStyle = topTabStyle;
            return this;
        }
    }
}
