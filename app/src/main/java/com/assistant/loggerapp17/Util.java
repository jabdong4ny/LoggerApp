package com.assistant.loggerapp17;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    static public final String TAG = Util.class.getSimpleName();

    public static String getCurrentDateString(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String time = format.format(date);
        return time;
    }

    /**
     * newNameFile 파일 naming logcat_yyyymmddhhmmss.log
     * @param achivedPath : 저장되어야할 파일 위치
     * @return logfile : 저장되어야 위치와 파일
     */
    public static File newNameFile(String achivedPath){
        File logFile = new File( achivedPath, "logcat_" + getCurrentDateString() + ".log" );
        Log.d(TAG, "logFile : "+logFile);
        return logFile;
    }


    /**
     * 외부저장소 read/write 가능 여부 확인
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    /**
     * 외부저장소 read 가능 여부 확인
     * @return
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }
}
