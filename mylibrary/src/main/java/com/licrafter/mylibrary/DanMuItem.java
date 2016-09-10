package com.licrafter.mylibrary;

import android.graphics.Bitmap;
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
    private int mTextSize;
    private int mTextColor;
    private SpannableString mContent;
    private float mCurrX;
    private float mCurrY;
    private float mFactor;
    private int mPadding = 10;

    private StaticLayout mStaticLayout;
    private Bitmap mCache;
    private int mContentWidth;
    private int mContentHeight;

    private TextPaint mTextPaint = new TextPaint();
    private static Paint mBgPaint = null;

    static {
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.BLACK);
        mBgPaint.setAlpha(150);
        mBgPaint.setAntiAlias(false);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setShadowLayer(0, 0, 0, 0);
    }

    @Override
    public void update(long deltaTime) {

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
        RectF bgRect = new RectF(-mPadding, -mPadding / 2, mContentWidth + mPadding, mContentHeight + mPadding / 2);
        canvas.drawRoundRect(bgRect, 30, 30, mBgPaint);
        mStaticLayout.draw(canvas);
        canvas.restore();
        mCurrX = mCurrX - mSpeed * mFactor;
    }


    private void measure() {
        mTextPaint.setAntiAlias(false);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mStaticLayout = new StaticLayout(mContent, mTextPaint, (int) Layout.getDesiredWidth(mContent, 0, mContent.length(), mTextPaint) + 1,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                false);
        mContentHeight = mStaticLayout.getHeight();
        mContentWidth = mStaticLayout.getWidth();
    }

    @Override
    public boolean isOut() {
        return mCurrX < 0 && Math.abs(mCurrX) > mContentWidth;
    }

    @Override
    public boolean isInCompletely() {
        return mCurrX+mContentWidth+mPadding<mContainerWidth;
    }

    @Override
    public float getScrollDistance(){
        return mContainerWidth - mCurrX;
    }

    @Override
    public int getWidth() {
        return mContentWidth;
    }

    @Override
    public int getHeight() {
        return mContentHeight;
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
    public void release() {
        mContent = null;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        measure();
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        measure();
    }

    public void setSpeedFactor(float factor) {
        mFactor = factor;
    }

    @Override
    public void setStartPoint(float startX, float startY) {
        mCurrX = startX;
        mCurrY = startY;
    }

    @Override
    public void setContent(SpannableString content) {
        mContent = content;
        measure();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        DanMuItem danMuItem;

        private Builder() {
            danMuItem = new DanMuItem();
        }

        public Builder setTextSize(int textSize) {
            danMuItem.setTextSize(textSize);
            return this;
        }

        public Builder setTextColor(int textColor) {
            danMuItem.setTextColor(textColor);
            return this;
        }

        public Builder setSpeedFactor(float factor) {
            danMuItem.setSpeedFactor(factor);
            return this;
        }

        public Builder setContent(SpannableString content) {
            danMuItem.setContent(content);
            return this;
        }

        public DanMuItem build() {
            return danMuItem;
        }
    }
}
