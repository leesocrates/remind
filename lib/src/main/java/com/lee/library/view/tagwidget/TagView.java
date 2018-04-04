package com.lee.library.view.tagwidget;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by lee on 2018/4/3.
 */
class TagView extends android.support.v7.widget.AppCompatTextView {

    private final TagLayoutConfig mConfig;
    private final Tag mTag;

    public TagView(Context context, Tag tag, TagLayoutConfig config) {
        super(context);
        mConfig = config;
        mTag = tag;
        if (mConfig != null) {
            setPadding(mConfig.getTagViewLeftPadding(), mConfig.getTagViewTopPadding(),
                    mConfig.getTagViewRightPadding(), mConfig.getTagViewBottomPadding());
            setTextColor(mConfig.getTextColor());
            setTextSize(TypedValue.COMPLEX_UNIT_SP, mConfig.getTextSize());

        }
        if (mTag != null) {
            setText(mTag.getText());
        }
    }


}
