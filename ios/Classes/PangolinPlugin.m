#import "PangolinPlugin.h"
#import <BUAdSDK/BUAdSDK.h>

@implementation PangolinPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"com.tongyangsheng.pangolin"
                                     binaryMessenger:[registrar messenger]];
    PangolinPlugin* instance = [[PangolinPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
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
        [BUAdSDKManager setIsPaidApp:NO];
        [BUAdSDKManager setLoglevel:BUAdSDKLogLevelDebug];
        CGRect frame = [UIScreen mainScreen].bounds;
        BUSplashAdView *splashView = [[BUSplashAdView alloc] initWithSlotID:@"887310537" frame:frame];
        splashView.delegate = self;
        UIWindow *keyWindow = [UIApplication sharedApplication].windows.firstObject;
        [splashView loadAdData];
        [keyWindow.rootViewController.view addSubview:splashView];
        splashView.rootViewController = keyWindow.rootViewController;
    }
    else
    {
        result(FlutterMethodNotImplemented);
    }
}

@end



