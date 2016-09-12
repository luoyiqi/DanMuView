package com.licrafter.mylibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author: shell
 * date 16/9/3 下午4:03
 **/
public class RenderView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    Thread mRenderThread = null;
    SurfaceHolder mHolder;
    Canvas mCanvas;
    protected Screen mScreen = null;
    AtomicBoolean mRunning = new AtomicBoolean();
    AtomicBoolean mRender = new AtomicBoolean();

    public RenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderOnTop(true);
        setWillNotCacheDrawing(true);
        setDrawingCacheEnabled(false);
        setWillNotDraw(true);
        android.util.Log.d("ljx",isHardwareAccelerated()+"");

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mRunning.set(false);
        mRender.set(false);
    }

    public void resume() {
        if (mRenderThread == null) {
            mRunning.set(true);
            mRenderThread = new Thread(this);
            mRenderThread.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mScreen==null){
            throw new RuntimeException("screen is not set");
        }
        mScreen.init(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public void run() {
        while (!mRender.get()) {
            continue;
        }
        if (mScreen==null){
            throw new RuntimeException("screen is not set");
        }
        //surface hase been initialied
        while (mRunning.get()) {
            long startTime = android.os.SystemClock.elapsedRealtime();
            if (!mRender.get()) {
                continue;
            }
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mScreen.draw(startTime,mCanvas);
            mHolder.unlockCanvasAndPost(mCanvas);
            float deltaTime = (android.os.SystemClock.elapsedRealtime() - startTime);
        }
    }

    public void onPause(){
        mRunning.set(false);
        if (mRenderThread!=null){
            boolean retry = true;
            while (retry){
                try {
                    mRenderThread.join();
                    mRenderThread=null;
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setScreen(Screen screen){
        mScreen = screen;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mRender.set(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mRender.compareAndSet(false, true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mRender.set(false);
    }
}
