import 'package:flutter/material.dart';
import 'dart:async';

import 'package:pangolin/pangolin.dart' as Pangolin;
import 'package:permission_handler/permission_handler.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    Pangolin.pangolinResponseEventHandler.listen((value) {
      if (value is Pangolin.RewardResponse) {
        print("激励视频回调：${value.rewardVerify}");
        print("激励视频回调：${value.rewardName}");
        print("激励视频回调：${value.rewardAmount}");

        if (value.rewardName == "rewardVideo Close") {
          debugPrint("视频关闭了");
        }
      } else {
        print("回调类型不符合");
      }
    });
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;

    Map<Permission, PermissionStatus> statuses = await [
      Permission.phone,
      Permission.location,
      Permission.storage,
    ].request();
    //校验权限
    if (statuses[Permission.location] != PermissionStatus.granted) {
      print("无位置权限");
    }
    _initPangolin();
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

//  "5056758",
//  true,
//  "爱看",
//  true,
//  true,
//  true,
//  true
  _initPangolin() async {
    await Pangolin.registerPangolin(
            appId: "5056758",
            useTextureView: true,
            appName: "爱看",
            allowShowNotify: true,
            allowShowPageWhenScreenLock: true,
            debug: true,
            supportMultiProcess: true)
        .then((v) {
      _loadRewardAd();
    });
  }

  _loadSplashAd() async {
    Pangolin.loadSplashAd(mCodeId: "887310537", debug: false);
  }

  //945122969
  _loadRewardAd() async {
    await Pangolin.loadRewardAd(
        isHorizontal: false,
        debug: true,
        mCodeId: "945122969",
        supportDeepLink: true,
        rewardName: "书币",
        rewardAmount: 3,
        isExpress: true,
        expressViewAcceptedSizeH: 500,
        expressViewAcceptedSizeW: 500,
        userID: "user123",
        mediaExtra: "media_extra");
  }

  _loadBannerAd() async {
    await Pangolin.loadBannerAd(
        mCodeId: "945330217",
        supportDeepLink: true,
        expressViewWidth: 600,
        expressViewHeight: 300,
        isCarousel: true,
        interval: 40,
        topMargin: 300);
  }

  _loadInterstitialAd() async {
    await Pangolin.loadInterstitialAd(
        mCodeId: "945332768", expressViewWidth: 300, expressViewHeight: 300);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Center(
            child: FlatButton(
              onPressed: () {},
              child: Text("Pangolin"),
            ),
          ),
        ),
      ),
    );
  }
}
