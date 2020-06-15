<h1 align="center">Pangolin 错误文档</h1>

## 前言
作为一个插件而言，我只能做到尽可能的接近原生和穿山甲SDK，各位可能会遇到各种各样的问题，但是相信都不难解决，这里我专门开通一个文档记录大家遇到及可能遇到的问题，问题来源主要包括issue，私人反馈，本人测试。

## Android
### 报错1
这个问题应该难不倒Android开发的同学，主要提醒一下iOS developer,加一个标签，轻松解决。
<p align="center">
<img src=https://github.com/tongyangsheng/Pangolin/blob/master/showImage/error1.jpg alt="drawing" width="600">
</p>

方法：
<p align="center">
<img src=https://github.com/tongyangsheng/Pangolin/blob/master/showImage/void1.jpeg alt="drawing" width="600">
</p>

### 有关开屏广告黑屏和激励视频回调收不到的问题
* step1
检查你的广告调用方法有没有使用await 进行异步操作，仔细确认自己的调用方式和example中的实例代码相同
* step2 
在issue中已经有人提过相关问题并解决，此处不做赘述点击👉[开屏广告黑屏issue](https://github.com/OpenFlutter/Pangolin/issues/3)
* step3
有使用者反应，可以在buildTypes的release中添加配置修复打包后黑屏问题
<p align="center">
<img src=https://github.com/tongyangsheng/Pangolin/blob/master/showImage/error2.png alt="drawing" width="600">
</p>



