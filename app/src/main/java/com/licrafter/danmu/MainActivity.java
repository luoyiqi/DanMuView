package com.licrafter.danmu;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;

import com.licrafter.mylibrary.DanMuItem;
import com.licrafter.mylibrary.DanMuScreen;
import com.licrafter.mylibrary.IDanMuItem;
import com.licrafter.mylibrary.Proxy;
import com.licrafter.mylibrary.RenderView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSIONS = 0x0001;


    RenderView mDanmuView;
    DanMuScreen mScreen;
    SpannableString spannableString;

    String[] urls = new String[5];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDanmuView = (RenderView) findViewById(R.id.danmuLayout);

        final Drawable drawable = getResources().getDrawable(R.mipmap.em);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        CenteredImageSpan imageSpan = new CenteredImageSpan(this, R.mipmap.em);
        spannableString = new SpannableString(" : 恭喜手机号159***789中奖");
        spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        urls[0] = "http://i0.hdslb.com/bfs/archive/96e9cf19ad4371fca8f3991ed600e58c65c4e5dd.jpg_320x200.jpg";
        urls[1] = "http://i0.hdslb.com/bfs/archive/a73c0c63fac4243119fd33c267c2ee81dc894e73.jpg_320x200.jpg";
        urls[2] = "http://i0.hdslb.com/bfs/archive/9fbccd916e32589914c513416479b1ee5f20a4d6.jpg_320x200.jpg";
        urls[3] = "http://i0.hdslb.com/bfs/archive/772f7ca948e044aa5bc45508d8565e8aa5ad0c5f.jpg_320x200.jpg";
        urls[4] = "http://i0.hdslb.com/bfs/archive/21dc6215f5eb23dc3b4143f12560e67bdd7f71d9.jpg_320x200.jpg";

        mScreen = DanMuScreen.create();
        mDanmuView.setScreen(mScreen);
        mScreen.setProxy(new Proxy() {
            private Drawable mDrawable;
            int num = 0;

            @Override
            public void prepareDraw(final IDanMuItem danMuItem) {

                com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                        .oval(true)
                        .build();
                RequestCreator requestCreator = Picasso.with(getApplicationContext())
                        .load(urls[num%5])
                        .transform(transformation);
                num++;
                requestCreator.into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        BitmapDrawable drawable1 = new BitmapDrawable(getResources(),bitmap);
                        CenteredImageSpan imageSpan = new CenteredImageSpan(drawable1);
                        final SpannableString spannableString = new SpannableString(" : 恭喜手机号159***789中奖");
                        spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        danMuItem.setContent(spannableString);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            @Override
            public void releaseResource(IDanMuItem danMuItem) {
                danMuItem.release();
            }

        });
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void danmu(View view){
        for (int i =0;i<1;i++){
            mScreen.addDanMu(DanMuItem.builder().setContent(spannableString).setSpeedFactor(1.8f)
                    .setTextColor(Color.WHITE).setTextSize(dip2px(MainActivity.this,13)).build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStoragePermission();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkStoragePermission() {
        if (PermissionUtil.checkPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mDanmuView.resume();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    PermissionUtil.showPermissionsDescDialog(this, "开启文件访问权限，以正常使用设置头像，网络缓存等功能", false);
                } else {
                    // Permission GRANTED
                    mDanmuView.resume();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDanmuView.onPause();
    }
}
