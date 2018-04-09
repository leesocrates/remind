package com.lee.library.view.tagwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.MotionEvent;

/**
 * Created by lee on 2018/4/3.
 */
class TagView extends android.support.v7.widget.AppCompatTextView {

    private final TagLayoutConfig mConfig;
    private final Tag mTag;
    /**
     * Used to detect the touch event.
     */
    private Rect mOutRect = new Rect();
    private int width;
    private int height;
    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    public TagView(Context context, Tag tag, TagLayoutConfig config) {
        super(context);
        mConfig = config;
        mTag = tag;
        if (mConfig != null) {
            setPadding(mConfig.getTagViewLeftPadding(), mConfig.getTagViewTopPadding(),
                    mConfig.getTagViewRightPadding(), mConfig.getTagViewBottomPadding());
            setTextColor(mConfig.getTextColor());
            setTextSize(TypedValue.COMPLEX_UNIT_SP, mConfig.getTextSize());
            mBorderPaint.setStrokeWidth(mConfig.getBorderStrokeWidth());
            mBorderPaint.setColor(mConfig.getBorderColor());
            mBackgroundPaint.setColor(mConfig.getBackgroundColor());
        }
        if (mTag != null) {
            setText(mTag.getText());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initColor();
        drawBackground(canvas);
        super.onDraw(canvas);
    }

    private void drawBackground(Canvas canvas) {
        if (mConfig.getShowModel() == TagLayoutConfig.ShowModel.CIRCLE) {
            if (mTag.isEnabled()) {
                if (mTag.isSelected()) {
                    canvas.drawCircle(width / 2, height / 2, width / 2 - mConfig.getBorderStrokeWidth(), mBackgroundPaint);
                } else {
                    canvas.drawCircle(width / 2, height / 2, width / 2 - mConfig.getBorderStrokeWidth(), mBorderPaint);
                }
            } else {
                canvas.drawCircle(width / 2, height / 2, width / 2 - mConfig.getBorderStrokeWidth(), mBorderPaint);
            }
        } else if (mConfig.getShowModel() == TagLayoutConfig.ShowModel.RECTANGLE) {
            if (mTag.isEnabled()) {
                if (mTag.isSelected()) {
                    canvas.drawRect(0, 0, width, height, mBackgroundPaint);
                } else {
                    canvas.drawRect(mConfig.getBorderStrokeWidth(), mConfig.getBorderStrokeWidth(),
                            width - mConfig.getBorderStrokeWidth(), height - mConfig.getBorderStrokeWidth(), mBorderPaint);
                }
            } else {
                canvas.drawRect(mConfig.getBorderStrokeWidth(), mConfig.getBorderStrokeWidth(),
                        width - mConfig.getBorderStrokeWidth(), height - mConfig.getBorderStrokeWidth(), mBorderPaint);
            }
        } else if (mConfig.getShowModel() == TagLayoutConfig.ShowModel.ROUNDED_RECTANGLE) {
            if (mTag.isEnabled()) {
                if (mTag.isSelected()) {
                    canvas.drawRoundRect(new RectF(0, 0, width, height), height / 2, height / 2, mBackgroundPaint);
                } else {
                    canvas.drawRoundRect(new RectF(mConfig.getBorderStrokeWidth(), mConfig.getBorderStrokeWidth(),
                                    width - mConfig.getBorderStrokeWidth(), height - mConfig.getBorderStrokeWidth()),
                            height / 2, height / 2, mBorderPaint);
                }
            } else {
                canvas.drawRoundRect(new RectF(mConfig.getBorderStrokeWidth(), mConfig.getBorderStrokeWidth(),
                                width - mConfig.getBorderStrokeWidth(), height - mConfig.getBorderStrokeWidth()),
                        height / 2, height / 2, mBorderPaint);
            }
        }
        if (!mTag.isEnabled()) {
            setEnabled(false);
        }
    }

    private void initColor() {
        if (mTag.isEnabled()) {
            if (mTag.isSelected()) {
                mBorderPaint.setColor(mConfig.getBorderColor());
                mBackgroundPaint.setColor(mConfig.getCheckedBackgroundColor());
                setTextColor(mConfig.getCheckedTextColor());
            } else {
                mBorderPaint.setColor(mConfig.getBorderColor());
                mBackgroundPaint.setColor(mConfig.getBackgroundColor());
                setTextColor(mConfig.getTextColor());
            }
        } else {
            mBorderPaint.setColor(mConfig.getUnEnabledBackgroundColor());
            mBackgroundPaint.setColor(mConfig.getUnEnabledBackgroundColor());
            setTextColor(mConfig.getTextColor());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getDrawingRect(mOutRect);
                setAlpha(0.6f);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mOutRect.contains((int) event.getX(), (int) event.getY())) {
                    setAlpha(1f);
                } else {
                    setAlpha(0.6f);
                }
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1f);
                break;
            case MotionEvent.ACTION_CANCEL:
                setAlpha(1f);
                break;

        }
        return super.onTouchEvent(event);
    }
}
