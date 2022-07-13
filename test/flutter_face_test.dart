import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_face/flutter_face.dart';
import 'package:flutter_face/flutter_face_platform_interface.dart';
import 'package:flutter_face/flutter_face_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterFacePlatform 
    with MockPlatformInterfaceMixin
    implements FlutterFacePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterFacePlatform initialPlatform = FlutterFacePlatform.instance;

  test('$MethodChannelFlutterFace is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterFace>());
  });

  test('getPlatformVersion', () async {
    FlutterFace flutterFacePlugin = FlutterFace();
    MockFlutterFacePlatform fakePlatform = MockFlutterFacePlatform();
    FlutterFacePlatform.instance = fakePlatform;
  
    expect(await flutterFacePlugin.getPlatformVersion(), '42');
  });
}
