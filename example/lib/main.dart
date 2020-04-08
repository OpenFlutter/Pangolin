import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:pangolin/pangolin.dart';

void main() => runApp(MyApp());



class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
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
    if(statuses[Permission.location] != PermissionStatus.granted){
      print("无位置权限");
    }

    Pangolin.register(
        "5056758",
        true,
        "爱看",
        true,
        true,
        true,
        true
    );
    Pangolin.loadSplashAd("887310537",false);
//    Pangolin.loadExpressAd(true,"945124378");
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
