package com.lee.library.view.tabswitchwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lee on 2018/4/2.
 */

public class SlidingTabView extends BaseTabLayoutView {
    public SlidingTabView(Context context) {
        super(context);
    }

    public SlidingTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        super.initView();
        setOrientation(VERTICAL);
        LinearLayout linearLayout = new LinearLayout(mContext);
        addView(linearLayout);
        tabContainerView = linearLayout;

    }
}
