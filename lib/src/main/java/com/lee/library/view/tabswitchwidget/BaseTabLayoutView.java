package com.lee.library.view.tabswitchwidget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.lee.library.R;
import com.lee.library.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2018/4/2.
 */
public class BaseTabLayoutView extends LinearLayout {

    /**
     * default value is -1 , it means all tab unselected *
     */
    protected int mCurrentIndex = -1;
    @ColorInt
    protected int activeTextColor;
    @ColorInt
    protected int inactiveTextColor;
    protected Context mContext;
    private static final int defaultActiveTextColor = 0x333333;
    private static final int defaultInactiveTextColor = 0x999999;
    @DrawableRes
    private int mActiveBackground; // resourceId
    @DrawableRes
    private int mInactiveBackground;  // resourceId
    private boolean mHasSeparator;
    private boolean mShowActiveBackground;
    private List<Tab> tabList;
    @ColorInt
    private int mSeparateLineColor;
    private TabListener mTabListener;
    /**
     * 当前选中的tab左右两边的分割线是否显示
     */
    private boolean isShowSepLineNearCurIndex = true;

    //每个tab的显示样式
    private TabStyle mTabStyle = new TextImageTabStyle();
    /**
     * tabview存放的容器
     */
    protected LinearLayout tabContainerView;

    public BaseTabLayoutView(Context context) {
        this(context, null);
    }

