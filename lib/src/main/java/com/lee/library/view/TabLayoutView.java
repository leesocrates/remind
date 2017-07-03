package com.lee.library.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.library.R;
import com.lee.library.util.CollectionUtils;
import com.lee.library.util.DensityUtil;
import com.lee.library.util.ResUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * Created by lee on 2015/11/12.
 */
public class TabLayoutView extends LinearLayout {

    private int mActiveTextColor;
    private int mInactiveTextColor;
    private int mActiveBackground;
    private int mInactiveBackground;
    private boolean mHasSeparator;
    private boolean mShowActiveBackground;
    private List<Tab> tabList;
    private Context mContext;
    /**
     * default value is -1 , it means all tab unselected *
     */
    private int mCurrentIndex = -1;
    private int mSeparateLineColor;

    public TabLayoutView(Context context) {
        this(context, null);

    }

    public TabLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources res = getResources();
        final int defaultActiveTextColor = res.getColor(R.color.defaultActiveTextColor);
        final int defaultInactiveTextColor = res.getColor(R.color.defaultInactiveTextColor);
        final int defaultActiveBackground = res.getColor(R.color.defaultActiveBackground);
        final int defaultInactiveBackground = res.getColor(R.color.defaultInactiveBackground);
        final boolean defaultHasSeparator = res.getBoolean(R.bool.defaultHasSeparator);
        final boolean defaultShowActiveBackground = res.getBoolean(R.bool.defaultShowActiveBackground);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayoutView, defStyleAttr, 0);
        mActiveTextColor = a.getInt(R.styleable.TabLayoutView_activeTextColor, defaultActiveTextColor);
        mActiveBackground = a.getInt(R.styleable.TabLayoutView_activeBackground, defaultActiveBackground);
        mInactiveTextColor = a.getInt(R.styleable.TabLayoutView_inactiveTextColor, defaultInactiveTextColor);
        mInactiveBackground = a.getInt(R.styleable.TabLayoutView_inactiveBackground, defaultInactiveBackground);
        mHasSeparator = a.getBoolean(R.styleable.TabLayoutView_hasSeparator, defaultHasSeparator);
        mShowActiveBackground = a.getBoolean(R.styleable.TabLayoutView_showAvtiveBackground, defaultShowActiveBackground);
        a.recycle();

        mSeparateLineColor = ResUtils.getColor(R.color.default_separate_line_color);
        tabList = new ArrayList<>();
        mContext = context;
    }

    public void clearAllTabView() {
        removeAllViews();
        if (tabList != null) {
            tabList.clear();
        }
    }

    public void addTab(Tab tab) {
        tab.index = tabList.size();
        tabList.add(tab);
        addTabView(tab);
    }

    /**
     *设置当前显示的tab,该方法不会产生tab点击时的回调事件，所以，要手动加载index对应的fragment布局（或手动调用viewpager设置对应的显示页面）
     * @param index
     */
    public void setCurrentIndex(int index) {
        if (index < 0 || index >= tabList.size()) {
            throw new IllegalArgumentException("index is invalid");
        }
        mCurrentIndex = index;
        updateCurrentTab(index, false);
    }

    /** current selected tab index >=0 , if all tab unselected this value is -1 **/
    /**
     * @return current selected tab index the value >=0 , otherwise if all tab unselected return -1
     */
    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setAllTabUnselected() {
        for (Tab tab : tabList) {
            tab.isActive = false;
        }
        mCurrentIndex = -1;
        resetTabLayout();
    }

    /**
     *

     */
    private void updateCurrentTab(int index, boolean notifyChange) {
        for (int i = 0; i < tabList.size(); i++) {
            if (i == index) {
                tabList.get(i).isActive = true;
            } else {
                tabList.get(i).isActive = false;
            }
        }
        resetTabLayout();
        if(notifyChange){
            notifyCurrentTabChange();
        }
    }

    private void resetTabLayout() {
        removeAllViews();
        if (!CollectionUtils.isNull(tabList)) {
            for (int i = 0; i < tabList.size(); i++) {
                Tab tab = tabList.get(i);
                if (i != 0 && mHasSeparator) {
                    addSeparatorLine();
                }
                addTabView(tab);
            }
        }
    }

    private void addSeparatorLine() {
        View view = new View(mContext);
        LayoutParams layoutParams = new LayoutParams(ResUtils.getDimensionPixelOffset(R.dimen.separate_line_height), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = DensityUtil.dipToPx(mContext, 6);
        layoutParams.bottomMargin = DensityUtil.dipToPx(mContext, 6);
        view.setBackgroundColor(mSeparateLineColor);
        addView(view, layoutParams);
    }

    private void notifyCurrentTabChange() {
        Tab curTab = tabList.get(mCurrentIndex);
        if (curTab.tabSelectedAction != null) {
            Observable.just(curTab).subscribe(curTab.tabSelectedAction);
        }
    }

    private void notifyTabReselected() {
        Tab curTab = tabList.get(mCurrentIndex);
        if (curTab.tabReselectedAction != null) {
            Observable.just(curTab).subscribe(curTab.tabReselectedAction);
        }
    }

    private void addTabView(Tab tab) {
        final View view = LayoutInflater.from(mContext).inflate(tab.tabViewLayoutId, this, false);
        view.setTag(tab);
        setTabView(view, tab);
        if (mShowActiveBackground && tab.isActive) {
            view.setBackgroundColor(mActiveBackground);
        } else {
            view.setBackgroundColor(mInactiveBackground);
        }
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        this.addView(view, layoutParams);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ((Tab)view.getTag()).index;
                if (index != mCurrentIndex) {
                    //maybe this isn't the good way to update view
                    updateCurrentTab(index, true);
                } else {
                    notifyTabReselected();
                }
            }
        });
    }

    private void setTabView(View view, Tab tab){
        if(tab instanceof TextImageTab){

            TextView name = (TextView) view.findViewById(R.id.tab_name);
            ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
            name.setText(((TextImageTab) tab).name);
            if (tab.isActive) {
                mCurrentIndex = tab.index;
                name.setTextColor(mActiveTextColor);
                icon.setBackgroundResource(((TextImageTab)tab).activeIconResId);
            } else {
                name.setTextColor(mInactiveTextColor);
                icon.setBackgroundResource(((TextImageTab)tab).inactiveIconResId);
            }
        } else if (tab instanceof  TwoLineTextTab){
            TextView topTextView = (TextView) view.findViewById(R.id.tab_top_textview);
            TextView bottomTextView = (TextView) view.findViewById(R.id.tab_bottom_textview);
            topTextView.setText(((TwoLineTextTab)tab).topText);
            bottomTextView.setText(((TwoLineTextTab) tab).bottomText);
            if (tab.isActive) {
                mCurrentIndex = tab.index;
                topTextView.setTextColor(mActiveTextColor);
                bottomTextView.setTextColor(mActiveTextColor);
            } else {
                topTextView.setTextColor(mInactiveTextColor);
                bottomTextView.setTextColor(mInactiveTextColor);
            }
        } else {
            throw new IllegalArgumentException("tab is not correctly instantiated");
        }

    }

    public abstract static class Tab {
        public int index;
        public boolean isActive;

        /**
         * single Tab View Layout xml Id
         */
        @NonNull
        public int tabViewLayoutId;
        /**
         * action receive current tab change event
         *
         * @params Tab current active tab
         */
        @NonNull
        public Consumer<Tab> tabSelectedAction;
        public Consumer<Tab> tabReselectedAction;
    }

    public static class TextImageTab extends Tab{
        public String name;
        public int activeIconResId;
        public int inactiveIconResId;
    }

    public static class TwoLineTextTab extends Tab{
        public String topText;
        public String bottomText;
    }

    public static class TabFactory {
        public static Tab getMainTab(String topText, String bottomText, Consumer<Tab> onTabSelectedAction){
            TwoLineTextTab twoLineTextTab = new TwoLineTextTab();
            twoLineTextTab.tabViewLayoutId = R.layout.view_tab_twolinetext;
            twoLineTextTab.topText = topText;
            twoLineTextTab.bottomText = bottomText;
            twoLineTextTab.tabSelectedAction =  onTabSelectedAction;
            return twoLineTextTab;
        }
    }
}
