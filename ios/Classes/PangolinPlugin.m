#import "PangolinPlugin.h"
#import <BUAdSDK/BUAdSDK.h>

@interface PangolinPlugin ()<BUNativeExpressRewardedVideoAdDelegate,BUSplashAdDelegate,
BUNativeAdsManagerDelegate,BUVideoAdViewDelegate,BUNativeAdDelegate,
BUNativeExpressAdViewDelegate,
BUNativeExpressFullscreenVideoAdDelegate,
BUNativeExpresInterstitialAdDelegate>
@property (nonatomic, strong) UITextField *playableUrlTextView;
@property (nonatomic, strong) UITextField *downloadUrlTextView;
@property (nonatomic, strong) UITextField *deeplinkUrlTextView;
@property (nonatomic, strong) UILabel *isLandscapeLabel;
@property (nonatomic, strong) UISwitch *isLandscapeSwitch;
@property (nonatomic, assign) BOOL isPlayableUrlValid;
@property (nonatomic, assign) BOOL isDownloadUrlValid;
@property (nonatomic, assign) BOOL isDeeplinkUrlValid;

@property (nonatomic, strong) BURewardedVideoAd *rewardedVideoAd;
@property (nonatomic, strong) BUNativeExpressRewardedVideoAd *rewardedAd;

@end

@implementation PangolinPlugin

FlutterMethodChannel* globalMethodChannel;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"com.tongyangsheng.pangolin"
                                     binaryMessenger:[registrar messenger]];
    PangolinPlugin* instance = [[PangolinPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
    
    globalMethodChannel = channel;
}


- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"register" isEqualToString:call.method]) {
        NSString* appId = call.arguments[@"appId"];
        if ([@"" isEqualToString:appId]) {
            result([FlutterError errorWithCode:@"500" message:@"appId can't be null" details:nil]);
        }
        [BUAdSDKManager setAppID:appId];
        result(@YES);
    }
    else if([@"loadSplashAd" isEqualToString:call.method])
    {
        NSString* mCodeId = call.arguments[@"mCodeId"];
        
        [BUAdSDKManager setIsPaidApp:NO];
        [BUAdSDKManager setLoglevel:BUAdSDKLogLevelDebug];
        CGRect frame = [UIScreen mainScreen].bounds;
        BUSplashAdView *splashView = [[BUSplashAdView alloc] initWithSlotID:mCodeId frame:frame];
        splashView.delegate = self;
        UIWindow *keyWindow = [UIApplication sharedApplication].windows.firstObject;
        [splashView loadAdData];
        [keyWindow.rootViewController.view addSubview:splashView];
        splashView.rootViewController = keyWindow.rootViewController;
    }
    else if([@"loadRewardAd" isEqualToString:call.method])
    {
            NSString* slotId = call.arguments[@"mCodeId"];
            NSString* userId = call.arguments[@"userID"];
            NSString* rewardName = call.arguments[@"rewardName"];
            NSInteger rewardAmount = [call.arguments[@"rewardAmount"] intValue];
            NSString* mediaExtra = call.arguments[@"mediaExtra"];
            
            BURewardedVideoModel *model = [[BURewardedVideoModel alloc] init];
            model.userId = userId;
            model.rewardName = rewardName;
            model.extra = mediaExtra;
            model.rewardAmount = rewardAmount;
        
            self.rewardedAd = [[BUNativeExpressRewardedVideoAd alloc] initWithSlotID:slotId rewardedVideoModel:model];
            self.rewardedAd.delegate = self;
            [self.rewardedAd loadAdData];
            result(@YES);
    }
    else
    {
        result(FlutterMethodNotImplemented);
    }
}

//展示视频用
- (UIViewController *)rootViewController{
    UIViewController *rootVC = [[UIApplication sharedApplication].delegate window].rootViewController;
    
    UIViewController *parent = rootVC;
    while((parent = rootVC.presentingViewController) != nil){
        rootVC = parent;
    }
    
    while ([rootVC isKindOfClass:[UINavigationController class]]) {
        rootVC = [(UINavigationController *)rootVC topViewController];
    }
    
    return rootVC;
}

//激励视频渲染完成并展示
- (void)nativeExpressRewardedVideoAdViewRenderSuccess:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    [self.rewardedAd showAdFromRootViewController: [self rootViewController]];
    
}

//激励视频播放完成
- (void)nativeExpressRewardedVideoAdDidPlayFinish:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
    NSMutableDictionary *mutableDictionary=[NSMutableDictionary dictionaryWithCapacity:3];
    [mutableDictionary setValue:@YES forKey:@"rewardVerify"];
    [mutableDictionary setValue:NULL forKey:@"rewardAmount"];
    [mutableDictionary setValue:NULL forKey:@"rewardName"];
    
    [globalMethodChannel invokeMethod:@"onRewardResponse" arguments:mutableDictionary];
}

- (void)nativeExpressRewardedVideoAdServerRewardDidSucceed:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd verify:(BOOL)verify {
    
}

//激励视频关闭
- (void)nativeExpressRewardedVideoAdDidClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    
}

@end



