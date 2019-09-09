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

package com.zhkrb.iwara.utils;

import android.content.res.Resources;

import com.zhkrb.iwara.AppContext;
import com.zhkrb.iwara.R;

/**
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
