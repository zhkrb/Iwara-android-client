package com.zhkrb.iwara;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;


import com.zhkrb.iwara.utils.L;
import com.zhkrb.netowrk.jsoup.JsoupUtil;
import com.zhkrb.netowrk.retrofit.HttpUtil;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class AppContext extends MultiDexApplication {

    public static AppContext sInstance;
    private int mCount;
    private boolean mFront;
    public static boolean sDeBug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        HttpUtil.init(AppConfig.HOST);
        JsoupUtil.init(AppConfig.HOST);
        registerActivityLifecycleCallbacks();
    }


    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
                if (!mFront) {
                    mFront = true;
                    L.e("AppContext------->处于前台");

                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount--;
                if (mCount == 0) {
                    mFront = false;
                    L.e("AppContext------->处于后台");

                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }



}
