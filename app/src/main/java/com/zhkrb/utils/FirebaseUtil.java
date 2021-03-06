/*
 *
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
 * Create by zhkrb on 2021-03-06 11:03:22
 *
 */

package com.zhkrb.utils;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseUtil {

    private static FirebaseUtil sInstance;

    private static FirebaseAnalytics mAnalytics;

    public static FirebaseUtil getInstance() {
        if (sInstance == null) {
            synchronized (FirebaseUtil.class) {
                if (sInstance == null) {
                    sInstance = new FirebaseUtil();
                }
            }
        }
        return sInstance;
    }

    private FirebaseUtil() {
    }


    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        mAnalytics = FirebaseAnalytics.getInstance(context);
    }

    /**
     * 是否启用信息收集
     * @param enable
     */
    public void setAnalyticsEnable(boolean enable){
        mAnalytics.setAnalyticsCollectionEnabled(enable);
    }


}
