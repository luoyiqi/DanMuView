package com.licrafter.mylibrary;

import android.graphics.Canvas;
import android.text.SpannableString;

/**
 * author: shell
 * date 16/9/3 下午5:13
 **/
public interface IDanMuItem {

    public void setStartPoint(float startX, float startY);

    public void setContent(SpannableString content);

    public void update(long deltaTime);

    public void draw(Canvas canvas);

    public boolean isOut();

    public int getWidth();

    public int getHeight();

    public float getCurrX();

    public float getCurrY();

    public float getPadding();

    public void release();
}
