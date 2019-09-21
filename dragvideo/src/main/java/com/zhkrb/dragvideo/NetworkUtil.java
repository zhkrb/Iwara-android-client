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
 * Create by zhkrb on 2019/9/8 21:34
 */

package com.zhkrb.dragvideo;

import com.zhkrb.dragvideo.bean.UrlBean;
import com.zhkrb.dragvideo.mainView.VideoPlayerView;

import java.util.List;

public  class NetworkUtil implements Cloneable {

    protected GetPlayUrlCallback mCallback;

    public NetworkUtil() {
    }

    public void setGetPlayUrlCallback(GetPlayUrlCallback callback) {
        mCallback = callback;
    }

    public void getUrlList(String mainUrl){

    }

    public void cancel(){

    }

    public NetworkUtil clone()
    {
        NetworkUtil o = null;
        try
        {
            o = (NetworkUtil) super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            System.out.println(e.toString());
        }
        assert o != null;
        o.setGetPlayUrlCallback(mCallback.clone());
        return o;
    }


    public abstract static class GetPlayUrlCallback implements Cloneable{
        public abstract void onSuccess(List<UrlBean> list);
        public abstract void onFailed(String result);

        public GetPlayUrlCallback clone()
        {
            GetPlayUrlCallback o = null;
            try
            {
                o = (GetPlayUrlCallback) super.clone();
            }
            catch(CloneNotSupportedException e)
            {
                System.out.println(e.toString());
            }
            return o;
        }
    }
}
