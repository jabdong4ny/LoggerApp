package com.assistant.loggerapp;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
     * newNameFileStr 파일 naming logcat_yyyymmddhhmmss.log
     * @param achivedPath : 저장되어야할 파일 위치
     * @return logfile : 저장되어야 파일 패스가 포함된 이름
     */
    public static String newNameFileStr(String achivedPath){
        File logFile = new File( achivedPath, "logcat_" + getCurrentDateString() + ".log" );
        Log.d(TAG, "logFile : "+logFile);
        String path = logFile.getAbsolutePath();
        String fileName = logFile.getName();
        return path;// + "/" + fileName;
    }

    /**
     * newNameFile 파일 naming logcat_yyyymmddhhmmss.log
     * @param achivedPath : 저장되어야할 파일 위치
     * @return logfile : 저장되어야 파일 패스가 포함된 file
     */
    public static File newNameFile(String achivedPath){
        File logFile = new File( achivedPath, "logcat_" + getCurrentDateString() + ".txt" );
        Log.d(TAG, "logFile : "+logFile.getAbsolutePath());
        return logFile;
    }

    /**
     * 디스크 용량 getDiskSpace
     * @param path : 용량 확인할 경로
     * @return nUsableMB : 잔여 용량 MB 단위
     */
    public static int getDiskSpaceByMB(File path) {
        //path.getTotalSpace(); total size
        if(path == null) {
            Log.e(TAG,"getDiskSpaceByMB : File path is null");
            return 0;
        }
        int nUsableMB = (int) (path.getUsableSpace() / (1024 * 1024)); //unit MB
        Log.e(TAG,"getDiskSpaceByMB : nUsableMB = "+ nUsableMB);
        return nUsableMB;
    }

    /**
     * getFileSizeMB 파일 용량
     * @param file : 용량을 확인할 파일
     * @return nUsableMB : 잔여 용량 MB 단위
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getFileSizeMB(File file) {
        if(file == null) {
            Log.e(TAG,"getFileSizeMB : File is null");
            return 0;
        }
        long bytes = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                bytes = Files.size(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bytes = file.length();
        }
        long kilobyte = bytes / 1024;
        long megabyte = kilobyte / 1024;
        //Log.d(TAG,"getFileSizeMB = "+ megabyte);
        return megabyte;
    }



    public static String toMB(long size){
        return String.valueOf((int) (size / (1024/ 1024)));
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
