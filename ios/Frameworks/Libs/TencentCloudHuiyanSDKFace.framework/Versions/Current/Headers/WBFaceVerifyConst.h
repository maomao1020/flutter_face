//
//  WBFaceVerifyConst.h
//  Pods
//
//  Created by pp on 2017/7/31.
//
//
#import <UIKit/UIKit.h>
#ifndef WBFaceVerifyConst_h
#define WBFaceVerifyConst_h
#define WBCloudReflectionFaceVerifyVersion @"6.5.4"

UIKIT_EXTERN NSString *const WBCloudFaceVerifySDKVersion;

/**
 SDK使用的主题风格

 - WBFaceVerifyThemeDarkness: 暗黑色系主题
 - WBFaceVerifyThemeLightness: 明亮色系主题
 - WBFaceVerifyThemeCustom: 自定义主题，通过修改bundle中的custom.json实现自定义
 */
typedef NS_ENUM(NSInteger, WBFaceVerifyTheme) {
    WBFaceVerifyThemeDarkness = 0,
    WBFaceVerifyThemeLightness,
    WBFaceVerifyThemeCustom,
};

typedef NS_ENUM(NSInteger, WBFaceVerifyLanguage) {
    WBFaceVerifyLanguage_ZH_CN = 0, //简体中文
    WBFaceVerifyLanguage_ZH_HK,     //繁体中文
    WBFaceVerifyLanguage_EN,        //英语
    WBFaceVerifyLanguage_ID,        //印尼语
    WBFaceVerifyLanguage_JA,        //日语
    WBFaceVerifyLanguage_KO,        //韩语
    WBFaceVerifyLanguage_TH         //泰语
};

typedef NS_ENUM(NSInteger, WBFaceCustomTipsLoc) {
    WBFaceCustomTipsLoc_Bottom = 0, //提示语在下
    WBFaceCustomTipsLoc_Top,
};
#endif /* WBFaceVerifyConst_h */
