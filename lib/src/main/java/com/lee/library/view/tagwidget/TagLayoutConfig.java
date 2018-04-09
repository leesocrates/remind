package com.lee.library.view.tagwidget;

import android.content.Context;
import android.view.ViewGroup;

import com.lee.library.util.DensityUtil;

/**
 * 各种属性和不同显示模式的配置
 */
public class TagLayoutConfig {

    public enum ShowModel {
        CIRCLE,
        RECTANGLE,
        ROUNDED_RECTANGLE
    }

    private ShowModel showModel;

    private int width;
    private int height;

    /**
     * The tag outline border color.
     */
    private int borderColor;

    /**
     * The tag text color.
     */
    private int textColor;

    /**
     * The tag background color.
     */
    private int backgroundColor;

    /**
     * The check text color
     */
    private int checkedTextColor;

    /**
     * The checked tag background color.
     */
    private int checkedBackgroundColor;

    /**
     * The unEnabled state background color
     */
    private int unEnabledBackgroundColor;

    /**
     * The tag background color, when the tag is being pressed.
     */
    private int pressedBackgroundColor;

    /**
     * The tag outline border stroke width, default is 0.5dp.
     */
    private float borderStrokeWidth;

    /**
     * The tag text size, default is 13sp.
     */
    private float textSize;

    /**
     * The horizontal tag spacing, default is 8.0dp.
     */
    private int horizontalSpacing;

    /**
     * The vertical tag spacing, default is 4.0dp.
     */
    private int verticalSpacing;

    /**
     * The TagView left padding, default is 12.0dp.
     */
    private int tagViewLeftPadding;

    /**
     * The TagView Top padding, default is 3.0dp.
     */
    private int tagViewTopPadding;

    /**
     * The TagView Right padding, default is 12.0dp.
     */
    private int tagViewRightPadding;

    /**
     * The TagView Bottom padding, default is 3.0dp.
     */
    private int tagViewBottomPadding;

    public TagLayoutConfig(Context context) {
        width = ViewGroup.LayoutParams.WRAP_CONTENT;
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
        borderColor = 0xff333333;
        backgroundColor = 0xffff0000;
        checkedTextColor = 0xffffffff;
        checkedBackgroundColor = 0xffff0000;
        pressedBackgroundColor = 0x88ff0000;
        unEnabledBackgroundColor = 0xff999999;
        borderStrokeWidth = DensityUtil.dipToPx(context, 1);
        textColor = 0xff333333;
        textSize = 13;
        tagViewLeftPadding = DensityUtil.dipToPx(context, 12);
        tagViewTopPadding = DensityUtil.dipToPx(context, 3);
        tagViewRightPadding = DensityUtil.dipToPx(context, 12);
        tagViewBottomPadding = DensityUtil.dipToPx(context, 3);
        horizontalSpacing = DensityUtil.dipToPx(context, 8);
        verticalSpacing = DensityUtil.dipToPx(context, 4);
        showModel = ShowModel.RECTANGLE;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCheckedTextColor() {
        return checkedTextColor;
    }

    public void setCheckedTextColor(int checkedTextColor) {
        this.checkedTextColor = checkedTextColor;
    }

    public int getCheckedBackgroundColor() {
        return checkedBackgroundColor;
    }

    public void setCheckedBackgroundColor(int checkedBackgroundColor) {
        this.checkedBackgroundColor = checkedBackgroundColor;
    }

    public int getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public int getUnEnabledBackgroundColor() {
        return unEnabledBackgroundColor;
    }

    public void setUnEnabledBackgroundColor(int unEnabledBackgroundColor) {
        this.unEnabledBackgroundColor = unEnabledBackgroundColor;
    }

    public void setPressedBackgroundColor(int pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    public float getBorderStrokeWidth() {
        return borderStrokeWidth;
    }

    public void setBorderStrokeWidth(float borderStrokeWidth) {
        this.borderStrokeWidth = borderStrokeWidth;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public int getTagViewLeftPadding() {
        return tagViewLeftPadding;
    }

    public void setTagViewLeftPadding(int tagViewLeftPadding) {
        this.tagViewLeftPadding = tagViewLeftPadding;
    }

    public int getTagViewTopPadding() {
        return tagViewTopPadding;
    }

    public void setTagViewTopPadding(int tagViewTopPadding) {
        this.tagViewTopPadding = tagViewTopPadding;
    }

    public int getTagViewRightPadding() {
        return tagViewRightPadding;
    }

    public void setTagViewRightPadding(int tagViewRightPadding) {
        this.tagViewRightPadding = tagViewRightPadding;
    }

    public int getTagViewBottomPadding() {
        return tagViewBottomPadding;
    }

    public void setTagViewBottomPadding(int tagViewBottomPadding) {
        this.tagViewBottomPadding = tagViewBottomPadding;
    }

    public ShowModel getShowModel() {
        return showModel;
    }

    public void setShowModel(ShowModel showModel) {
        this.showModel = showModel;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
