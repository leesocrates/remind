package com.lee.library.view.tagwidget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by lee on 2018/4/3.
 * 基于github开源项目 AndroidTagGroup修改
 */

public class TagLayoutView extends LinearLayout {

    private Context mContext;
    private TagLayoutConfig mConfig;
    private List<Tag> mTagList;

    public TagLayoutView(Context context) {
        super(context);
        mContext = context;
    }

    public TagLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TagLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public TagLayoutView init(TagLayoutConfig config, OnTagClickListener onTagClickListener) {
        mConfig = config;
        if (mConfig == null) {
            mConfig = new TagLayoutConfig(mContext);
        }
        mOnTagClickListener = onTagClickListener;
        return this;
    }

    public void showTags(List<Tag> tagList) {
        if (tagList == null || tagList.size() == 0) {
            return;
        }
        if (mConfig == null) {
            throw new IllegalStateException("must invoke init() before this invoke method");
        }
        mTagList = tagList;
        updateTagShow();
    }

    private void updateTagShow() {
        removeAllViews();
        if (mTagList != null) {
            for (Tag tag : mTagList) {
                addTag(tag);
            }
        }
    }

    private void addTag(Tag tag) {
        final TagView tagView = new TagView(mContext, tag, mConfig);
        tagView.setTag(tag);
        tagView.setOnClickListener(mChangeStateTagClickListener);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(mConfig.getWidth(), mConfig.getHeight());
        addView(tagView, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int row = 0; // The row counter.
        int rowWidth = 0; // Calc the current row width.
        int rowMaxHeight = 0; // Calc the max tag height, in current row.

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                rowWidth += childWidth;
                if (rowWidth > widthSize) { // Next line.
                    rowWidth = childWidth; // The next row width.
                    height += rowMaxHeight + mConfig.getVerticalSpacing();
                    rowMaxHeight = childHeight; // The next row max height.
                    row++;
                } else { // This line.
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += mConfig.getHorizontalSpacing();
            }
        }
        // Account for the last row height.
        height += rowMaxHeight;

        // Account for the padding too.
        height += getPaddingTop() + getPaddingBottom();

        // If the tags grouped in one row, set the width to wrap the tags.
        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {// If the tags grouped exceed one line, set the width to match the parent.
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        int childTop = parentTop;

        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (childLeft + width > parentRight) { // Next line
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + mConfig.getVerticalSpacing();
                    rowMaxHeight = height;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);

                childLeft += width + mConfig.getHorizontalSpacing();
            }
        }
    }


    private ChangeStateTagClickListener mChangeStateTagClickListener = new ChangeStateTagClickListener();

    private class ChangeStateTagClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Tag tag = (Tag) v.getTag();
            if (mOnTagClickListener != null) {
                mOnTagClickListener.onTagClick(v);
            }
            if (tag.isEnabled()) {
                if (tag.isSelected()) {
                    tag.setSelected(false);
                } else {
                    tag.setSelected(true);
                }
                v.invalidate();
            }
        }
    }


    /**
     * Listener used to dispatch tag click event.
     */
    private OnTagClickListener mOnTagClickListener;

    /**
     * Interface definition for a callback to be invoked when a tag is clicked.
     */
    public interface OnTagClickListener {
        /**
         * Called when a tag has been clicked.
         */
        void onTagClick(View v);
    }

}
