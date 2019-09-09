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
 * Create by zhkrb on 2019/9/8 23:11
 */

package com.zhkrb.dragvideo.utils;

import android.content.Context;

import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.bean.SettingBean;
import com.zhkrb.dragvideo.bean.UrlBean;
import com.zhkrb.dragvideo.bean.ValueSelectBean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SettingListUtil {

    public static List<SettingBean> getSettingList(Context context, List<UrlBean> urlList,int urlPos,float speed){
        List<SettingBean> list = new ArrayList<>();
        SettingBean bean = new SettingBean();
        bean.setId(0);
        bean.setTitle(context.getResources().getString(R.string.quality));
        if (urlList != null){
            bean.setValue(urlList.get(urlPos).getResolution());
        }
        bean.setThumb(R.drawable.ic_settings_white_24dp);
        list.add(bean);
        SettingBean bean1 = new SettingBean();
        bean1.setId(1);
        bean1.setTitle(context.getResources().getString(R.string.speed));
        if (speed == 1.0f){
            bean1.setValue(context.getResources().getString(R.string.nom));
        }else {
            bean1.setValue(String.format(context.getResources().getString(R.string.speed_setting_item),speed));
        }
        bean1.setThumb(R.drawable.ic_slow_motion_video_white_24dp);
        list.add(bean1);
        return list;
    }


    public static List<ValueSelectBean> getSpeedList(Context context) {
        List<ValueSelectBean> list = new ArrayList<>();
        NumberFormat formatter = new DecimalFormat("0.00");
        ValueSelectBean bean = new ValueSelectBean();
        bean.setValue("0.5");
        bean.setValueText(String.format(context.getResources().getString(R.string.speed_item),formatter.format(0.5f)));
        list.add(bean);
        ValueSelectBean bean1 = new ValueSelectBean();
        bean1.setValue("0.75");
        bean1.setValueText(String.format(context.getResources().getString(R.string.speed_item),formatter.format(0.75f)));
        list.add(bean1);
        ValueSelectBean bean2 = new ValueSelectBean();
        bean2.setValue("1.0");
        bean2.setValueText(context.getResources().getString(R.string.nom));
        list.add(bean2);
        ValueSelectBean bean3 = new ValueSelectBean();
        bean3.setValue("1.5");
        bean3.setValueText(String.format(context.getResources().getString(R.string.speed_item),formatter.format(1.5f)));
        list.add(bean3);
        ValueSelectBean bean4 = new ValueSelectBean();
        bean4.setValue("2.0");
        bean4.setValueText(String.format(context.getResources().getString(R.string.speed_item),formatter.format(2.0f)));
        list.add(bean4);
        return list;
    }


    public static List<ValueSelectBean> getUrlList(List<UrlBean> videoUrlList) {
        List<ValueSelectBean> list = new ArrayList<>();
        for (int i=0; i<videoUrlList.size(); i++){
            ValueSelectBean bean1 = new ValueSelectBean();
            bean1.setValue(String.valueOf(i));
            bean1.setValueText(videoUrlList.get(i).getResolution());
            list.add(bean1);
        }
        return list;
    }
}
