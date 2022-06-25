package com.assistant.loggerapp.Control;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.assistant.loggerapp.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Logcat {
    public static final String TAG = Logcat.class.getSimpleName();
    Thread thread = null;
    String logFileName = null;
    String archivedPath = null;
    InputStream is;

    public Logcat(String archivedPath) {
        Log.d(TAG, "create !!");
        this.archivedPath = archivedPath;

        //이전 logcat 을 지우고 파일에 새 로그을 씀
//        try {
//            String sCmd = "logcat -v time";// > " + archivedPath +"/logcat.log";
//            Process process = Runtime.getRuntime().exec(sCmd);
//            is = process.getInputStream();
//
//        } catch ( IOException e ) {
//            e.printStackTrace();
//        }
    }

    public void start() {

    LogCatThread loggerT = new LogCatThread(archivedPath);
    thread = new Thread(loggerT);
    if(thread != null)
    {
        thread.setDaemon(true);
        thread.start();
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        //스레드 멈추고 싶으면
        //thread.interrupt();
        }
    }

    public void logcatStop(){
        if(thread != null) {
            thread.interrupt();
            Log.d(TAG,"logcatStop()!!");
        }
        else
        {
            Log.e(TAG,"thread is null!!");
        }
    }
}

class LogCatThread implements Runnable {
    String archivedPath = null;
    Process process = null;

    public LogCatThread(String archivedPath) {
        Log.d(Logcat.TAG, "LogCatThread() : " + archivedPath);
        this.archivedPath = archivedPath;
    }

    public void onProcessDestroy(){
        if(process != null){
            process.destroy();
            Log.d(Logcat.TAG, "process.destory()!!");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run() {

        try {
            //파일명 갱신
            String line;
            //TODO : 저장할 파일
            File fileName = Util.newNameFile(archivedPath);
            try {
                String sCmd = "logcat -v threadtime";// > " + archivedPath +"/logcat.log";
                //String sCmd = "logcat -f " + fileName;// > " + archivedPath +"/logcat.log";
                //String sCmd = "logcat -d";// > " + archivedPath +"/logcat.log";
                process = Runtime.getRuntime().exec(sCmd);

            } catch ( IOException e ) {
                e.printStackTrace();
            }

            FileWriter fw = new FileWriter(fileName.getAbsolutePath(),true);
            BufferedWriter bw = new BufferedWriter(fw);

            /*while (!Thread.currentThread().isInterrupted())*/{
                Log.d(Logcat.TAG,"Thread is aLive ...");

                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));


                //if(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))){
                int nMB = 0;
                //최초 파일명 갱신
                while((!Thread.currentThread().isInterrupted())
                        &&((line = br.readLine()) != null)
                ){
                    //100넘는지 확인
                    nMB = (int)Util.getFileSizeMB(fileName);
                    //남은 공간 확인
                    //nMB = Util.getDiskSpaceByMB(fileName);

                    //10 넘으면 파일복사 ( 어디로 복사할지 얻어야 함)
                    if(nMB >= 10)
                    {
                        Log.d(Util.TAG,"over 10 MB !!");

                        File newFile = new File(Util.newNameFileStr(archivedPath)); //생성해야하는 파일 위치 : archivedPath
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Files.copy(fileName.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            // 1. 원본 File, 복사할 File 준비
                            // 2. FileInputStream, FileOutputStream 준비
                            FileInputStream input = new FileInputStream(fileName);
                            FileOutputStream output = new FileOutputStream(newFile);
                            // 3. 한번에 read하고, write 할 사이즈 지정
                            byte[] buf = new byte[1024];
                            // 4. buf 사이즈만큼 input에서 데이터를 읽어서, output에 쓴다.
                            int readData;
                            while ((readData = input.read(buf)) > 0 ) {
                                output.write(buf, 0, readData);
                            }
                            // 5. Stream close
                            //input.close();
                            output.close();
                        }
                        //raw 파일 비우기
                        new FileOutputStream(fileName).close();
                    }

                    //한줄씩 저장
                    bw.append(line);
                    bw.newLine();
                    //Log.d(Logcat.TAG, line); //debug log
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
                }
                Log.d(Logcat.TAG,"buffer read finish ...");
                bw.flush();
                bw.close();
                onProcessDestroy();
            }
        } catch (IOException e){
            Log.d(Logcat.TAG,"Thread is Dead ... by Exception");
            e.printStackTrace();
            onProcessDestroy();
        } finally {
            Log.d(Logcat.TAG,"Thread is Dead ...");
            onProcessDestroy();
        }
    }
}