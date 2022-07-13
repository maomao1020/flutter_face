import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_face_method_channel.dart';

abstract class FlutterFacePlatform extends PlatformInterface {
  /// Constructs a FlutterFacePlatform.
  FlutterFacePlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterFacePlatform _instance = MethodChannelFlutterFace();

  /// The default instance of [FlutterFacePlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterFace].
  static FlutterFacePlatform get instance => _instance;
  
  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterFacePlatform] when
  /// they register themselves.
  static set instance(FlutterFacePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  startFaceService({String faceId='',String order='',String nonce='',String sign='',String userId='',String appId='',String licence=''}) {
    throw UnimplementedError('startFaceService() has not been implemented.');
  }
}
