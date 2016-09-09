package com.licrafter.mylibrary;

import android.graphics.Canvas;
import android.os.SystemClock;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author: shell
 * date 16/9/3 下午4:26
 **/
public class DanMuScreen extends Screen {

    private static final int MAX_CHANNEL = 7;

    private CopyOnWriteArrayList<IDanMuItem> mDanMus;
    private long[] mChannelTime;
    private int[] mChannelY;
    private int mMaxChannel;
    private float mStartYOffset = 0.05f;
    private float mScreenWidth;
    private float mScreenHeight;


    public static DanMuScreen create() {
        return new DanMuScreen();
    }

    @Override
    public void init(float screenWidth,float screenHeight) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        android.util.Log.d("ljx","screenWidth = "+mScreenWidth+"  height = "+mScreenHeight);
        mMaxChannel = MAX_CHANNEL;
        mChannelTime = new long[mMaxChannel];
        mChannelY = new int[mMaxChannel];
        mDanMus = new CopyOnWriteArrayList<>();
    }

    @Override
    public void draw(float deltaTime, Canvas canvas) {
        for (IDanMuItem danMuItem : mDanMus){
            if (!danMuItem.isOut()) {
                danMuItem.draw(canvas);
            } else {
                mProxy.releaseResource(danMuItem);
                mDanMus.remove(danMuItem);
            }
        }
    }


    @Override
    public void addDanMu(IDanMuItem danmu) {
        int lastedChannel = 0;
        mProxy.prepareDraw(danmu);
        for (int i=0;i<mChannelTime.length;i++){
            if (mChannelTime[i]==0){
                lastedChannel = i;
                break;
            }else if (mChannelTime[i]<=mChannelTime[lastedChannel]){
               lastedChannel = i;
            }
        }
        mChannelTime[lastedChannel] = SystemClock.uptimeMillis();
        danmu.setStartPoint(mScreenWidth+100,(danmu.getHeight()+danmu.getPadding())*lastedChannel+(lastedChannel+1)*10);
        android.util.Log.d("ljx","width = "+danmu.getWidth()+" channel = "+lastedChannel+" y ="+danmu.getHeight()+" y = "+danmu.getCurrY());
        boolean added = false;
        synchronized (mDanMus) {
            added = mDanMus.add(danmu);
            android.util.Log.d("ljx","added = "+added);
        }
    }

    @Override
    public void setProxy(Proxy proxy) {
        mProxy = proxy;
    }


}
