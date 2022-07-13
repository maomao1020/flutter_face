package com.u52mutou.flutter_face_example;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.cloud.huiyansdkface.facelight.api.WbCloudFaceContant;
import com.tencent.cloud.huiyansdkface.facelight.api.WbCloudFaceVerifySdk;
import com.tencent.cloud.huiyansdkface.facelight.api.listeners.WbCloudFaceVerifyLoginListener;
import com.tencent.cloud.huiyansdkface.facelight.api.listeners.WbCloudFaceVerifyResultListener;
import com.tencent.cloud.huiyansdkface.facelight.api.result.WbFaceError;
import com.tencent.cloud.huiyansdkface.facelight.api.result.WbFaceVerifyResult;
import com.tencent.cloud.huiyansdkface.facelight.process.FaceVerifyStatus;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {

    private static final String TAG = "MainActivity";
    private static final String FACE_CHANNEL = "flutter_face";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(),FACE_CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                if (call.method.equals("getPlatformVersion")) {
                    result.success("Android " + android.os.Build.VERSION.RELEASE);
                }else if(call.method.equals("startFaceService")){
                    String faceId = call.argument("faceId");
                    String orderNo = call.argument("order");
                    String nonce = call.argument("nonce");
                    String sign = call.argument("sign");
                    String userId = call.argument("userId");
                    String appId = call.argument("appId");
                    //活体类型：光线活体/动作活体
                    String mode = call.argument("mode");
                    //对比类型：身份证/自带对比源/仅活体
                    String type = call.argument("type");
                    String licence = call.argument("licence");

                    Map<String, String> config = call.argument("config");

                    openCloudFaceService(faceId, orderNo, appId, nonce, userId, sign, mode, type, licence, config, result);
                }
            }
        });
    }

    private void openCloudFaceService(String faceId, String order, String appId, String nonce, String userId, String sign, String mode, String type, String keyLicence, Map<String, String> config, final MethodChannel.Result callbackResult) {
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
        //是否展示刷脸成功页面，默认展示
        data.putBoolean(WbCloudFaceContant.SHOW_SUCCESS_PAGE, isShowSuccess);
        //是否展示刷脸失败页面，默认展示
        data.putBoolean(WbCloudFaceContant.SHOW_FAIL_PAGE, isShowFail);
        //颜色设置
        data.putString(WbCloudFaceContant.COLOR_MODE, color);
        //是否需要录制上传视频 默认需要
        data.putBoolean(WbCloudFaceContant.VIDEO_UPLOAD, isRecordVideo);
        //是否开启闭眼检测，默认不开启
//        data.putBoolean(WbCloudFaceContant.ENABLE_CLOSE_EYES, isEnableCloseEyes);
        //是否播放提示音，默认播放
        data.putBoolean(WbCloudFaceContant.PLAY_VOICE, isPlayVoice);
        //设置选择的比对类型  默认为公安网纹图片对比
        //公安网纹图片比对 WbCloudFaceVerifySdk.ID_CRAD
        //自带比对源比对  WbCloudFaceVerifySdk.SRC_IMG
        //仅活体检测  WbCloudFaceVerifySdk.NONE
        //默认公安网纹图片比对
        data.putString(WbCloudFaceContant.COMPARE_TYPE, faceVerifyCompareType);
        WbCloudFaceVerifySdk.getInstance().initSdk(MainActivity.this, data, new WbCloudFaceVerifyLoginListener() {

            @Override
            public void onLoginSuccess() {
                WbCloudFaceVerifySdk.getInstance().startWbFaceVerifySdk(MainActivity.this, new WbCloudFaceVerifyResultListener() {

                    @Override
                    public void onFinish(WbFaceVerifyResult result) {
                        if (result != null)
                        {
                            if (result.isSuccess()) {
                                Log.d(TAG, "刷脸成功! Sign=" + result.getSign() + "; liveRate=" + result.getLiveRate() +
                                        "; similarity=" + result.getSimilarity() + "userImageString=" + result.getUserImageString());

                                Map<String, String> temp = new HashMap<String, String>();
                                temp.put("success", "1");
                                temp.put("desc", result.toString());
                                callbackResult.success(temp);
                            } else {
                                WbFaceError error = result.getError();
                                if (error != null) {
                                    Log.d(TAG, "刷脸失败！domain=" + error.getDomain() + " ;code= " + error.getCode()
                                            + " ;desc=" + error.getDesc() + ";reason=" + error.getReason());
                                    if (error.getDomain().equals(WbFaceError.WBFaceErrorDomainCompareServer)) {
                                        Log.d(TAG, "对比失败，liveRate=" + result.getLiveRate() +
                                                "; similarity=" + result.getSimilarity());
                                    }
                                } else {
                                    Log.e(TAG, "sdk返回error为空！");
                                }
                                Map<String, String> temp = new HashMap<String, String>();
                                temp.put("success", "0");
                                temp.put("desc", result.toString());
                                callbackResult.success(temp);
                            }
                        } else {
                            Log.e(TAG, "sdk返回result为空！");

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
                    Log.d(TAG, "登录失败！domain=" + error.getDomain() + " ;code= " + error.getCode()
                            + " ;desc=" + error.getDesc() + ";reason=" + error.getReason());
                } else {
                    Log.e(TAG, "sdk返回error为空！");
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