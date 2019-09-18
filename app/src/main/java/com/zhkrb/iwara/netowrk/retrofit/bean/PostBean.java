package com.zhkrb.iwara.netowrk.retrofit.bean;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class PostBean {

    private JSONObject mArgs;


    public PostBean() {
        mArgs = new JSONObject();
    }

    public void param(String key,String value){
        try {
            mArgs.put(key,value);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void param(String key,int value){
        try {
            mArgs.put(key,value);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void param(String key, JSONObject value){
        try {
            mArgs.put(key,value);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void param(String key, JSONArray value){
        try {
            mArgs.put(key,value);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public boolean isEmpry(){
        return mArgs == null || mArgs.size() == 0;
    }

    public String create(){
        return mArgs.toJSONString();
    }




}
