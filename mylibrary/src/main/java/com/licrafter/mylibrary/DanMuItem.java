package com.licrafter.mylibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * author: shell
 * date 16/9/3 下午5:13
 **/
public class DanMuItem implements IDanMuItem {

    private int mSpeed = 3;
    private int mContainerWidth;
    private int mContainerHeight;
    private float mCurrX;
    private float mCurrY;
    private SpannableString mContent;
    private float mFactor;
    private int mTextSize;
    private int mTextColor;
    private int mPadding = 10;
    private DanMuCache mCache;

    private static Paint paint;

    static {
        paint = new Paint();
        paint.setAntiAlias(false);
    }


    public DanMuItem(SpannableString content) {
        mContent = content;
    }

    @Override
    public void update(SpannableString content) {
        mContent = content;
        mCache.buildCache(this);
    }

    @Override
    public void draw(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        if (canvasWidth != this.mContainerWidth || canvasHeight != this.mContainerHeight) {//phone rotated !
            this.mContainerWidth = canvasWidth;
            this.mContainerHeight = canvasHeight;
        }
        canvas.save();
        canvas.translate(mCurrX, mCurrY);
        canvas.drawBitmap(mCache.bitmap, 0, 0, paint);
        canvas.restore();
        mCurrX = mCurrX - mSpeed * mFactor;
    }

    @Override
    public void buildCache(DanMuCache cache) {
        mCache = cache;
    }

    @Override
    public boolean isOut() {
        return mCurrX < 0 && Math.abs(mCurrX) > mCache.getWidth() + mPadding * 2;
    }

    @Override
    public boolean isInCompletely() {
        return mCurrX + mCache.getWidth() + mPadding < mContainerWidth;
    }

    @Override
    public float getScrollDistance() {
        return mContainerWidth - mCurrX;
    }

    @Override
    public float getWidth() {
        return mCache.getWidth();
    }

    @Override
    public float getHeight() {
        return mCache.getHeight();
    }

    @Override
    public float getCurrX() {
        return mCurrX;
    }

    @Override
    public float getCurrY() {
        return mCurrY;
    }

    @Override
    public float getPadding() {
        return mPadding;
    }

    @Override
    public void setPadding(int padding) {
        mPadding = padding;
    }

    @Override
    public void setTextSize(int size) {
        mTextSize = size;
    }

    @Override
    public int getTextSize() {
        return mTextSize;
    }

    @Override
    public void setTextColor(int color) {
        mTextColor = color;
    }

    @Override
    public int getTextColor() {
        return mTextColor;
    }

    @Override
    public float getFactor() {
        return mFactor;
    }

    @Override
    public void setFactor(float factor) {
        mFactor = factor;
    }

    @Override
    public SpannableString getContent() {
        return mContent;
    }

    @Override
    public void release() {
        mContent = null;
        mCache.recycle();
        mCache = null;
    }


    @Override
    public void setStartPoint(float startX, float startY) {
        mCurrX = startX;
        mCurrY = startY;
    }
}
