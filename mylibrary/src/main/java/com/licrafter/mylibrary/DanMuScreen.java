package com.licrafter.mylibrary;

import android.graphics.Canvas;
import android.os.SystemClock;

import java.util.HashMap;
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

    private HashMap<Integer,CopyOnWriteArrayList<IDanMuItem>> mDanmuMap;


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
        mDanmuMap = new HashMap<>();
        for (int i=0;i<mMaxChannel;i++){
            mDanmuMap.put(i,new CopyOnWriteArrayList<IDanMuItem>());
        }
    }

    @Override
    public void draw(float deltaTime, Canvas canvas) {

        for (int i=0;i<mMaxChannel;i++){
            for (IDanMuItem danMuItem:mDanmuMap.get(i)){
                if (!danMuItem.isOut()){
                    danMuItem.draw(canvas);
                }else {
                    mProxy.releaseResource(danMuItem);
                    mDanmuMap.get(i).remove(danMuItem);
                }
            }
        }
    }


    @Override
    public void addDanMu(IDanMuItem danmu) {
        int lastedChannel = 0;
        mProxy.prepareDraw(danmu);

        for (int i=0;i<mMaxChannel;i++){
            int count = mDanmuMap.get(i).size();
            if (count==0){
                lastedChannel = i;
                break;
            }else if (mDanmuMap.get(i).get(count-1).isInCompletely()){
                lastedChannel = i;
                break;
            }else if (mDanmuMap.get(i).get(count-1).getScrollDistance()>mDanmuMap.get(lastedChannel).get(mDanmuMap.get(lastedChannel).size()-1).getScrollDistance()){
                lastedChannel = i;
            }
        }
        danmu.setStartPoint(mScreenWidth+100,(danmu.getHeight()+danmu.getPadding())*lastedChannel+(lastedChannel+1)*10);
        mDanmuMap.get(lastedChannel).add(danmu);
    }

    @Override
    public void setProxy(Proxy proxy) {
        mProxy = proxy;
    }


}
