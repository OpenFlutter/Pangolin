package com.tongyangsheng.pangolin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;


/** PangolinPlugin */
public class PangolinPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

  private MethodChannel methodChannel;
  private Context applicationContext;
  private Activity activity;

  //bannerAd parameter
  private TTAdNative mTTAdNative;
  private Context mContext;
  private FrameLayout mExpressContainer;
  private TTNativeExpressAd mTTAd;
  private long startTime = 0;

  public static void registerWith(Registrar registrar) {
    final PangolinPlugin instance = new PangolinPlugin();
    instance.onAttachedToEngine(registrar.context(),registrar.messenger(),registrar.activity());
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    onAttachedToEngine(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
  }

  private void onAttachedToEngine(Context applicationContext , BinaryMessenger messenger) {
    this.applicationContext = applicationContext;
    methodChannel = new MethodChannel(messenger, "com.tongyangsheng.pangolin");
    methodChannel.setMethodCallHandler(this);
  }

  private void onAttachedToEngine(Context applicationContext , BinaryMessenger messenger, Activity activity) {
    this.applicationContext = applicationContext;
    methodChannel = new MethodChannel(messenger, "com.tongyangsheng.pangolin");
    methodChannel.setMethodCallHandler(this);
    this.activity = activity;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("register")) {
      String appId = call.argument("appId");
      Boolean useTextureView = call.argument("useTextureView");
      String appName = call.argument("appName");
      Boolean allowShowNotify = call.argument("allowShowNotify");
      Boolean allowShowPageWhenScreenLock = call.argument("allowShowPageWhenScreenLock");
      Boolean debug = call.argument("debug");
      Boolean supportMultiProcess = call.argument("supportMultiProcess");
      List<Integer> directDownloadNetworkType = call.argument("directDownloadNetworkType");
      if(useTextureView == null)
      {
        useTextureView = false;
      }
      if (allowShowNotify == null)
      {
        allowShowNotify = true;
      }
      if (allowShowPageWhenScreenLock == null)
      {
        allowShowPageWhenScreenLock = true;
      }
      if (debug == null)
      {
        debug = true;
      }
      if (supportMultiProcess == null)
      {
        supportMultiProcess = false;
      }
      if (appId == null || appId.trim().isEmpty())
      {
        result.error("500","appId can't be null",null);
      }
      else {
        if (appName == null || appName.trim().isEmpty()) {
          result.error("600", "appName can't be null", null);
        } else {
          TTAdManagerHolder.init(applicationContext, appId, useTextureView, appName, allowShowNotify, allowShowPageWhenScreenLock, debug, supportMultiProcess,directDownloadNetworkType);
          result.success(true);
        }
      }
    }
    else if(call.method.equals("loadSplashAd"))
    {
      String mCodeId = call.argument("mCodeId");
      Boolean deBug = call.argument("debug");
      Intent intent = new Intent();
      intent.setClass(activity,SplashActivity.class);
      intent.putExtra("mCodeId",mCodeId);
      intent.putExtra("debug",deBug);
      activity.startActivity(intent);
    }
    else if (call.method.equals("loadRewardAd")) {
      Boolean isHorizontal = call.argument("isHorizontal");
      String mCodeId = call.argument("mCodeId");
      Boolean debug = call.argument("debug");
      Boolean supportDeepLink = call.argument("supportDeepLink");
      String rewardName = call.argument("rewardName");
      int rewardAmount = (int) call.argument("rewardAmount");
      Boolean isExpress = call.argument("isExpress");

      double expressViewAcceptedSizeH;
      if (call.argument("expressViewAcceptedSizeH") == null) {
        expressViewAcceptedSizeH = 500;
      } else {
        expressViewAcceptedSizeH = call.argument("expressViewAcceptedSizeH");
      }
      double expressViewAcceptedSizeW;
      if (call.argument("expressViewAcceptedSizeW") == null) {
        expressViewAcceptedSizeW = 500;
      } else {
        expressViewAcceptedSizeW = call.argument("expressViewAcceptedSizeW");
      }

      String userID = call.argument("userID");
      String mediaExtra;
      if (call.argument("mediaExtra") == null) {
        mediaExtra = "media_extra";
      } else {
        mediaExtra = call.argument("mediaExtra");
      }

      RewardVideo rewardVideo = new RewardVideo();
      RewardVideo._channel = methodChannel;
      rewardVideo.activity = activity;
      rewardVideo.context = applicationContext;
      if (isHorizontal) {
        rewardVideo.mHorizontalCodeId = mCodeId;
      } else {
        rewardVideo.mVerticalCodeId = mCodeId;
      }

      if (debug != null) {
        rewardVideo.debug = debug;
      } else {
        rewardVideo.debug = false;
      }

      if (isExpress != null)
      {
        rewardVideo.mIsExpress = isExpress;
      }
      else
      {
        rewardVideo.mIsExpress = false;
      }

      rewardVideo.supportDeepLink = supportDeepLink;
      rewardVideo.expressViewAcceptedSizeH = expressViewAcceptedSizeH;
      rewardVideo.expressViewAcceptedSizeW = expressViewAcceptedSizeW;
      rewardVideo.rewardName = rewardName;
      rewardVideo.rewardAmount = rewardAmount;
      rewardVideo.userID = userID;
      rewardVideo.mediaExtra = mediaExtra;
      rewardVideo.init();
    }
    else if (call.method.equals("loadBannerAd"))
    {
      Log.d("banner广告","接入Android");
      String mCodeId = call.argument("mCodeId");
      Boolean supportDeepLink = call.argument("supportDeepLink");
      Boolean isCarousel = call.argument("isCarousel");
      int interval = 0;
      float expressViewWidth = 0;
      float expressViewHeight = 0;
      int topMargin = 0;
      if (call.argument("expressViewWidth") != null)
      {
        double expressViewWidthDouble = call.argument("expressViewWidth");
        expressViewWidth = (float)expressViewWidthDouble;
      }

      if (call.argument("expressViewHeight") != null)
      {
        double expressViewHeightDouble = call.argument("expressViewHeight");
        expressViewHeight = (float)expressViewHeightDouble;
      }
      if (call.argument("interval") != null && isCarousel)
      {
        interval = call.argument("interval");
      }
      if(call.argument("topMargin") != null)
      {
        topMargin = call.argument("topMargin");
      }

      Log.d("banner广告",mCodeId);
      Log.d("banner广告",supportDeepLink.toString());
      Log.d("banner广告",Float.toString(expressViewHeight));


      mContext = this.applicationContext;

      // 获取根视图
      ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
      View view = View.inflate(activity, R.layout.activity_native_express_banner,null);
      mExpressContainer = (FrameLayout) view.findViewById(R.id.express_container);
      if(mExpressContainer.getParent() != null) {
        ((ViewGroup)mExpressContainer.getParent()).removeView(mExpressContainer);
      }

      // 设置banner 广告参数
      RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) mExpressContainer.getLayoutParams();
      params.height= (int) expressViewHeight * 2;
      params.width = (int) expressViewWidth * 2;
      // 到顶部距离
      params.topMargin = topMargin;
      mExpressContainer.setLayoutParams(params);
      rootView.addView(mExpressContainer);
      initTTSDKConfig();
      this.loadExpressBannerAd(mCodeId,Math.round(expressViewWidth),Math.round(expressViewHeight), interval);
    }
    else if (call.method.equals("loadInterstitialAd"))
    {
      String mCodeId = call.argument("mCodeId");
      double expressViewWidth = 0;
      double expressViewHeight = 0;
      if(call.argument("expressViewWidth") != null)
      {
        expressViewWidth = call.argument("expressViewWidth");
      }
      if (call.argument("expressViewHeight") != null)
      {
        expressViewHeight = call.argument("expressViewHeight");
      }
      initTTSDKConfig();
      this.loadExpressInterstitialAd(mCodeId,(int) expressViewWidth,(int) expressViewHeight);
    }
    else
    {
      result.notImplemented();
    }
  }


  private void initTTSDKConfig() {
    //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
    mTTAdNative = TTAdManagerHolder.get().createAdNative(activity);
    //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
    TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
  }

  // banner广告 加载
  private void loadExpressBannerAd(String codeId, int expressViewWidth, int expressViewHeight, final int interval) {
    mExpressContainer.removeAllViews();
    //step4:创建广告请求参数AdSlot,具体参数含义参考文档
    AdSlot adSlot = new AdSlot.Builder()
            .setCodeId(codeId) //广告位id
            .setSupportDeepLink(true)
            .setAdCount(1) //请求广告数量为1到3条
            .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
            .build();
    //step5:请求广告，对请求回调的广告作渲染处理
    mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
      @Override
      public void onError(int code, String message) {
//        TToast.show(activity, "load error : " + code + ", " + message);
        mExpressContainer.removeAllViews();
      }

      @Override
      public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
        if (ads == null || ads.size() == 0) {
          return;
        }
        //获取哪一条广告
        mTTAd = ads.get(0);
        //设置轮播间隔 ms,不调用则不进行轮播展示
        mTTAd.setSlideIntervalTime(interval * 1000);
        bindBannerAdListener(mTTAd);
        startTime = System.currentTimeMillis();
        // 渲染广告
        mTTAd.render();
      }
    });
  }

  // banner广告 监听
  private void bindBannerAdListener(TTNativeExpressAd ad) {
    ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
      @Override
      public void onAdClicked(View view, int type) {
//        TToast.show(mContext, "广告被点击");
      }

      @Override
      public void onAdShow(View view, int type) {
//        TToast.show(mContext, "广告展示");
      }

      @Override
      public void onRenderFail(View view, String msg, int code) {
        Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
//        TToast.show(mContext, msg + " code:" + code);
      }

      @Override
      public void onRenderSuccess(View view, float width, float height) {
        Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
        //返回view的宽高 单位 dp
//        TToast.show(mContext, "渲染成功");
        mExpressContainer.removeAllViews();
        mExpressContainer.addView(view);
      }
    });
    //dislike设置
