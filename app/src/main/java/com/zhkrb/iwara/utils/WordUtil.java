package com.zhkrb.iwara.utils;

import android.content.res.Resources;

import com.zhkrb.iwara.AppContext;
import com.zhkrb.iwara.R;

/**
 * Created by cxf on 2017/10/10.
 * 获取string.xml中的字
 */

public class WordUtil {

    private static Resources sResources;

    static {
        sResources = AppContext.sInstance.getResources();
    }

    public static String getString(int res) {
        return sResources.getString(res);
    }


    public static String getErrorMsg(int code,String msg){
        switch (code){
            case -998:
                return getString(R.string.serializationerror);
            case -997:
                return getString(R.string.uncheckedioerror);
            case -996:
                return getString(R.string.unsupported_mime_type);
            case -995:
                return getString(R.string.socket_time_out);
            case -999:
                return getString(R.string.unknow_error);
            default:
                return msg;
        }
    }

}
