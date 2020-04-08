import 'dart:async';

import 'package:flutter/services.dart';

class Pangolin {
  static const MethodChannel _channel =
      const MethodChannel('com.tongyangsheng.pangolin');

  static Future<bool> register(String appId,bool useTextureView,String appName,bool allowShowNotify,bool allowShowPageWhenScreenLock,bool debug,bool supportMultiProcess) async{
    assert(appId != Null);
    return await _channel.invokeMethod("register",
        {
          "appId":appId,
          "useTextureView":useTextureView,
          "appName":appName,
          "allowShowNotify":allowShowNotify,
          "allowShowPageWhenScreenLock":allowShowPageWhenScreenLock,
          "debug":debug,
          "supportMultiProcess":supportMultiProcess
        }
    );
  }

  static Future<bool> loadSplashAd(String mCodeId, bool debug) async{
    return await _channel.invokeMethod("loadSplashAd",
        {
          "mCodeId":mCodeId,
          "debug":debug
        }
    );
  }

  static Future loadExpressAd(bool isHorizontal, String mCodeId) async {
    return await _channel.invokeMethod("loadExpressAd",
    {
      "isHorizontal" : isHorizontal,
      "mCodeId" : mCodeId
    });
  }
}
