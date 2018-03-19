package com.lee.socrates.mvvmdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by lee on 2018/3/19.
 */

public class MultiLayerImageView extends android.support.v7.widget.AppCompatImageView {

    protected Canvas mCanvas = null;

    protected Bitmap bitmap;

    private int mWidth = 150;
    private int mHeight = 150;

    public MultiLayerImageView(Context context) {
        super(context);
    }

    public MultiLayerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiLayerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        setupCanvas();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackground(null);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

    }

    private void setupCanvas() {
        if (mCanvas == null) {
            mCanvas = new Canvas();
        }

        if (bitmap != null)
            bitmap.recycle();

        try {
            bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            bitmap = null;
            return;
        }

        mCanvas.setBitmap(bitmap);
    }

    public void addLayer(byte[] bytes) {
        setupCanvas();
        Bitmap bitmap = byteArrayToBitmap(bytes);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        mCanvas.drawBitmap(bitmap, rect, rect, paint);
        invalidate();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseMemory();
    }

    public void releaseMemory() {
        recycleBitmap(bitmap);
    }

    /**
     * 回收一个或若干个Bitmap
     */
    public void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null || bitmaps.length == 0) {
            return;
        }
        for (int i = 0; i < bitmaps.length; i++) {
            if (bitmaps[i] != null && !bitmaps[i].isRecycled()) {
                bitmaps[i].recycle();
            }
        }
    }

    public byte[] drawableToByteArray(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        return bitmapToBytes(bitmap);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, mWidth, mHeight);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public byte[] bitmapToBytes(Bitmap bmp) {
        int bytes = bmp.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);
        return buf.array();
    }

    public Drawable byteArrayToDrawable(byte[] bytes) {
        Bitmap bitmap = byteArrayToBitmap(modifyByteArray(bytes));
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public Bitmap byteArrayToBitmap(byte[] bytes) {
        Bitmap newBmp = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        newBmp.copyPixelsFromBuffer(ByteBuffer.wrap(bytes).position(0));
        return newBmp;
    }

    public byte[] modifyByteArray(byte[] bytes){
        if(bytes.length>7500*4){
            for (int i=0; i<7500; i++){
                bytes[i*4+3]=0;
            }
        }
        return bytes;
    }
}
