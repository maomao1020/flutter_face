#import "FlutterFacePlugin.h"
#import <TencentCloudHuiyanSDKFace/WBFaceVerifyCustomerService.h>

@interface FlutterFacePlugin()<WBFaceVerifyCustomerServiceDelegate>

@property (nonatomic, copy) FlutterResult result;

@end

@implementation FlutterFacePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_face"
            binaryMessenger:[registrar messenger]];
  FlutterFacePlugin* instance = [[FlutterFacePlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  }else if([@"startFaceService" isEqualToString:call.method]){
      self.result = result;
      NSDictionary *params = call.arguments;
      NSLog(@"传参=%@", params);
      NSDictionary *config = params[@"config"];
      WBFaceVerifySDKConfig *sdkConfig = [WBFaceVerifySDKConfig sdkConfig];
    //        sdkConfig.enableCloseEyes = [config[@"detectCloseEyes"] boolValue];
      sdkConfig.mute = [config[@"playVoice"] boolValue];
      sdkConfig.recordVideo = [config[@"recordVideo"] boolValue];
      sdkConfig.showFailurePage = [config[@"showFailurePage"] boolValue];
      sdkConfig.showSuccessPage = [config[@"showSuccessPage"] boolValue];
      sdkConfig.theme = (WBFaceVerifyTheme)[config[@"theme"] integerValue];
    //        [sdkConfig setValue:@"https://idav6.test.webank.com" forKey:@"baseUrl"];
      [WBFaceVerifyCustomerService sharedInstance].delegate = self;
      dispatch_async(dispatch_get_main_queue(), ^{
          
          [[WBFaceVerifyCustomerService sharedInstance] initSDKWithUserId:params[@"userId"] nonce:params[@"nonce"] sign:params[@"sign"] appid:params[@"appId"] orderNo:params[@"order"] apiVersion:@"1.0.0" licence:params[@"licence"] faceId:params[@"faceId"] sdkConfig:sdkConfig success:^{
              [[WBFaceVerifyCustomerService sharedInstance] startWbFaceVeirifySdk];
          } failure:^(WBFaceError * _Nonnull error) {
              NSLog(@"%@", error.description);
              self.result(@{@"success" : @"0", @"desc" : error.desc});
          }];
      });
  }else {
    result(FlutterMethodNotImplemented);
  }
}

-(void)wbfaceVerifyCustomerServiceDidFinishedWithFaceVerifyResult:(WBFaceVerifyResult *)faceVerifyResult{
    NSString *userImageString = faceVerifyResult.userImageString;
    if (userImageString) {
        //        dispatch_async(dispatch_get_global_queue(0, 0), ^{
        //            UIImage *userImage = [self base64StringToUIImage:userImageString];
        //            __block ALAssetsLibrary *lib = [[ALAssetsLibrary alloc] init];
        //            [lib writeImageToSavedPhotosAlbum:userImage.CGImage metadata:nil completionBlock:^(NSURL *assetURL, NSError *error) {
        //                NSLog(@"assetURL = %@, error = %@", assetURL, error);
        //                lib = nil;
        //
        //            }];
        //        });
    }


    if (faceVerifyResult.isSuccess) {
        NSString *message = [NSString stringWithFormat:@"liveRate: %@, similarity: %@", faceVerifyResult.liveRate, faceVerifyResult.similarity];
        self.result(@{@"success" : @"1", @"desc" : message});
    }else {
        NSString *message = [NSString stringWithFormat:@"%@, liveRate:%@, similarity:%@, sign: %@", faceVerifyResult.error.desc, faceVerifyResult.liveRate, faceVerifyResult.similarity, faceVerifyResult.sign];
        self.result(@{@"success" : @"0", @"desc" : message});
    }
}
@end
