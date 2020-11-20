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

package com.zhkrb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.zhkrb.iwara.AppContext;

import java.util.Map;

/**
 * Created by cxf on 2018/9/17.
 * SharedPreferences 封装
 */

public class SpUtil {

    public static final String SECURITY_ENABLE = "security_enable";
    public static final String SAFEMODE = "safemode";
    public static final String LANGUAGE = "language";
    public static final String INDEX_VIDEO_LIST_MODE = "indexvideolistmode";
    public static final String VIEW_VIDEO_LIST_MODE = "indexvideolistmode";
    public static final String LIKE_VIDEO_LIST_MODE = "indexvideolistmode";
    public static final String GALLERY_MODE = "gallery_mode";
    public static final String SHOW_LIKE_BG = "show_like_bg";
    public static final String VERSION_CODE = "version_code";
    public static final String IGNORE_VERSION = "ignore_version";


    private static SpUtil sInstance;
    private SharedPreferences mSharedPreferences;


    private SpUtil() {
        mSharedPreferences = AppContext.sInstance.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }

    public static SpUtil getInstance() {
        if (sInstance == null) {
            synchronized (SpUtil.class) {
                if (sInstance == null) {
                    sInstance = new SpUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存一个字符串
     */
    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取一个字符串
     */
    public String getStringValue(String key) {
        return mSharedPreferences.getString(key, "");
    }

    /**
     * 保存一个数字
     */
    public void setIntValue(String key,int value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    /**
     * 获取数字
     */
    public int getIntValue(String key,int defValue){
        return mSharedPreferences.getInt(key,defValue);
    }

    /**
     * 保存多个字符串
     */
    public void setMultiStringValue(Map<String, String> pairs) {
        if (pairs == null || pairs.size() == 0) {
            return;
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Map.Entry<String, String> entry : pairs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                editor.putString(key, value);
            }
        }
        editor.apply();
    }

    /**
     * 获取多个字符串
     */
    public String[] getMultiStringValue(String... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        int length = keys.length;
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            String temp = "";
            if (!TextUtils.isEmpty(keys[i])) {
                temp = mSharedPreferences.getString(keys[i], "");
            }
            result[i] = temp;
        }
        return result;
    }


    /**
     * 保存一个布尔值
     */
    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获取一个布尔值
     */
    public boolean getBooleanValue(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    /**
     * 保存多个布尔值
     */
    public void setMultiBooleanValue(Map<String, Boolean> pairs) {
        if (pairs == null || pairs.size() == 0) {
            return;
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Map.Entry<String, Boolean> entry : pairs.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            if (!TextUtils.isEmpty(key)) {
                editor.putBoolean(key, value);
            }
        }
        editor.apply();
    }

    /**
     * 获取多个布尔值
     */
    public boolean[] getMultiBooleanValue(String[] keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        int length = keys.length;
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            boolean temp = false;
            if (!TextUtils.isEmpty(keys[i])) {
                temp = mSharedPreferences.getBoolean(keys[i], false);
            }
            result[i] = temp;
        }
        return result;
    }


    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().apply();
    }

    public void removeValue(String... keys) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    public void setLongValue(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key,value);
        editor.apply();
    }

    public long getLongValue(String key,long defValue){
        return mSharedPreferences.getLong(key,defValue);
    }
}
