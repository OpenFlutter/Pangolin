<p align="center">
<img src=https://github.com/tongyangsheng/Pangolin/blob/master/showImage/1.jpg alt="drawing" width="700">
</p>

<h1 align="center">Pangolin</h1>

<p>
<a href="https://www.npmjs.com/package/drone"><img src=https://img.shields.io/badge/license-MIT-brightgreen></a>
<a href="https://www.apple.com/lae/ios/ios-13/"><img src=https://img.shields.io/badge/platform-ios-lightgrey></a>
<a href="https://www.Android.com/package/drone"><img src=https://img.shields.io/badge/platform-Android-lightgrey></a>
<a href="https://www.dart.dev"><img src=https://img.shields.io/badge/Language-Dart-orange></a>
<a href="https://www.flutter.dev"><img src=https://img.shields.io/badge/Flutter-v1.12.13-informational></a>
<a href="https://www.dart.dev"><img src=https://img.shields.io/badge/Dart-v2.4.1-informational></a>
<a href="https://github.com/tongyangsheng/flutter_reader"><img src=https://img.shields.io/badge/Pangolin-v0.0.2-success></a>
</p>

## 前言
⚠️在使用本插件前请认真，仔细阅读[穿山甲官方文档](http://partner.toutiao.com/doc?id=5dd0fe756b181e00112e3ec5)。本插件将尽量保留SDK内容和各API相关内容，如出现在官方文档以外报错信息可以留言issue,或通过文末联系方式联系作者（注明来意）。针对你可能会遇到的问题，在使用过程中可以先查阅👉
### [Pangolin报错及其解决方案](https://github.com/tongyangsheng/Pangolin/blob/master/PangolinError.md)

## 简介
Pangolin是一款Flutter插件，集成了字节跳动旗下的广告平台——穿山甲的Android和iOS的SDK，方便开发者直接在Flutter层面调用相关方法。

## 插件开发环境相关
### Flutter
```
Flutter (Channel stable, v1.12.13+hotfix.8, on Mac OS X 10.15.1 19B88, locale zh-Hans-CN)
```
### Dart
```
Dart VM version: 2.4.1 (Wed Aug 7 13:15:56 2019 +0200) on "macos_x64"
```
### Platform
```
Xcode - develop for iOS and macOS (Xcode 11.2)
Android Studio (version 3.6)
```
### 穿山甲
```
iOS - 2.5.1.5(cocoapods lastest version)
Android - 2.9.5.0
```

## 安装
```yaml
# add this line to your dependencies
dependencies:
  pangolin: ^0.0.2
```

## 环境配置
使用前请确认您以根据穿山甲的官方文档中的步骤进行了相应的依赖添加，权限获取以及参数配置
### Android
[穿山甲Android SDK 接入基础配置](https://partner.oceanengine.com/union/media/union/download/detail?id=4&docId=5de8d9b425b16b00113af0da&osType=android)<br>
⚠️说明：<br>
* 从Android 6.0(API 23)开始，对系统权限做了很大的改变。在之前用户安装APP前，只是把APP需要使用的权限列出来给用户告知一下，APP安装后都可以访问这些权限。从6.0开始，一些敏感权限，需要在使用时动态申请，并且用户可以选择拒绝授权访问这些权限，已授予过的权限，用户也可以去APP设置页面去关闭授权。有关动态权限的获取pub上有很多package，我个人不会在插件内集成权限申请相关，方便使用者灵活配置，可根据实际需求选择相应插件和权限。

* 穿山甲官方推荐在合适时机申请用户权限，但是使用本插件前请务必确认你所需的权限已经获得用户授权。

#### iOS
[穿山甲iOS SDK 接入基础配置](https://partner.oceanengine.com/union/media/union/download/detail?id=16&docId=5de8d570b1afac00129330c5&osType=ios)

## Pangolin集成
### Android 
在Android端你可能需要简单的四个小步骤导入穿山甲SDK具体步骤已为你写好请戳👉
#### [Pangolin Android集成手册](https://github.com/tongyangsheng/Pangolin/blob/master/AndroidProfile.md)
### iOS
```
pod install
```
## 穿山甲平台
在使用之前必须确认您在穿山甲平台的[控制台](https://partner.oceanengine.com/union/media/union/site)已经注册了自己app所对应的应用以及对应广告类型的代码位，由于穿山甲包含多种类型的广告和功能请务必确认你在插件中注册的和你在平台注册的一一对应。
## 开始使用
### 初始化（register）
调用穿山甲SDK的第一步是对SDK的初始化

```dart
await Pangolin.registerPangolin(
        appId: "Your AppID",
        useTextureView: true,
        appName: "Your AppName",
        allowShowNotify: true,
        allowShowPageWhenScreenLock: true,
        debug: true,
        supportMultiProcess: true
    )
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| appId      | 在穿山甲平台注册的自己的AppId | null |
| useTextureView       | 使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView       |   false |
| appName  | 自己的应用名称       |    null |
| allowShowNotify   | 是否允许sdk展示通知栏提示       |    true |
| allowShowPageWhenScreenLock  | 是否在锁屏场景支持展示广告落地页       |    true |
| debug  | 测试阶段打开，可以通过日志排查问题，上线时去除该调用       |    true |
| supportMultiProcess  | 是否支持多进程      |    false |

* 注意以上参数大部分针对Android端，iOS端由于穿山甲SDK本身的原因并没有过多的参数配置，有用的参数仅为appId，appName。
#### 接入成功debug信息
* Android
```
E/TTAdSdk-InitChecker( 5148): ==当前进程名：com.tongyangsheng.pangolin_example
E/TTAdSdk-InitChecker( 5148): ==穿山甲sdk接入，环境为debug，初始化配置检测开始==
E/TTAdSdk-InitChecker( 5148): AndroidManifest.xml中TTMultiProvider配置正常
E/TTAdSdk-InitChecker( 5148): AndroidManifest.xml中TTFileProvider配置正常
E/TTAdSdk-InitChecker( 5148): AndroidManifest.xml中权限配置正常
E/TTAdSdk-InitChecker( 5148): 动态权限正常：android.permission.READ_PHONE_STATE
E/TTAdSdk-InitChecker( 5148): 动态权限正常：android.permission.ACCESS_COARSE_LOCATION
E/TTAdSdk-InitChecker( 5148): 动态权限正常：android.permission.ACCESS_FINE_LOCATION
E/TTAdSdk-InitChecker( 5148): 动态权限正常：android.permission.WRITE_EXTERNAL_STORAGE
E/TTAdSdk-InitChecker( 5148): ==穿山甲sdk初始化配置检测结束==
```

* iOS 
仅提示穿山甲接入成功

⚠️进行下一步操作前请确认，穿山甲已经成功接入并且检测正常。

### 加载开屏广告
```dart
        Pangolin.loadSplashAd(
            mCodeId: "Your CodeId",
            debug: false);
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| mCodeId      | 在穿山甲平台注册的自己的广告位id | null |
| debug  | 测试阶段打开，可以通过日志排查问题，上线时去除该调用       |    true |

### 加载激励视频(Android)
激励视频的原生接入相对复杂，但是我已经给各位留好了接口，只需简单的几步就可以加载到你的激励视频<br/>
⚠️使用前请确认您已在穿山甲平台的[控制台](https://partner.oceanengine.com/union/media/union/site)建立了你的激励视频广告id。<br/>
```dart
await Pangolin.loadRewardAd(
      isHorizontal: false,
      mCodeId: "Your CodeId",
      debug: true
        );
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| mCodeId      | 在穿山甲平台注册的自己的广告位id | null |
| debug  | 此处debug为true的情况下 我会给你显示整体进程的一个Toast 方便你调试      |    true |

### 激励视频回调监听(Android)
在合适的位置注册你的监听，保证用户看完广告时接收到我给你的回调信息，并做下一步处理
```dart
Pangolin.pangolinResponseEventHandler.listen((value)
    {
      if(value is Pangolin.onRewardResponse)
        {
          print("激励视频回调：${value.rewardVerify}");
          print("激励视频回调：${value.rewardName}");
          print("激励视频回调：${value.rewardAmount}");
        }
      else
        {
          print("回调类型不符合");
        }
    });
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| rewardVerify      | 验证奖励有效性，即用户是否完成观看 | / |
| rewardName  | 你在穿山甲填写的奖励名称      |    / |
| rewardName  | 你在穿山甲填写的奖励数量     |    / |

激励视频的具体使用参见项目目录下Example

## 测试说明
穿山甲的测试个人建议在真机进行测试，我本人在模拟器上会遇到各种疑难杂症，虽然插件和穿山甲SDK的报错都能看到，但是直接上真机很多报错会减少，这个由使用者自行决定，建议仅供参考
## 感谢
感谢各位Flutter开发者的支持和帮助，如果本插件能为你在开发过程中省下一点点的时间和精力算是达到了我开发插件的初衷。如果喜欢，欢迎点个🌟持续关注。如果项目关注度高，会继续开发SDK的剩余部分和分享一些开发插件的心得和踩坑经验。


## 个人联系方式
* QQ:964997115<br/>
* Wechat:tys19971122<br/>

