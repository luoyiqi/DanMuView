package com.licrafter.mylibrary;

import android.graphics.Canvas;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author: shell
 * date 16/9/3 下午4:05
 **/
public abstract class Screen {

    protected AtomicBoolean mInitialized;

    protected Proxy mProxy;

    public Screen() {
        mInitialized = new AtomicBoolean(false);
    }

    public abstract void init(float screenWidth,float screenHeight);

    public abstract void draw(float deltaTime,Canvas canvas);

    public abstract void addDanMu(IDanMuItem danmu);

    public abstract void setProxy(Proxy proxy);
}
