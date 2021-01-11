package com.qiyei.android.common;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.qiyei.android.common.util.AndroidUtil;
import com.qiyei.android.common.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Created by qiyei2015 on 2017/5/7.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 崩溃时异常处理器
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /**
     * 调试标志
     */
    private final static String TAG = "CrashHandler";

    private static final String CRASH_FILE = "crash_file";
    /**
     * 默认的应用信息
     */
    private static final String APP_INFO_NAME = "DeviceInfo.txt";

    /**
     * 默认的线程ExceptionHandler
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static class SingleHolder{
        private final static CrashHandler sHandler = new CrashHandler();
    }

    /**
     * 构造方法私有化
     */
    private CrashHandler(){
    }

    /**
     * 内部类的方式提供单例
     * @return
     */
    public static CrashHandler getInstance(){
        return SingleHolder.sHandler;
    }

    /**
     * 初始化，一般在Application中调用
     */
    public void init(){
        //设置全局的异常处理类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        //获取当前线程默认的ExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(TAG,e.getMessage());

        //写入到本地文件 e 当前应用版本 手机信息

        // 1. 崩溃的详细信息
        // 2. 应用信息 包名 版本号
        // 3. 手机信息 系统版本 手机型号 内存
        // 4. 保存当乞丐了文件，等应用再次启动再上传(上传文件不在这里处理)
        // TODO: 2021/1/11 需要写入文件中 
        String crashFile = writeExceptionToFile(e);
        Log.e(TAG,"crashFile --> " + crashFile);
        // 缓存崩溃日志文件
        cacheCrashFile(crashFile);
        uploadCrashLog();
        //让系统默认处理器处理
        mDefaultHandler.uncaughtException(t,e);
    }

    /**
     * 上传crash日志到服务器
     */
    private void uploadCrashLog(){


    }

    /**
     * 写默认的应用信息
     */
    public static void writeAppInfo(){
        //默认存储在 包名 + log 目录下
        File dir = new File(AndroidUtil.getExternalDataPath() + File.separator + "log");
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir,APP_INFO_NAME);
        //如果文件存在，表明已经存储过了，不必再写
        if (file.exists()){
            return;
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(file,true),true);
            StringBuilder sb = new StringBuilder();
            //设备信息 + 应用信息
            for (Map.Entry<String,String> entry : RuntimeEnv.getAppInfo().entrySet()){
                sb.append(entry.getKey() + " = ").append(entry.getValue()).append("\n");
            }
            Log.i(TAG,"AppInfo --> " + sb.toString());
            printWriter.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            printWriter.close();
        }
    }

    /**
     * 保存获取的 软件信息，设备信息，异常信息到存储设备中
     * @param e
     * @return
     */
    public static String writeExceptionToFile(Throwable e) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        //添加时间,进程等信息 打印进程ID 线程ID
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = sf.format(new Date());
        String message = time + " "
                + ""+ android.os.Process.myPid() + "|" +""+ android.os.Process.myTid()
                + "[" + RuntimeEnv.getCurrentFileName() + "->" + RuntimeEnv.getCurrentMethodName()+"]"
                + " ";
        sb.append(message);

        //崩溃的详细信息
        sb.append(getExceptionInfo(e));

        Log.d(TAG,"writeExceptionToFile --> " + sb.toString());

        //保存文件 手机应用目录
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            //文件目录 包名/log/crash/
            File dir = new File(AndroidUtil.getExternalDataPath() + File.separator  + "log" + File.separator + "crash" );
            //文件存在
            if (dir.exists()){
                FileUtil.deleteFile(dir);
            }
            //重新创建
            if (!dir.exists()){
                dir.mkdir();
            }
            //异常信息文件格式 时间 + .log
            fileName = dir.toString() + File.separator + getFormatTime("yyyy-MM-dd HH-mm-ss.SSS") + "_crash.log";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }finally {
                try {
                    if (fos != null){
                        fos.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return fileName;
    }

    /**
     * 获取CrashFile
     * @return
     */
    public static File getCrashFile(){
        String fileName = RuntimeEnv.appContext.getSharedPreferences(RuntimeEnv.appContext.getPackageName(), Context.MODE_PRIVATE)
                .getString(CRASH_FILE,"");
        return new File(fileName);
    }

    /**
     * 缓存crashFile文件
     * @param fileName
     */
    public static void cacheCrashFile(String fileName) {
        SharedPreferences sp = RuntimeEnv.appContext.getSharedPreferences(RuntimeEnv.appContext.getPackageName(),Context.MODE_PRIVATE);
        sp.edit().putString(CRASH_FILE,fileName).commit();
    }

    /**
     * 获取系统未捕捉的错误信息
     * @param throwable
     * @return
     */
    public static String getExceptionInfo(Throwable throwable){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);

        //原因
        Throwable cause = throwable.getCause();
        while (cause != null){
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * 获取格式化时间
     * @param formatStr
     * @return
     */
    private static String getFormatTime(String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr);
        return df.format(System.currentTimeMillis());
    }
}
