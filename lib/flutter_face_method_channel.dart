import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_face_platform_interface.dart';

/// An implementation of [FlutterFacePlatform] that uses method channels.
class MethodChannelFlutterFace extends FlutterFacePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_face');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  startFaceService({String faceId='',String order='',String nonce='',String sign='',String userId='',String appId='',String licence=''}) async {
    final Map<String, dynamic> config = {
      'faceId': faceId,
      'order': order,
      'nonce': nonce,
      'sign': sign,
      'userId': userId,
      'appId': appId,
      'mode':'reflect',
      'type':'idCard',
      'licence': licence,
      'config': {
        'showSuccessPage': '0', //是否展示成功页面
        'showFailurePage': '0', //是否展示失败页面
        'recordVideo': '0', //是否录制视频
        'playVoice': '0', //是否播放语音提示
        'detectCloseEyes': '0', //是否检测用户闭眼
        'theme': '0', //sdk皮肤设置，0黑色，1白色
      }
    };
    final res = await methodChannel.invokeMethod('startFaceService',config);
    return res;
  }
}