    public BaseTabLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        parseAttributeSet(context, attrs, defStyleAttr);
        initData();
        initView();
    }

    protected void parseAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        final Resources res = getResources();
        final int defaultActiveBackground = R.color.defaultActiveBackground;
        final int defaultInactiveBackground = R.color.defaultInactiveBackground;
        final boolean defaultHasSeparator = res.getBoolean(R.bool.defaultHasSeparator);
        final boolean defaultShowActiveBackground = res.getBoolean(R.bool.defaultShowActiveBackground);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseTabLayoutView, defStyleAttr, 0);
        activeTextColor =
                a.getColor(R.styleable.BaseTabLayoutView_activeTextColor, defaultActiveTextColor);
        inactiveTextColor =
                a.getColor(R.styleable.BaseTabLayoutView_inactiveTextColor, defaultInactiveTextColor);
        mActiveBackground = a.getResourceId(R.styleable.BaseTabLayoutView_activeBackground, defaultActiveBackground);
        mInactiveBackground = a.getResourceId(R.styleable.BaseTabLayoutView_inactiveBackground, defaultInactiveBackground);
        mHasSeparator = a.getBoolean(R.styleable.BaseTabLayoutView_hasSeparator, defaultHasSeparator);
        mShowActiveBackground = a.getBoolean(R.styleable.BaseTabLayoutView_showActiveBackground, defaultShowActiveBackground);
        String tabStyle = a.getString(R.styleable.BaseTabLayoutView_tabStyle);
        initTabStyle(tabStyle);
        mTabStyle.parseAttributes(a);

        a.recycle();
    }

    protected void initData(){
        mSeparateLineColor = mContext.getResources().getColor(R.color.default_separate_line_color);
        tabList = new ArrayList<>();
    }

    protected void initView(){
        tabContainerView = this;
    }

    private void initTabStyle(String tabStyle) {
        if(!TextUtils.isEmpty(tabStyle)){
            if(getResources().getString(R.string.tabStyleTextImage).equals(tabStyle)){
                mTabStyle = new TextImageTabStyle();
            } else if (getResources().getString(R.string.tabStyleTwoLineText).equals(tabStyle)){
                mTabStyle = new TwoLineTextTabStyle();
            }
        }
    }

    /**
     * 当前选中的tab左右两边的分割线是否显示
     */
    public BaseTabLayoutView isShowSepLineNearCurIndex(boolean isShowSepLineNearCurIndex) {
        this.isShowSepLineNearCurIndex = isShowSepLineNearCurIndex;
        return this;
    }

    public BaseTabLayoutView clearAllTabView() {
        tabContainerView.removeAllViews();
        if (tabList != null) {
            tabList.clear();
        }
        return this;
    }

    public BaseTabLayoutView addTab(Tab tab) {
        tab.index = tabList.size();
        tabList.add(tab);
        addTabView(tab);
        return this;
    }

    /**
     * 设置当前选中的tab,该方法不会产生tab点击时的回调事件，
     * 所以，要手动加载index对应的fragment布局（或手动调用viewpager设置对应的显示页面）
     *
     * @param index 设置为当前选中tab的索引值
     */
    public BaseTabLayoutView setCurrentIndex(int index) {
        setCurrentIndex(index, false);
        return this;
    }

    /**
     * 设置当前选中的tab
     *
     * @param index        设置为当前选中tab的索引值
     * @param notifyChange 是否发送tab选中状态改变的事件
     */
    public BaseTabLayoutView setCurrentIndex(int index, boolean notifyChange) {
        if (index < 0 || index >= tabList.size()) {
            throw new IllegalArgumentException("index is invalid");
        }
        updateCurrentTab(index, notifyChange);
        return this;
    }

    /**
     * @return current selected tab index the value >=0 , otherwise if all tab unselected return -1
     */
    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public BaseTabLayoutView setAllTabUnselected() {
        for (Tab tab : tabList) {
            tab.isActive = false;
        }
        mCurrentIndex = -1;
        resetTabLayout();
        return this;
    }

    /**
     * 设置或点击某个tab后，调用这个方法更新TabLayoutView的显示
     *
     * @param index        当前选中的tab的索引
     * @param notifyChange true:调用tabListener通知tab选中状态有改变； false：不通知
     */
    private void updateCurrentTab(int index, boolean notifyChange) {
        mCurrentIndex = index;
        for (int i = 0; i < tabList.size(); i++) {
            if (i == index) {
                tabList.get(i).isActive = true;
            } else {
                tabList.get(i).isActive = false;
            }
        }
        resetTabLayout();
        if (notifyChange) {
            notifyTabSelected();
        }
    }

    private void resetTabLayout() {
        tabContainerView.removeAllViews();
        if (tabList != null && tabList.size() > 0) {
            for (int i = 0; i < tabList.size(); i++) {
                Tab tab = tabList.get(i);
                if (i != 0 && mHasSeparator) {
                    if (isShowSepLineNearCurIndex) {
                        addSeparatorLine();
                    } else {
                        if (i != mCurrentIndex && i != mCurrentIndex + 1) {
                            addSeparatorLine();
                        }
                    }
                }
                if (tab.isActive) {
                    mCurrentIndex = tab.index;
                }
                addTabView(tab);
            }
        }
    }

    private void addSeparatorLine() {
        View view = new View(mContext);
        LayoutParams layoutParams = new LayoutParams(mContext.getResources().getDimensionPixelOffset(R.dimen.separate_line_height), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = DensityUtil.dipToPx(mContext, 7);
        layoutParams.bottomMargin = DensityUtil.dipToPx(mContext, 7);
        view.setBackgroundColor(mSeparateLineColor);
        tabContainerView.addView(view, layoutParams);
    }

    private void addTabView(Tab tab) {
        final View singleTabView = getSingleTabView(tab);
        singleTabView.setTag(tab);
        mTabStyle.initSingleTabView(singleTabView, tab, this);
        if (mShowActiveBackground && tab.isActive) {
            singleTabView.setBackgroundResource(mActiveBackground);
        } else {
            singleTabView.setBackgroundResource(mInactiveBackground);
        }
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tabContainerView.addView(singleTabView, layoutParams);
        singleTabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ((Tab) singleTabView.getTag()).index;
                if (index != mCurrentIndex) {
                    //maybe this isn't the good way to update view
                    updateCurrentTab(index, true);
                } else {
                    notifyTabReselected();
                }
            }
        });
    }

    private View getSingleTabView(Tab tab) {
        if (tab.tabViewLayoutId == 0) {
            tab.tabViewLayoutId = mTabStyle.getDefaultTabLayoutResId();
        }
        return LayoutInflater.from(mContext).inflate(tab.tabViewLayoutId, this, false);
    }

    private void notifyTabSelected() {
        Tab curTab = tabList.get(mCurrentIndex);
        if (mTabListener != null) {
            mTabListener.onTabSelected(curTab);
        }
    }

    private void notifyTabReselected() {
        Tab curTab = tabList.get(mCurrentIndex);
        if (mTabListener != null) {
            mTabListener.onTabReselected(curTab);
        }
    }

    public abstract static class Tab {
        public int index;
        public boolean isActive;

        /**
         * single Tab View Layout xml Id
         */
        public int tabViewLayoutId;
    }

    public void setTabListener(TabListener tabListener) {
        this.mTabListener = tabListener;
    }

    public interface TabListener {
        void onTabSelected(Tab tab);

        void onTabReselected(Tab tab);
    }

    /**
     * 实现这个接口可以定义不同显示样式的tab，以及对应于这个样式的一些初始化逻辑
     */
    public interface TabStyle {
        /**
         * 解析一个样式特有的属性值
         * @param typedArray
         */
        void parseAttributes(TypedArray typedArray);

        /**
         * 当用户没有设置每个tab使用的资源文件时，使用这个作为默认的布局资源，用来初始化一个tab对应的View
         */
        int getDefaultTabLayoutResId();

        /**
         * 根据对应的Tab初始化单个TabView
         *  @param view
         * @param tab
         * @param baseTabLayoutView
         */
        void initSingleTabView(View view, Tab tab, BaseTabLayoutView baseTabLayoutView);
    }
}
