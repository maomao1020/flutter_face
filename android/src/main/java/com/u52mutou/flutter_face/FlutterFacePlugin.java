package com.u52mutou.flutter_face;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import com.tencent.cloud.huiyansdkface.facelight.api.WbCloudFaceContant;
import com.tencent.cloud.huiyansdkface.facelight.api.WbCloudFaceVerifySdk;
import com.tencent.cloud.huiyansdkface.facelight.api.listeners.WbCloudFaceVerifyLoginListener;
import com.tencent.cloud.huiyansdkface.facelight.api.listeners.WbCloudFaceVerifyResultListener;
import com.tencent.cloud.huiyansdkface.facelight.api.result.WbFaceError;
import com.tencent.cloud.huiyansdkface.facelight.api.result.WbFaceVerifyResult;
import com.tencent.cloud.huiyansdkface.facelight.process.FaceVerifyStatus;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


/** FlutterFacePlugin */
public class FlutterFacePlugin implements FlutterPlugin, MethodCallHandler , ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private static final String TAG = "FlutterFacePlugin";
  private MethodChannel channel;
  private Activity activity;



  @Override
  public void onAttachedToActivity(@NonNull @NotNull ActivityPluginBinding binding) {
    activity  = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_face");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equals("startFaceService")){
      String faceId = call.argument("faceId");
      String orderNo = call.argument("order");
      String nonce = call.argument("nonce");
      String sign = call.argument("sign");
      String userId = call.argument("userId");
      String appId = call.argument("appId");
      //???????????????????????????/????????????
      String mode = call.argument("mode");
      //????????????????????????/???????????????/?????????
      String type = call.argument("type");
      String licence = call.argument("licence");

      Map<String, String> config = call.argument("config");

      openCloudFaceService(faceId, orderNo, appId, nonce, userId, sign, mode, type, licence, config, result);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private void openCloudFaceService(String faceId, String order, String appId, String nonce, String userId, String sign, String mode, String type, String keyLicence, Map<String, String> config,MethodChannel.Result callbackResult) {
    String faceVerifyCompareType = getFaceVerifyCompareType(type);

    Bundle data = new Bundle();
    WbCloudFaceVerifySdk.InputData inputData = new WbCloudFaceVerifySdk.InputData(
            faceId,
            order,
            appId,
            "1.0.0",
            nonce,
            userId,
            sign,
            FaceVerifyStatus.Mode.GRADE,
            keyLicence);

    boolean isShowSuccess = config.get("showSuccessPage").equals("0") ? false : true;
    boolean isShowFail = config.get("showFailurePage").equals("0") ? false : true;
    boolean isRecordVideo = config.get("recordVideo").equals("0") ? false : true;
    boolean isEnableCloseEyes = config.get("detectCloseEyes").equals("0") ? false : true;
    boolean isPlayVoice = config.get("playVoice").equals("0") ? false : true;
    String color = config.get("showSuccessPage").equals("0") ? WbCloudFaceContant.BLACK : WbCloudFaceContant.WHITE;

    data.putSerializable(WbCloudFaceContant.INPUT_DATA, inputData);
    //?????????????????????????????????????????????
    data.putBoolean(WbCloudFaceContant.SHOW_SUCCESS_PAGE, isShowSuccess);
    //?????????????????????????????????????????????
    data.putBoolean(WbCloudFaceContant.SHOW_FAIL_PAGE, isShowFail);
    //????????????
    data.putString(WbCloudFaceContant.COLOR_MODE, color);
    //?????????????????????????????? ????????????
    data.putBoolean(WbCloudFaceContant.VIDEO_UPLOAD, isRecordVideo);
    //??????????????????????????????????????????
//        data.putBoolean(WbCloudFaceContant.ENABLE_CLOSE_EYES, isEnableCloseEyes);
    //????????????????????????????????????
    data.putBoolean(WbCloudFaceContant.PLAY_VOICE, isPlayVoice);
    //???????????????????????????  ?????????????????????????????????
    //???????????????????????? WbCloudFaceVerifySdk.ID_CRAD
    //?????????????????????  WbCloudFaceVerifySdk.SRC_IMG
    //???????????????  WbCloudFaceVerifySdk.NONE
    //??????????????????????????????
    data.putString(WbCloudFaceContant.COMPARE_TYPE, faceVerifyCompareType);
    WbCloudFaceVerifySdk.getInstance().initSdk(activity, data, new WbCloudFaceVerifyLoginListener() {

      @Override
      public void onLoginSuccess() {
        WbCloudFaceVerifySdk.getInstance().startWbFaceVerifySdk(activity, new WbCloudFaceVerifyResultListener() {

          @Override
          public void onFinish(WbFaceVerifyResult result) {
            if (result != null)
            {
              if (result.isSuccess()) {
                Log.d(TAG, "????????????! Sign=" + result.getSign() + "; liveRate=" + result.getLiveRate() +
                        "; similarity=" + result.getSimilarity() + "userImageString=" + result.getUserImageString());

                Map<String, String> temp = new HashMap<String, String>();
                temp.put("success", "1");
                temp.put("desc", result.toString());
                callbackResult.success(temp);
              } else {
                WbFaceError error = result.getError();
                if (error != null) {
                  Log.d(TAG, "???????????????domain=" + error.getDomain() + " ;code= " + error.getCode()
                          + " ;desc=" + error.getDesc() + ";reason=" + error.getReason());
                  if (error.getDomain().equals(WbFaceError.WBFaceErrorDomainCompareServer)) {
                    Log.d(TAG, "???????????????liveRate=" + result.getLiveRate() +
                            "; similarity=" + result.getSimilarity());
                  }
                } else {
                  Log.e(TAG, "sdk??????error?????????");
                }
                Map<String, String> temp = new HashMap<String, String>();
                temp.put("success", "0");
                temp.put("desc", result.toString());
                callbackResult.success(temp);
              }
            } else {
              Log.e(TAG, "sdk??????result?????????");

              Map<String, String> temp = new HashMap<String, String>();
              temp.put("success", "0");
              temp.put("desc", result.toString());
              callbackResult.success(temp);
            }
            WbCloudFaceVerifySdk.getInstance().release();
          }
        });
      }

      @Override
      public void onLoginFailed(WbFaceError error) {
        Log.i(TAG, "onLoginFailed!");
        if (error != null) {
          Log.d(TAG, "???????????????domain=" + error.getDomain() + " ;code= " + error.getCode()
                  + " ;desc=" + error.getDesc() + ";reason=" + error.getReason());
        } else {
          Log.e(TAG, "sdk??????error?????????");
        }

        Map<String, String> temp = new HashMap<String, String>();
        temp.put("success", "0");
        temp.put("desc", error.toString());
        callbackResult.success(temp);
        WbCloudFaceVerifySdk.getInstance().release();
      }
    });
  }

  private String getFaceVerifyCompareType(String type) {
    switch (type) {
      case "none":
        return WbCloudFaceContant.NONE;
      default:
        return WbCloudFaceContant.ID_CARD;
    }
  }
}
