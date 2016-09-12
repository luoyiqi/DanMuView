package com.licrafter.mylibrary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * author: shell
 * date 16/9/10 下午4:57
 **/
public class DanMuCache {
    public Canvas canvas;
    public Bitmap bitmap;
    private StaticLayout mStaticLayout;
    private TextPaint mTextPaint;

    private int width;
    private int height;

    private static Paint mBgPaint;

    static {
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.BLACK);
        mBgPaint.setAlpha(150);
        mBgPaint.setAntiAlias(false);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setShadowLayer(0, 0, 0, 0);
    }

    public DanMuCache(TextPaint textPaint) {
        mTextPaint = textPaint;
    }

    public synchronized void recycle() {
        width = height = 0;
        if (bitmap != null) {
            bitmap.recycle();
        }
        bitmap = null;
        canvas = null;
    }

    public DanMuCache buildCache(IDanMuItem danMuItem) {
        mStaticLayout = new StaticLayout(danMuItem.getContent(), mTextPaint, (int) Layout.getDesiredWidth(danMuItem.getContent(), 0, danMuItem.getContent().length(), mTextPaint) + 1,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                false);
        int padding = (int) danMuItem.getPadding();
        width = mStaticLayout.getWidth();
        height = mStaticLayout.getHeight();
        if (canvas != null) {
            bitmap.eraseColor(Color.TRANSPARENT);
            canvas.setBitmap(bitmap);
        } else {
            bitmap = Bitmap.createBitmap(width + 2 * padding, height + padding, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
        }

        RectF bgRect = new RectF(0, 0, width + padding * 2, height + padding);
        canvas.drawRoundRect(bgRect, 30, 30, mBgPaint);
        canvas.save();
        canvas.translate(padding, padding / 2);
        mStaticLayout.draw(canvas);
        canvas.restore();
        mStaticLayout = null;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
}
