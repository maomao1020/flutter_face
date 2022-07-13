
import 'flutter_face_platform_interface.dart';

class FlutterFace {
  Future<String?> getPlatformVersion() {
    return FlutterFacePlatform.instance.getPlatformVersion();
  }

  startFaceService({
    String faceId='',
    String order='',
    String nonce='',
    String sign='',
    String userId='',
    String appId='',
    String license='',
  }){
    return FlutterFacePlatform.instance.startFaceService(faceId: faceId,order: order,nonce: nonce,sign: sign,userId: userId,appId: appId,licence: license);
  }
}
