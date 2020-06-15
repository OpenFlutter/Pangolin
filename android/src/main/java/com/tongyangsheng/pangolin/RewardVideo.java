package com.tongyangsheng.pangolin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class RewardVideo {
    private static final String TAG = "RewardVideo";
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;

    public String mHorizontalCodeId = null;
    public String mVerticalCodeId = null;
    public Boolean supportDeepLink = true;
    public String rewardName = null;
    public int rewardAmount = 0;
    public double expressViewAcceptedSizeW = 500;
    public double expressViewAcceptedSizeH = 500;
    public String userID = "user123";
    public String mediaExtra = "media_extra";
    public boolean debug = false;

    private boolean mIsExpress = false; //是否请求模板广告



    static MethodChannel _channel;
    public Context context;
    public Activity activity;

    public void init()
    {
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(context);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(context);
        configAd();
    }

    private void configAd()
    {
        if (mHorizontalCodeId != null)
        {
            loadAd(mHorizontalCodeId,supportDeepLink,rewardName,rewardAmount,(float) expressViewAcceptedSizeW,(float) expressViewAcceptedSizeH,userID,mediaExtra, TTAdConstant.HORIZONTAL);
        }
        else
        {
            loadAd(mVerticalCodeId,supportDeepLink,rewardName,rewardAmount,(float) expressViewAcceptedSizeW,(float) expressViewAcceptedSizeH,userID,mediaExtra, TTAdConstant.VERTICAL);
        }
    }

    private boolean mHasShowDownloadActive = false;

    private void loadAd(final String codeId,boolean supportDeepLink,String rewardName,int rewardAmount, float expressViewAcceptedSizeW, float expressViewAcceptedSizeH, String userID, String mediaExtra, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot;
        if (mIsExpress) {
            //个性化模板广告需要传入期望广告view的宽、高，单位dp，
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(supportDeepLink)
                    .setRewardName(rewardName) //奖励的名称
                    .setRewardAmount(rewardAmount)  //奖励的数量
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
                    .setExpressViewAcceptedSize(expressViewAcceptedSizeW,expressViewAcceptedSizeH)
                    .setUserID(userID)//用户id,必传参数
                    .setMediaExtra(mediaExtra) //附加参数，可选
                    .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
        } else {
            //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(supportDeepLink)
                    .setRewardName(rewardName) //奖励的名称
                    .setRewardAmount(rewardAmount)  //奖励的数量
                    .setUserID(userID)//用户id,必传参数
                    .setMediaExtra(mediaExtra) //附加参数，可选
                    .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
        }
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "onError: " + code + ", " + String.valueOf(message));
                if(debug)
                {
                    TToast.show(context, message);
                }
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                if (debug)
                {
                    Log.e(TAG, "onRewardVideoCached");
                    TToast.show(context, "rewardVideoAd video cached");
                }
                mttRewardVideoAd.showRewardVideoAd(activity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
                mttRewardVideoAd = null;
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                if (debug)
                {
                    Log.e(TAG, "onRewardVideoAdLoad");
                    TToast.show(context, "rewardVideoAd loaded 广告类型：" + getAdType(ad.getRewardVideoAdType()));
                }
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        if (debug)
                        {
                            TToast.show(context, "rewardVideoAd show");
                        }
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        if (debug)
                        {
                            TToast.show(context, "rewardVideoAd bar click");
                        }
                    }

                    @Override
                    public void onAdClose() {
                        if (debug)
                        {
                            TToast.show(context, "rewardVideoAd close");
                        }
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        if (debug)
                        {
                            TToast.show(context, "rewardVideoAd complete");
                        }
                    }

                    @Override
                    public void onVideoError() {
                        if (debug)
                        {
                            TToast.show(context, "rewardVideoAd error");
                        }
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        if (debug)
                        {
                            TToast.show(context, "verify:" + rewardVerify + " amount:" + rewardAmount +
                                    " name:" + rewardName);
                        }
                        Map<String,Object> rewardVideoCallBack = new HashMap<>();
                        rewardVideoCallBack.put("rewardVerify",rewardVerify);
                        rewardVideoCallBack.put("rewardAmount",rewardAmount);
                        rewardVideoCallBack.put("rewardName",rewardName);
                        _channel.invokeMethod("onRewardResponse",rewardVideoCallBack);
                    }

                    @Override
                    public void onSkippedVideo() {
                        if (debug)
                        {
                            TToast.show(context, "rewardVideoAd has onSkippedVideo");
                        }
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (debug)
                        {
                            Log.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        }

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            if (debug)
                            {
                                TToast.show(context, "下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                            }
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        if (debug)
                        {
                            Log.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                            TToast.show(context, "下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        if (debug)
                        {
                            Log.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                            TToast.show(context, "下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        if (debug)
                        {
                            Log.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                            TToast.show(context, "下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        if (debug)
                        {
                            Log.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                            TToast.show(context, "安装完成，点击下载区域打开", Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });
    }

    private String getAdType(int type) {
        switch (type) {
            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通激励视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable激励视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;
        }

        return "未知类型+type=" + type;
    }

}
