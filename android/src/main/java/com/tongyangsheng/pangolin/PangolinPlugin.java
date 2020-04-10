package com.tongyangsheng.pangolin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.HashMap;
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


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
      onAttachedToEngine(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
  }

    private void onAttachedToEngine(Context applicationContext , BinaryMessenger messenger) {
        this.applicationContext = applicationContext;
        methodChannel = new MethodChannel(messenger, "com.tongyangsheng.pangolin");
        methodChannel.setMethodCallHandler(this);
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
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.tongyangsheng.pangolin");
    channel.setMethodCallHandler(new PangolinPlugin());
  }

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
      if (appId == null || appId == "")
      {
        result.error("500","appId can't be null",null);
      }
      else {
        if (appName == null || appName == "") {
          result.error("600", "appName can't be null", null);
        } else {
          TTAdManagerHolder.init(applicationContext, appId, useTextureView, appName, allowShowNotify, allowShowPageWhenScreenLock, debug, supportMultiProcess);
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
    else if (call.method.equals("loadRewardAd"))
    {
      Boolean isHorizontal = call.argument("isHorizontal");
      String mCodeId = call.argument("mCodeId");
      Boolean debug = call.argument("debug");

      if (isHorizontal == null)
      {
        result.error("600","isHorizonal can not be null",null);
      }
      else
      {
        RewardVideoActivity._channel = methodChannel;
        if(isHorizontal)
        {
          Intent intent = new Intent();
          intent.setClass(activity, RewardVideoActivity.class);
          intent.putExtra("horizontal_rit",mCodeId);
          intent.putExtra("debug",debug);
          activity.startActivity(intent);
        }
        else
        {
          Intent intent = new Intent();
          intent.setClass(activity, RewardVideoActivity.class);
          intent.putExtra("vertical_rit",mCodeId);
          intent.putExtra("debug",debug);
          activity.startActivity(intent);
        }
      }
    }
    else
      {
      result.notImplemented();
    }
  }
}
