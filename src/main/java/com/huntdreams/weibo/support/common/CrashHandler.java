package com.huntdreams.weibo.support.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 应用程序异常处理类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/28.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{

    public static String CRASH_DIR = Environment.getExternalStorageDirectory().getPath()+"/"+AppConfig.APP_NAME+"/";
    public static String CRASH_LOG = CRASH_DIR + "last_crash.log";
    public static String CRASH_TAG = CRASH_DIR + ".crashed";

    private static String ANDROID = Build.VERSION.RELEASE;
    private static String MODEL = Build.MODEL;
    private static String MANUFACTURER = Build.MANUFACTURER;

    public static String VERSION = "Unknown";
    private Thread.UncaughtExceptionHandler mPrevious;

    public static void init(Context context){
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            VERSION = info.versionName + info.versionCode;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void register(){new CrashHandler();}

    private CrashHandler(){
        mPrevious = Thread.currentThread().getUncaughtExceptionHandler();
        Thread.currentThread().setUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if("Your Fault".equals(ex.getMessage())){
            mPrevious.uncaughtException(thread,ex);
            return;
        }
        // Create the folder
        File file = new File(CRASH_LOG);
        if(file.exists()){
            file.delete();
        }else{
            try{
                new File(CRASH_DIR).mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Write messages
        PrintWriter p = null;
        try{
            p = new PrintWriter(file);
            p.write("Android Version: " + ANDROID + "\n");
            p.write("Device Model: " + MODEL + "\n");
            p.write("Device Manufacturer: " + MANUFACTURER + "\n");
            p.write("App Version: " + VERSION + "\n");
            p.write("*********************\n");
            ex.printStackTrace(p);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            p.close();
        }

        try{
            new File(CRASH_TAG).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mPrevious != null){
            mPrevious.uncaughtException(thread,ex);
        }
    }
}
