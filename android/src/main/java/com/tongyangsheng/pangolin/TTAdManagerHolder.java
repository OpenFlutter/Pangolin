package com.tongyangsheng.pangolin;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;

public class TTAdManagerHolder {

    private static boolean sInit;


    public static TTAdManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return TTAdSdk.getAdManager();
    }

    public static void init(Context context, String appId,Boolean useTextureView,String appName, Boolean allowShowNotify, Boolean allowShowPageWhenScreenLock, Boolean debug, Boolean supportMultiProcess) {
        doInit(context,appId,useTextureView,appName,allowShowNotify,allowShowPageWhenScreenLock,debug,supportMultiProcess);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context,String appId,Boolean useTextureView,String appName, Boolean allowShowNotify, Boolean allowShowPageWhenScreenLock, Boolean debug, Boolean supportMultiProcess) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context,appId,useTextureView,appName,allowShowNotify,allowShowPageWhenScreenLock,debug,supportMultiProcess));
            sInit = true;
        }
    }

    private static TTAdConfig buildConfig(Context context,String appId,Boolean useTextureView,String appName, Boolean allowShowNotify, Boolean allowShowPageWhenScreenLock, Boolean debug, Boolean supportMultiProcess) {
        return new TTAdConfig.Builder()
                .appId(appId)
                .useTextureView(useTextureView) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(appName)
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(allowShowNotify) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(allowShowPageWhenScreenLock) //是否在锁屏场景支持展示广告落地页
                .debug(debug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .supportMultiProcess(supportMultiProcess)//是否支持多进程
                .needClearTaskReset()
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build();
    }
}
