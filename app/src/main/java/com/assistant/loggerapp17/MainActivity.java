package com.assistant.loggerapp17;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.assistant.loggerapp17.databinding.ActivityMainBinding;

import java.io.File;

import static com.assistant.loggerapp17.Util.isExternalStorageReadable;
import static com.assistant.loggerapp17.Util.isExternalStorageWritable;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "LoggerApp17";
    private ActivityMainBinding binding;

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

            File appDirectory = new File( Environment.getExternalStorageDirectory()+ File.separator + TAG );
            File logDirectory = new File( appDirectory + File.separator + "logs" );
            Log.d(TAG, "*** onCreate() - appDirectory :: "+appDirectory.getAbsolutePath());
            Log.d(TAG, "*** onCreate() - logDirectory :: "+logDirectory.getAbsolutePath());

            //appDirectory 폴더 없을 시 생성
            if ( !appDirectory.exists() ) {
                appDirectory.mkdirs();
            }

            //logDirectory 폴더 없을 시 생성
            if ( !logDirectory.exists() ) {
                logDirectory.mkdirs();
            }

            //TODO : logcat class 생성하면서 저장할 파일 패스 전달


        } else if ( isExternalStorageReadable() ) {
            //read 만 가능
        } else {
            //접근 불가능
        }
    }

}