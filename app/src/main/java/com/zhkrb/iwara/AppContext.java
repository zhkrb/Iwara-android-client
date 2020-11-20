/*
 * Copyright zhkrb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Create by zhkrb on 2019/9/7 22:12
 */

package com.zhkrb.iwara;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;


import com.zhkrb.utils.L;
import com.zhkrb.netowrk.jsoup.JsoupUtil;
import com.zhkrb.netowrk.retrofit.HttpUtil;

import java.util.concurrent.atomic.AtomicInteger;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class AppContext extends MultiDexApplication {

    public static AppContext sInstance;
    private final AtomicInteger mCount = new AtomicInteger(0);
    private boolean mFront;
    private Handler mHandler;
    private boolean isFirstStart = true;
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
                mCount.getAndIncrement();
                if (!mFront) {
                    mFront = true;
                    L.e("AppContext------->处于前台");
                    if (!isFirstStart()) {
                        isFirstStart = false;
                    }
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
                mCount.getAndDecrement();
                if (mCount.get() == 0) {
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

    public Handler getMainHandler() {
        return mHandler;
    }

    public boolean isFront() {
        return mFront;
    }

    public boolean isFirstStart() {
        return isFirstStart;
    }
}
