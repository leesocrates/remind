package com.lee.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lee.library.R;

/**
 * Created by lee on 2016/1/13.
 * 导航栏
 */
public class TitleBarView extends RelativeLayout {

    private LinearLayout mNavigationLayout;
    private LinearLayout mMenuLayout;
    private RelativeLayout mCustomLayout;
    private ImageView mNavigationIconView;
    private ImageView mAppLogoView;
    private TextView mNavigationTextView;
    private TextView mTitleTextView;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private float mTitleTextSize;
    private float mNavigationTextSize;
    private int mTitleTextColor;
    private int mNavigationTextColor;
    private int marginLeft;
    private int marginRight;
    private int contentMargin;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.titleBar);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_title_bar, this, false);
        mNavigationLayout = (LinearLayout) view.findViewById(R.id.title_navigation_layout);
        mCustomLayout = (RelativeLayout) view.findViewById(R.id.title_custom_layout);
        mMenuLayout = (LinearLayout) view.findViewById(R.id.title_menu_layout);
        mNavigationIconView = (ImageView) view.findViewById(R.id.navigation_icon);
        mNavigationTextView = (TextView) view.findViewById(R.id.navigation_text);
        mAppLogoView = (ImageView) view.findViewById(R.id.app_logo);
        this.addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int defaultTitleTextColor = getResources().getColor(R.color.defaultTitleTextColor);
        int defaultNavigationTextColor = getResources().getColor(R.color.defaultNavigationTextColor);
        float defaultTitleTextSize = getResources().getDimension(R.dimen.defaultTitleTextSize);
        float defaultNavigationTextSize = getResources().getDimension(R.dimen.defaultNavigationTextSize);
        int defaultMarginLeft = getResources().getDimensionPixelOffset(R.dimen.defaultMarginLeft);
        int defaultMarginRight = getResources().getDimensionPixelOffset(R.dimen.defaultMarginRight);
        int defaultContentMargin = getResources().getDimensionPixelOffset(R.dimen.defaultContentMargin);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView, defStyleAttr, 0);
        mTitleTextColor = a.getColor(R.styleable.TitleBarView_centerTitleTextColor, defaultTitleTextColor);
        mTitleTextSize = a.getDimension(R.styleable.TitleBarView_titleTextSize, defaultTitleTextSize);
        mNavigationTextColor = a.getColor(R.styleable.TitleBarView_navigationTextColor, defaultNavigationTextColor);
        mNavigationTextSize = a.getDimension(R.styleable.TitleBarView_navigationTextSize, defaultNavigationTextSize);
        marginLeft = a.getDimensionPixelOffset(R.styleable.TitleBarView_marginLeft, defaultMarginLeft);
        marginRight = a.getDimensionPixelOffset(R.styleable.TitleBarView_marginRight, defaultMarginRight);
        contentMargin = a.getDimensionPixelOffset(R.styleable.TitleBarView_contentMargin, defaultContentMargin);
        a.recycle();
        mMenuLayout.setPadding(0, 0, marginRight, 0);
        mNavigationLayout.setPadding(marginLeft, 0, 0, 0);
    }

    public TitleBarView setNavigationIcon(@DrawableRes int resId) {
        mNavigationIconView.setBackgroundResource(resId);
        return this;
    }

    public TitleBarView setNavigationText(CharSequence navigationText) {
        if (!TextUtils.isEmpty(navigationText)) {
            if (mNavigationTextColor != 0) {
                mNavigationTextView.setTextColor(mNavigationTextColor);
            }
            if (mNavigationTextSize != 0) {
                setTextSize(mNavigationTextView, mNavigationTextSize);
            }
            mNavigationTextView.setText(navigationText);
        }
        return this;
    }

    public TitleBarView setAppLogo(@DrawableRes int resId) {
        mAppLogoView.setBackgroundResource(resId);
        return this;
    }

    public TitleBarView setOnNavigationLayoutClick(OnClickListener onClickListener) {
        mNavigationLayout.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置title, titleview将被添加到{@link #mCustomLayout}中,
     * mCustomLayout只能添加一个childview,mCustomLayout 每次addview的时候都会remove先前添加的view
     * 如果调用了{@link #setCustomView(View)},则先前set的title会被移除，不再显示，反之一样
     *
     * @param title 居中的标题
     * @return
     */
    public TitleBarView setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) {
                mTitleTextView = new TextView(getContext());
                mTitleTextView.setSingleLine();
                mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                LayoutParams llp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llp.addRule(RelativeLayout.CENTER_IN_PARENT);
                if (mTitleTextColor != 0) {
                    mTitleTextView.setTextColor(mTitleTextColor);
                }
                if (mTitleTextSize != 0) {
                    setTextSize(mTitleTextView, mTitleTextSize);
                }
                mTitleTextView.setLayoutParams(llp);
                addViewToCustomLayout(mTitleTextView);
            }
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        return this;
    }

    public TitleBarView setTitleTextColor(@ColorInt int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(mTitleTextColor);
        }
        return this;
    }

    /**
     * 设置左边导航布局中的文字大小
     *
     * @param textSize 单位是px
     * @return
     */
    public TitleBarView setTitleTextSize(float textSize) {
        mTitleTextSize = textSize;
        if (mTitleTextView != null) {
            setTextSize(mTitleTextView, mTitleTextSize);
        }
        return this;
    }

    public TitleBarView setNavigationTextColor(@ColorInt int color) {
        mNavigationTextColor = color;
        if (mNavigationTextView != null) {
            mNavigationTextView.setTextColor(mNavigationTextColor);
        }
        return this;
    }

    /**
     * 设置左边导航布局中的文字大小
     *
     * @param textSize 单位是px
     * @return
     */
    public TitleBarView setNavigationTextSize(float textSize) {
        mNavigationTextSize = textSize;
        if (mNavigationTextView != null) {
            setTextSize(mNavigationTextView, mNavigationTextSize);
        }
        return this;
    }

    private void setTextSize(TextView textView, float textSize) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * 添加titlebar中间部分显示的内容， customview将被add到{@link #mCustomLayout}, 多次调用不会添加多个view到 mCustomLayout, 只有最后添加的view会显示
     *
     * @param customview 添加的customview
     */
    public TitleBarView setCustomView(View customview) {
        addViewToCustomLayout(customview);
        return this;
    }

    /**
     * 添加view到{@link #mCustomLayout},mCustomLayout只会有一个childview，每次add新的view的时候， 都会移除已添加的view
     *
     * @param view
     */
    private void addViewToCustomLayout(View view) {
        mCustomLayout.removeAllViews();
        mCustomLayout.addView(view);
    }

    public TitleBarView setIconMenuItem(int menuId, @DrawableRes int resId) {
        if (resId != 0) {
            addMenuItem(menuId, resId, "");
        }
        return this;
    }

    public TitleBarView setTextMenuItem(int menuId, @DrawableRes int resId, CharSequence menuText) {
        if (resId != 0 || !TextUtils.isEmpty(menuText)) {
            addMenuItem(menuId, resId, menuText);
        }
        return this;
    }

    public TitleBarView setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
        return this;
    }

    private void addMenuItem(int menuId, @DrawableRes int resId, CharSequence menuText) {
        if (!couldAddMoreMenuItem()) {
            return;
        }
        if (!TextUtils.isEmpty(menuText)) {
            TextView textView = new TextView(getContext());
            textView.setText(menuText);
            textView.setBackgroundResource(resId);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);
            addViewToMenuLayout(textView);
        } else if (resId != 0) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(resId);
            imageView.setTag(menuId);
            addViewToMenuLayout(imageView);
        }
    }

    private void addViewToMenuLayout(View view) {
        if (mMenuLayout == null || view == null) {
            return;
        }
        if (mMenuLayout.getChildCount() == 1) {
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(contentMargin, 0, 0, 0);
            view.setLayoutParams(llp);
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMenuItemClickListener != null) {
                    mOnMenuItemClickListener.OnMenuItemClick((Integer) v.getTag());
                }
            }
        });
        mMenuLayout.addView(view);
    }

    /**
     * 判断是否能添加更多的menuItem, 当前限制最多能添加两个MenuItem
     *
     * @return 是否能添加新的MenuItem
     */
    private boolean couldAddMoreMenuItem() {
        return mMenuLayout != null && mMenuLayout.getChildCount() < 2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int leftWidth = mNavigationLayout.getWidth();
        int rightWidth = mMenuLayout.getWidth();
        int width = Math.max(leftWidth, rightWidth) + contentMargin;
        LayoutParams params = (LayoutParams) mCustomLayout.getLayoutParams();
        params.setMargins(width, 0, width, 0);
    }

    public static interface OnMenuItemClickListener {
        void OnMenuItemClick(int menuId);
    }
}
