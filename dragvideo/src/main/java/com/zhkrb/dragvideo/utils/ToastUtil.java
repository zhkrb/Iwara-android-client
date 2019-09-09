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

package com.zhkrb.dragvideo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhkrb.dragvideo.R;


public class ToastUtil {


    public static void show(Context context ,int res){
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.toast,null,false);
        ((TextView)view.findViewById(R.id.text)).setText(res);
        toast.setView(view);
        toast.show();
    }

    public static void show(Context context ,String s) {
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.toast,null,false);
        ((TextView)view.findViewById(R.id.text)).setText(s);
        toast.setView(view);
        toast.show();
    }

}
