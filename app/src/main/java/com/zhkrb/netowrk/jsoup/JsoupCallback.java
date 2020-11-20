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
 * Create by zhkrb on 2019/10/27 0:29
 */

package com.zhkrb.netowrk.jsoup;

import android.util.Log;

import com.zhkrb.netowrk.ExceptionUtil;
import com.zhkrb.netowrk.NetworkCallback;
import com.zhkrb.netowrk.retrofit.manager.RequestManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public  abstract class JsoupCallback implements Observer<ResponseBody>, NetworkCallback {

    private String mTag;
    private formater mFormater;

    public JsoupCallback addTag(String tag){
        mTag = tag;
        return this;
    }

    @Override
    public void onSubscribe(Disposable d) {
        RequestManager.getInstance().add(mTag,d);
        onStart();
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            if (mFormater != null){
                mFormater.format(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        ExceptionUtil.msg msg1 = ExceptionUtil.getException(e);
        Log.e("jsoup exception",msg1.getCode()+": "+msg1.getMsg());
        onError(msg1.getCode(),msg1.getMsg());
        RequestManager.getInstance().remove(mTag);
        onFinish();
    }

    @Override
    public void onComplete() {
        RequestManager.getInstance().remove(mTag);
        onFinish();
    }

    public void setFormater(formater formater) {
        mFormater = formater;
    }

    interface formater{
        void format(ResponseBody responseBody) throws Exception;
    }
}
