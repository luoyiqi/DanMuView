package com.licrafter.mylibrary;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author: shell
 * date 16/9/3 下午4:26
 **/
public class DanMuScreen extends Screen {

    private static final int MAX_CHANNEL = 7;

    private int mMaxChannel;
    private float mScreenWidth;
    private float mScreenHeight;
    private HandlerThread mThread;
    private DanMuHandler mHandler;

    private HashMap<Integer, CopyOnWriteArrayList<IDanMuItem>> mDanmuMap;

    public static DanMuScreen create() {
        return new DanMuScreen();
    }

    @Override
    public void init(float screenWidth, float screenHeight) {
        mHandler = new DanMuHandler(getLooper());
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        android.util.Log.d("ljx", "screenWidth = " + mScreenWidth + "  height = " + mScreenHeight);
        mMaxChannel = MAX_CHANNEL;
        mDanmuMap = new HashMap<>();
        for (int i = 0; i < mMaxChannel; i++) {
            mDanmuMap.put(i, new CopyOnWriteArrayList<IDanMuItem>());
        }
    }

    @Override
    public void draw(float deltaTime, Canvas canvas) {

        for (int i = 0; i < mMaxChannel; i++) {
            for (IDanMuItem danMuItem : mDanmuMap.get(i)) {
                if (!danMuItem.isOut()) {
                    danMuItem.draw(canvas);
                } else {
                    mProxy.releaseResource(danMuItem);
                    mDanmuMap.get(i).remove(danMuItem);
                }
            }
        }
    }


    @Override
    public void addDanMu(IDanMuItem danmu) {
        mProxy.prepareDraw(danmu);
        mHandler.obtainMessage(DanMuHandler.ADD,danmu).sendToTarget();
    }

    @Override
    public void setProxy(Proxy proxy) {
        mProxy = proxy;
    }

    public void buildCache(IDanMuItem danMuItem) {
        danMuItem.buildCache(new DanMuCache(danMuItem.getTextSize(), danMuItem.getTextColor()).buildCache(danMuItem));
    }

    private Looper getLooper(){
        if (mThread!=null){
            mThread.quit();
            mThread = null;
        }
        mThread = new HandlerThread("DM ADD THREAD",android.os.Process.THREAD_PRIORITY_DEFAULT);
        mThread.start();
        return mThread.getLooper();
    }

    public class DanMuHandler extends Handler{

        public static final int QUITE = 0;
        public static final int ADD = 1;
        public static final int UPDATE = 2;

        public DanMuHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;
            switch (what){
                case QUITE:
                    break;
                case ADD:
                    IDanMuItem danmu = (IDanMuItem)msg.obj;
                    int lastedChannel = 0;
                    buildCache(danmu);
                    for (int i = 0; i < mMaxChannel; i++) {
                        int count = mDanmuMap.get(i).size();
                        if (count == 0) {
                            lastedChannel = i;
                            break;
                        } else if (mDanmuMap.get(i).get(count - 1).isInCompletely()) {
                            lastedChannel = i;
                            break;
                        } else if (mDanmuMap.get(i).get(count - 1).getScrollDistance() > mDanmuMap.get(lastedChannel).get(mDanmuMap.get(lastedChannel).size() - 1).getScrollDistance()) {
                            lastedChannel = i;
                        }
                    }
                    danmu.setStartPoint(mScreenWidth + 100, (danmu.getHeight() + danmu.getPadding()) * lastedChannel + (lastedChannel + 1) * 10);
                    mDanmuMap.get(lastedChannel).add(danmu);
                    break;
                case UPDATE:
                    break;
            }
        }
    }
}
