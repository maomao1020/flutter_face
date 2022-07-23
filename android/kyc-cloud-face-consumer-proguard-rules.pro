#不混淆内部类
-keepattributes InnerClasses

#不混淆jni调用类
-keepclasseswithmembers class *{
    native <methods>;
}

###################### faceverify-BEGIN ###########################
-ignorewarnings
-keep public class com.tencent.ytcommon.**{*;}
-keep class com.tencent.turingfd.sdk.mfa.TNative$aa { public *; }
-keep class com.tencent.turingfd.sdk.mfa.TNative$aa$bb { public *;}
-keep class com.tencent.turingcam.** {*;}
-keep class com.tencent.turingfd.** {*;}

-keep public class com.tencent.youtu.ytagreflectlivecheck.jni.**{*;}
-keep public class com.tencent.youtu.ytagreflectlivecheck.YTAGReflectLiveCheckInterface{
    public <methods>;
}
-keep public class com.tencent.youtu.ytposedetect.jni.**{*;}
-keep public class com.tencent.youtu.ytposedetect.data.**{*;}
-keep public class com.tencent.youtu.liveness.YTFaceTracker{*;}
-keep public class com.tencent.youtu.liveness.YTFaceTracker$*{*;}

-keep public class com.tencent.cloud.huiyansdkface.facelight.net.*$*{
    *;
}
-keep public class com.tencent.cloud.huiyansdkface.facelight.net.**{
    *;
}
-keep public class com.tencent.cloud.huiyansdkface.facelight.config.cdn.WbUiTips{
    *;
}

#================数据上报混淆规则 start===========================
#实体类
-keep class com.tencent.cloud.huiyansdkface.analytics.EventSender{
    *;
}
-keep class com.tencent.cloud.huiyansdkface.analytics.EventSender$*{
    *;
}
-keep class com.tencent.cloud.huiyansdkface.analytics.WBSAEvent{
     *;
}
-keep class com.tencent.cloud.huiyansdkface.analytics.WBSAParam{
     *;
}
#================数据上报混淆规则 end===========================

#######################faceverify-END#############################

####################### normal混淆规则-BEGIN#############################
#不混淆内部类
-keepattributes InnerClasses
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

-keep public class com.tencent.cloud.huiyansdkface.normal.net.*$*{
    *;
}
-keep public class com.tencent.cloud.huiyansdkface.normal.net.*{
    *;
}
#bugly
-keep class com.tencent.bugly.idasc.**{
    *;
}
#wehttp混淆规则
-dontwarn com.tencent.cloud.huiyansdkface.okio.**
-keep class com.tencent.cloud.huiyansdkface.okio.**{
    *;
}
-dontwarn com.tencent.cloud.huiyansdkface.okhttp3.OkHttpClient$Builder

####################### normal混淆规则-END#############################


