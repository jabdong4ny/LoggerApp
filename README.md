# LoggerApp
. Android 에서 별도 로그를 취득할 목적의 앱

 1) 전체로그를 취득하기 위해선 몇 가지 제약이 있다.<p>
  (1) 전재조건 : rooting 된 device 이여야만 가능하다.<p>
  (2) apk 는 시스템app 으로 실행되어야 log process와 정보를 공유를 할 수 있기에 /system/app 경로에 넣어야 한다.<p>
  . 설치 방법<p>
   - adb remount
   - adb shell chmod 755 /system/app
   - adb shell loggerApp.apk /system/app
 
 2) android API 버전에 따라 파일 생성 경로<p>
  (1) Build.VERSION.SDK_INT >= 29 이면 <p>
   - /storage/emulated/0/Android/data/com.assistant.loggerapp/files/ 에 생성된다.<p>
  (2) 29 미만의 경우에는 로그파일에 생성할 경로를 지정해야 한다.<p>
   - MainActivity.java 에 sPath 에 파일경로 지정필요!<p>
