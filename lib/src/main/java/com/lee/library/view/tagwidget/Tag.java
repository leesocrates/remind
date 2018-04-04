package com.lee.library.view.tagwidget;

import android.support.annotation.DrawableRes;

/**
 * Created by lee on 2018/4/3.
 */

public class Tag {
    private String text;
    private boolean enabled;
    private boolean selected;
    @DrawableRes private int unEnabledBgResId;
    @DrawableRes private int unSelectedBgResId;
    @DrawableRes private int selectedResBgId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getUnEnabledBgResId() {
        return unEnabledBgResId;
    }

    public void setUnEnabledBgResId(int unEnabledBgResId) {
        this.unEnabledBgResId = unEnabledBgResId;
    }

    public int getUnSelectedBgResId() {
        return unSelectedBgResId;
    }

    public void setUnSelectedBgResId(int unSelectedBgResId) {
        this.unSelectedBgResId = unSelectedBgResId;
    }

    public int getSelectedResBgId() {
        return selectedResBgId;
    }

    public void setSelectedResBgId(int selectedResBgId) {
        this.selectedResBgId = selectedResBgId;
    }
}
