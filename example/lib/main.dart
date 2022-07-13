import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_face/flutter_face.dart';
import 'package:http/http.dart' as http;


void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _flutterFacePlugin = FlutterFace();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _flutterFacePlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: GestureDetector(
          onTap: () async
          {
            Uri url      = Uri.http('testapi.52mutou.com', '/api/test/getFaceId');
            var response = await http.get(url);
            print(response.body);
            Map<String,dynamic> body  = jsonDecode(response.body);
            print(body['code']);
            if(body['code'].toString()=='000')
            {
              String faceId = body['data']['faceid'];
              String nonce  = body['data']['nonce'];
              String sign   = body['data']['sign'];
              String appId  = body['data']['appid'];
              String order  = body['data']['orderno'];
              String licence= body['data']['licence'];
              String userId = body['data']['userid'];
              await _flutterFacePlugin.startFaceService(
                  faceId:faceId,
                  order: order,
                  nonce: nonce,
                  sign: sign,
                  userId: userId,
                  appId: appId,
                  license: licence
              );
            }
          },
          child: Center(
            child: Text('Running on: $_platformVersion\n'),
          ),
        ),
      ),
    );
  }
}
