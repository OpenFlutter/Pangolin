import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:pangolin/pangolin.dart';


MethodChannel _channel = MethodChannel('com.tongyangsheng.pangolin')..setMethodCallHandler(_methodHandler);


StreamController<BasePangolinResponse> _pangolinResponseEventHandlerController = new StreamController.broadcast();

Stream<BasePangolinResponse> get pangolinResponseEventHandler =>
    _pangolinResponseEventHandlerController.stream;

Future<bool> registerPangolin({
  @required String appId,
  @required bool useTextureView,
  @required String appName,
  @required bool allowShowNotify,
  @required bool allowShowPageWhenScreenLock,
  @required bool debug,
  @required bool supportMultiProcess}) async{
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

Future<bool> loadSplashAd({
  @required String mCodeId,
  @required bool debug}) async{
  return await _channel.invokeMethod("loadSplashAd",
      {
        "mCodeId":mCodeId,
        "debug":debug
      }
  );
}

Future loadRewardAd({
  @required bool isHorizontal,
  @required String mCodeId,
  @required bool debug}) async {
  return await _channel.invokeMethod("loadRewardAd",
      {
        "isHorizontal" : isHorizontal,
        "mCodeId" : mCodeId,
        "debug" : debug
      });
}


Future _methodHandler(MethodCall methodCall) {
  var response =
  BasePangolinResponse.create(methodCall.method, methodCall.arguments);
  _pangolinResponseEventHandlerController.add(response);
  return Future.value();
}