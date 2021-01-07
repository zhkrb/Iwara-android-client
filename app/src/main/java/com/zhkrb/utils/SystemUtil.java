package com.zhkrb.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;


import com.zhkrb.iwara.AppContext;

import androidx.annotation.NonNull;

import static android.content.Context.CLIPBOARD_SERVICE;


public class SystemUtil {

    public static void openUrl(@NonNull Activity activity, String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * 判断是否是debug
     *
     * @return
     */
    public static boolean isDebug() {
        try {
            ApplicationInfo info = AppContext.sInstance.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 复制文本
     *
     * @param str 文本内容
     */
    public static void copy(String str) {
        ClipboardManager myClipboard = (ClipboardManager) AppContext.sInstance.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", str);
        myClipboard.setPrimaryClip(clipData);
    }


    /**
     * 启动服务
     *
     * @param intent
     */
    public static void startService(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppContext.sInstance.startForegroundService(intent);
        } else {
            AppContext.sInstance.startService(intent);
        }
    }

    /**
     * 获取版本号
     *
     * @return long 版本号
     */
    public static long getVersion(){
        long version = 0;
        try {
            PackageInfo packageInfo = AppContext.sInstance.getPackageManager().getPackageInfo(
                    AppContext.sInstance.getPackageName(),0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                version = packageInfo.getLongVersionCode();
            }else {
                version = packageInfo.versionCode;
            }
            L.e("getVersion: "+ version);
            L.e("getVersionName: " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            L.e("getVersionErr: "+e.getMessage());
        }
        return version;
    }


    /**
     * 点击间隔时间
     */
    public static final int CLICK_DURATION = 100;
    private static long lastClickTime;

    public static boolean canClick(){
        long currentTime = System.currentTimeMillis();
        long timeInterval = currentTime - lastClickTime;
        if (timeInterval > CLICK_DURATION) {
            lastClickTime = currentTime;
            return true;
        }
        return false;
    }

}
