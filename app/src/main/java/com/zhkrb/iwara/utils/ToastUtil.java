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

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.zhkrb.iwara.AppContext;
import com.zhkrb.iwara.R;

/**
 * Created by cxf on 2017/8/3.
 */

public class ToastUtil {

    private static Toast sToast;

    static {
        sToast = Toast.makeText(AppContext.sInstance, "", Toast.LENGTH_SHORT);
    }

    public static void show(int res){
        show(WordUtil.getString(res));
    }

    public static void show(String s) {
        View view = LayoutInflater.from(AppContext.sInstance).inflate(R.layout.toast,null,false);
        ((TextView)view.findViewById(com.zhkrb.dragvideo.R.id.text)).setText(s);
        sToast.setView(view);
        sToast.show();
    }

}
