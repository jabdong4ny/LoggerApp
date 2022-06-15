package com.assistant.loggerapp17;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.assistant.loggerapp17.Control.Logcat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.assistant.loggerapp17.databinding.ActivityMainBinding;

import java.io.File;

import static com.assistant.loggerapp17.Util.isExternalStorageReadable;
import static com.assistant.loggerapp17.Util.isExternalStorageWritable;

import io.hamed.floatinglayout.callback.FloatingListener;
import io.hamed.floatinglayout.FloatingLayout;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "LoggerApp17";
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private ActivityMainBinding binding;
    private Logcat logcat;

    private FloatingLayout floatingLayout = null;
    private FloatingListener floatingListener = new FloatingListener() {
        @Override
        public void onCreateListener(View view) {
            Button btn = view.findViewById(R.id.btn_close);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    floatingLayout.destroy();
                    floatingLayout = null;
                }
            });

            Button minimum = view.findViewById(R.id.btn_collapse_screen);
            minimum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    floatingLayout.destroy();
                    floatingLayout = null;
                    onFloatingMinimum();
                }
            });
        } // floating layout 내부 레이아웃 설정

        @Override
        public void onCloseListener() {
            Toast.makeText(getApplicationContext(), "Close or Minimum", Toast.LENGTH_SHORT).show();
        }
    };

    private FloatingListener floatingListener_minimum = new FloatingListener() {
        @Override
        public void onCreateListener(View view) {
            Button maximum = view.findViewById(R.id.btn_maximum_screen);
            maximum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    floatingLayout.destroy();
                    floatingLayout = null;
                    showFloating();
                }
            });
        } // floating layout 내부 레이아웃 설정

        @Override
        public void onCloseListener() {
            Toast.makeText(getApplicationContext(), "Maximum", Toast.LENGTH_SHORT).show();
        }
    };
    private boolean isNeedPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this);
    } // 다른 앱 위에서 작동이 가능하게 허용

    // floating layout 실행
    private void showFloating() {
        if(floatingLayout == null) {
            floatingLayout = new FloatingLayout(this, R.layout.float_layout);
            floatingLayout.setFloatingListener(floatingListener);
            floatingLayout.create();
        }
    }

    // floating layout maximum
//    private void onFloatingMaximum() {
//        floatingLayout = new FloatingLayout(this, R.layout.float_layout);
//        floatingLayout.setFloatingListener(floatingListener);
//        floatingLayout.create();
//    }

    // floating layout minimum
    private void onFloatingMinimum() {
        if(floatingLayout == null) {
            floatingLayout = new FloatingLayout(this, R.layout.float_layout_minimum);
            floatingLayout.setFloatingListener(floatingListener_minimum);
            floatingLayout.create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //local code
        //TODO 파일 위치 지정
        //저장 시작, 중지, 앱 삭제
        if ( isExternalStorageWritable() ) {
            //read, write 둘다 가능
            requestPermission(); //from oreo


        } else if ( isExternalStorageReadable() ) {
            //read 만 가능
        } else {
            //접근 불가능
        }
    }

    public void onClick(View view) {
        Log.d(TAG, "*** onClick()");
        switch (view.getId()) {
            case R.id.btn_open:
                if (!isNeedPermission())
                    showFloating();
                break;
        }
    }

    //TODO : app 종료
    //device.shell("am force-stop <package name>")

    private void logcatStart(File logDirectory){
        //TODO : logcat class 생성하면서 저장할 파일 패스 전달
        logcat = new Logcat(logDirectory.getAbsolutePath());
        logcat.process();      
    }
    
    private void makeDir(){
        File logDirectory = null;
        File appDirectory = null;
        String appDirPath = null;
        String logDirPath = null;

        if( Build.VERSION.SDK_INT < 29) {
            appDirPath = Environment.getExternalStorageDirectory()+ File.separator + TAG;
            appDirectory = new File(appDirPath);
            logDirPath = appDirectory + File.separator + "logs";
            logDirectory = new File(logDirPath);
            Log.d(TAG, "*** onCreate() - appDirectory :: "+appDirectory.getAbsolutePath());
            //appDirectory 폴더 없을 시 생성
            if ( !appDirectory.exists() ) {
                if(!appDirectory.mkdirs()){
                    Log.e(TAG,"it couldn't be make directory!!1");
                    Log.e(TAG,"end");
                    return ;
                }
            }
        }
        else {
            logDirectory = MainActivity.this.getExternalFilesDir("/logs");
        }
        //File logDirectory = new File(logDirPath);
        Log.d(TAG, "*** onCreate() - logDirectory :: "+logDirectory.getAbsolutePath());


        //logDirectory 폴더 없을 시 생성
        if ( !logDirectory.exists() ) {
            if(!logDirectory.mkdirs()){
                Log.e(TAG,"it couldn't be make directory!!2");
                Log.e(TAG,"end");
                return ;
            }
        }
        logcatStart(logDirectory);
    }

    //from oreo
    private void requestPermission() {
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);//사용자가 이전에 거절한적이 있어도 true 반환

        if (shouldProviceRationale) {
            //앱에 필요한 권한이 없어서 권한 요청
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            //권한있을때.
            //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
            //makeDir();
        }
        if(isNeedPermission()) {
            // overlay permission 요청
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivityForResult(intent, 25);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //권한 허용 선택시
                    //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
                    makeDir();
                } else {
                    //사용자가 권한 거절시
                    denialDialog();
                }
                return;
            }
        }
    }

    public void denialDialog() {
        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent); //확인버튼누르면 바로 어플리케이션 권한 설정 창으로 이동하도록
                    }
                })
                .create()
                .show();
    }
}