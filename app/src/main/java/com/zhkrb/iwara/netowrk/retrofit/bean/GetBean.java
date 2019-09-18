package com.zhkrb.iwara.netowrk.retrofit.bean;

import com.alibaba.fastjson.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetBean {

    private Map<String ,Object> mArgs;

    public GetBean() {
        mArgs = new HashMap<>();
    }

    public GetBean param(String key, String value){
        try {
            mArgs.put(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public GetBean param(String key, int value){
        try {
            mArgs.put(key,value);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return this;
    }

    public String create(){
        if (mArgs.size()==0){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for (String key:mArgs.keySet()){
            builder.append(key);
            builder.append("=");
            builder.append(mArgs.get(key));
        }
        builder.deleteCharAt(builder.length()-1);
        return mArgs.toString();
    }


}
