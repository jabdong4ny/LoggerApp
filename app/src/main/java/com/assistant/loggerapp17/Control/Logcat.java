package com.assistant.loggerapp17.Control;

import android.util.Log;

import com.assistant.loggerapp17.Util;

import java.io.File;
import java.io.IOException;

public class Logcat {
    public static final String TAG = Logcat.class.getSimpleName();
    Thread thread = null;
    String logFileName = null;
    String archivedPath = null;

    public Logcat(String archivedPath) {
        Log.d(TAG, "create !!");
        this.archivedPath = archivedPath;

        //이전 logcat 을 지우고 파일에 새 로그을 씀
        try {
            Process process = Runtime.getRuntime().exec("logcat -v time");
            //process = Runtime.getRuntime().exec("logcat -f " + logFile);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void process(){
    LogCatThread loggerT = new LogCatThread(archivedPath);
    thread = new Thread(loggerT);
    if(thread != null)
    {
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
        //스레드 멈추고 싶으면
        thread.interrupt();
        }
    }

    public void stopProcess(){
        if(thread != null) {
            thread.interrupt();
        }
        else
        {
            Log.e(TAG,"thread is null!!");
        }
    }


}
class LogCatThread implements Runnable {
    String archivedPath = null;

    public LogCatThread(String archivedPath) {
        Log.d(Logcat.TAG, "LogCatThread() : " + archivedPath);
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()){
                Log.d(Logcat.TAG,"Thread is aLive ...");
                File a = Util.newNameFile(archivedPath);
                //최초 파일명 갱신
                while(!Thread.currentThread().isInterrupted()){
                    //100넘는지 확인
                        //100 넘으면 파일복사 ( 어디로 복사할지 얻어야 함)
                        //파일명 갱신
                        //raw 파일 비우기
                        //new FileOutputStream(FILE_PATH).close();
                    //한줄씩 저장
                    Thread.sleep(5000);
                }
                //
                Thread.sleep(5000);
            }
        } catch (Exception e){

        } finally {
            Log.d(Logcat.TAG,"Thread is Dead ...");
            //100 파일복사
        }
    }
}