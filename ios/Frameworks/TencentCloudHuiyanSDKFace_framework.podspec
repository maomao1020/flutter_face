Pod::Spec.new do |s|
  s.name = "TencentCloudHuiyanSDKFace_framework"
  s.version = "6.5.4"
  s.summary = "A short description of TencentCloudHuiyanSDKFace_framework."
  s.homepage         = 'https://github.com/brownfeng/TencentCloudHuiyanSDKFace_framework'
  s.license          =  "MIT"
  s.authors = {"brownfeng"=>"brownfeng@github.com"}
  s.description = "TODO: Add long description of the pod here."
  s.frameworks = ["UIKit", "AVFoundation", "CoreVideo", "Security", "SystemConfiguration", "CoreMedia", "VideoToolbox", "CoreTelephony", "ImageIO"]
  s.libraries = ["c++","z"]
  s.source = { :path => '.' }

  s.ios.deployment_target    = '9.0'
  s.ios.vendored_framework   = 'Libs/*.framework'
  s.ios.resource = 'Resources/*.bundle'
end