//    bindDislike(ad, false);
    if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
      return;
    }
    ad.setDownloadListener(new TTAppDownloadListener() {
      @Override
      public void onIdle() {
      }

      @Override
      public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//        if (!mHasShowDownloadActive) {
//          mHasShowDownloadActive = true;
////          TToast.show(BannerExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
//        }
      }

      @Override
      public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//        TToast.show(BannerExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
      }

      @Override
      public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//        TToast.show(BannerExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
      }

      @Override
      public void onInstalled(String fileName, String appName) {
//        TToast.show(BannerExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
      }

      @Override
      public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//        TToast.show(BannerExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
      }
    });
  }

  //插屏广告 加载
  private void loadExpressInterstitialAd(String codeId, int expressViewWidth, int expressViewHeight) {
    //step4:创建广告请求参数AdSlot,具体参数含义参考文档
    AdSlot adSlot = new AdSlot.Builder()
            .setCodeId(codeId) //广告位id
            .setSupportDeepLink(true)
            .setAdCount(1) //请求广告数量为1到3条
            .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
            .build();
    //step5:请求广告，对请求回调的广告作渲染处理
    mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
      @Override
      public void onError(int code, String message) {
//        TToast.show(InteractionExpressActivity.this, "load error : " + code + ", " + message);
      }

      @Override
      public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
        if (ads == null || ads.size() == 0) {
          return;
        }
        mTTAd = ads.get(0);
        bindInterstitialAdListener(mTTAd);
        startTime = System.currentTimeMillis();
        mTTAd.render();
      }
    });
  }

  //插屏广告 监听
  private void bindInterstitialAdListener(TTNativeExpressAd ad) {
    ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
      @Override
      public void onAdDismiss() {
        TToast.show(mContext, "广告关闭");
      }

      @Override
      public void onAdClicked(View view, int type) {
        TToast.show(mContext, "广告被点击");
      }

      @Override
      public void onAdShow(View view, int type) {
        TToast.show(mContext, "广告展示");
      }

      @Override
      public void onRenderFail(View view, String msg, int code) {
        Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
        TToast.show(mContext, msg + " code:" + code);
      }

      @Override
      public void onRenderSuccess(View view, float width, float height) {
        Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
        //返回view的宽高 单位 dp
        TToast.show(mContext, "渲染成功");
        mTTAd.showInteractionExpressAd(activity);

      }
    });
    if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
      return;
    }
    ad.setDownloadListener(new TTAppDownloadListener() {
      @Override
      public void onIdle() {
//        TToast.show(InteractionExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
      }

      @Override
      public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//        if (!mHasShowDownloadActive) {
//          mHasShowDownloadActive = true;
//          TToast.show(InteractionExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
//        }
      }

      @Override
      public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//        TToast.show(InteractionExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
      }

      @Override
      public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//        TToast.show(InteractionExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
      }

      @Override
      public void onInstalled(String fileName, String appName) {
//        TToast.show(InteractionExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
      }

      @Override
      public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//        TToast.show(InteractionExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
      }
    });
  }

}
