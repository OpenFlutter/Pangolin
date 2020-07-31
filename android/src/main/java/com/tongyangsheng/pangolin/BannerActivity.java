package com.tongyangsheng.pangolin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

import java.util.List;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;

public class BannerActivity extends FlutterActivity {

    private TTAdNative mTTAdNative;

    //广告所需参数
    public String mCodeId;
    public Boolean supportDeepLink = true;
    public float expressViewWidth;
    public float expressViewHeight;

    private FrameLayout mExpressContainer;
    private TTNativeExpressAd mTTAd;
    private long startTime = 0;


    static MethodChannel _channel;
    public Context context;
    public Activity activity;

    public void init(){
        TTAdManager mTTAdManager = TTAdManagerHolder.get();
        mTTAdNative = mTTAdManager.createAdNative(context.getApplicationContext());
        configAd();
    }

    private void configAd(){
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mCodeId) //广告位id
                .setSupportDeepLink(supportDeepLink)
                .setAdCount(3) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize((int) expressViewWidth, (int) expressViewHeight)
                .build();

        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);
//                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });
    }

//    private void bindAdListener(TTNativeExpressAd ad) {
//        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
//            @Override
//            public void onAdClicked(View view, int type) {
//
//            }
//
//            @Override
//            public void onAdShow(View view, int type) {
//
//            }
//
//            @Override
//            public void onRenderFail(View view, String msg, int code) {
//                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
//            }
//
//            @Override
//            public void onRenderSuccess(View view, float width, float height) {
//                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
//                //返回view的宽高 单位 dp
//                mExpressContainer.removeAllViews();
//                mExpressContainer.addView(view);
//            }
//        });
//        //dislike设置
//        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//            return;
//        }
//        ad.setDownloadListener(new TTAppDownloadListener() {
//            @Override
//            public void onIdle() {
//                TToast.show(BannerExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!mHasShowDownloadActive) {
//                    mHasShowDownloadActive = true;
//                    TToast.show(BannerExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
//                }
//            }
//
//            @Override
//            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onInstalled(String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
//            }
//        });
//    }
}
