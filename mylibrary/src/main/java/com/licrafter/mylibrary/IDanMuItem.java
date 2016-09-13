package com.licrafter.mylibrary;

import android.graphics.Canvas;
import android.text.SpannableString;

/**
 * author: shell
 * date 16/9/3 下午5:13
 **/
public interface IDanMuItem {

    public void setStartPoint(float startX, float startY);

    public void buildCache(DanMuCache cache);

    public void update(SpannableString spannableString);

    public void draw(Canvas canvas);

    public boolean isOut();

    public boolean isInCompletely();

    public float getScrollDistance();

    public float getWidth();

    public float getHeight();

    public float getCurrX();

    public float getCurrY();

    public SpannableString getContent();

    public float getPadding();

    public void setPadding(int padding);

    public void setTextSize(int size);

    public int getTextSize();

    public void setTextColor(int color);

    public int getTextColor();

    public float getFactor();

    public void setFactor(float factor);

    public void release();
}
