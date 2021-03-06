package com.zhkrb.utils;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * @description：全局异常捕获
 * @author：zhkrb
 * @DATE： 2020/12/5 13:53
 */
public class CrashHandlerUtil implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "Crash";

    private static CrashHandlerUtil sInstance;

    private final Thread.UncaughtExceptionHandler defaultHandler;

    public static CrashHandlerUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CrashHandlerUtil.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandlerUtil(context);
                }
            }
        }
        return sInstance;
    }

    public CrashHandlerUtil(Context context) {
        L.e(TAG, "线程初始化");
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if (!handleException(e) && defaultHandler != null) {
            //延迟3秒保证文件写入
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                L.e(TAG, "exception : " + ex.getClass().toString() + ": " + ex.getLocalizedMessage());
                e.printStackTrace();
            }
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(t, e);

        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                L.e(TAG, "exception : " + ex.getClass().toString() + ": " + ex.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    /**
     * 处理异常方法，如果没有处理，返回false交由系统默认方法处理
     *
     * @param e
     * @return
     */
    private boolean handleException(Throwable e) {
        L.e(TAG, "-----------↓↓↓↓----------CRASH!!!!-----------↓↓↓↓----------");
        L.e(TAG, "exception : " + e.getClass().toString() + ": " + e.getLocalizedMessage());
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements) {
            L.e(TAG, "stackTrace : " + element.toString());
        }
        L.e(TAG, "-----------↑↑↑↑----------CRASH!!!!-----------↑↑↑↑----------");
        return false;
    }


}
