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
 * Create by zhkrb on 2019/9/9 11:12
 */

package com.zhkrb.utils;

import com.alibaba.fastjson.JSON;
import com.zhkrb.dragvideo.NetworkUtil;
import com.zhkrb.dragvideo.bean.UrlBean;
import com.zhkrb.iwara.AppConfig;
import com.zhkrb.netowrk.retrofit.HttpUtil;
import com.zhkrb.netowrk.retrofit.RetrofitCallback;

import java.util.List;

public class VideoNetWorkUtil extends NetworkUtil {

    @Override
    public void getUrlList(String mainUrl) {
        String[] sp = mainUrl.split("/");
        String url = AppConfig.GET_VIDEO_API+sp[sp.length-1];
        HttpUtil.getVideoUrlList(url,HttpConstsUtil.GET_VIDEO_URL_LIST,mUrlCallback);
    }

    @Override
    public void cancel() {
        HttpUtil.cancel(HttpConstsUtil.GET_VIDEO_URL_LIST);
    }

    private RetrofitCallback mUrlCallback = new RetrofitCallback() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(int code, String msg, String info) {
            if (mCallback == null){
                return;
            }
            if (!msg.equals("success")){
                mCallback.onFailed(msg);
                return;
            }
            List<UrlBean> list = JSON.parseArray(info,UrlBean.class);
            if (list!=null&&list.size()>0){
                mCallback.onSuccess(list);
            }else {
                mCallback.onFailed("empty");
            }
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onError(int code, String msg) {
            if (mCallback!=null){
                mCallback.onFailed(msg);
            }
        }
    };

}
