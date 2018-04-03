package com.lee.library.view.tagwidget;

import android.support.annotation.DrawableRes;

/**
 * Created by lee on 2018/4/3.
 */

public class Tag {
    private String text;
    private boolean enabled;
    private boolean selected;
    @DrawableRes private int unEnabledResId;
    @DrawableRes private int unSelectedResId;
    @DrawableRes private int selectedResId;

}
