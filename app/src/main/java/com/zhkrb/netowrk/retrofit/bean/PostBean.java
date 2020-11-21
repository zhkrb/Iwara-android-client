package com.zhkrb.netowrk.retrofit.bean;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class PostBean {
    private JSONObject mArgs;


    public PostBean() {
        mArgs = new JSONObject();
    }

    public PostBean param(String key, String value) {
        try {
            mArgs.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PostBean param(String key, int value) {
        try {
            mArgs.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PostBean param(String key, long value) {
        try {
            mArgs.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PostBean param(String key, JSONObject value) {
        try {
            mArgs.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PostBean param(String key, JSONArray value) {
        try {
            mArgs.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isEmpty() {
        return mArgs == null || mArgs.size() == 0;
    }

    public String create() {
        return mArgs.toJSONString();
    }


}
