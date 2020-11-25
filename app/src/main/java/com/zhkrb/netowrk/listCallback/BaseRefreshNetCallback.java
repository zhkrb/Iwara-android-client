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
 * Create by zhkrb on 2020/11/24 11:11
 */

package com.zhkrb.netowrk.listCallback;

import com.alibaba.fastjson.JSONArray;
import com.zhkrb.netowrk.BaseDataLoadCallback;
import com.zhkrb.netowrk.ExceptionUtil;
import com.zhkrb.netowrk.retrofit.manager.RequestManager;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/11/24 11:11
 */
public abstract class BaseRefreshNetCallback<T> implements Observer<ResponseBody>, BaseDataLoadCallback<List<T>> {

    private String mTag;
    private Class<T> mClass;

    public BaseRefreshNetCallback(Class<T> clazz) {
        mClass = clazz;
    }

    public BaseRefreshNetCallback<T> addTag(String tag) {
        mTag = tag;
        return this;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mTag = RequestManager.getInstance().add(mTag, d);
        onStart();
    }

    @Override
    public void onNext(@NonNull ResponseBody body) {
        List<T> list = null;

        try {
            BufferedSource bufferedSource = Okio.buffer(body.source());
            String string = bufferedSource.readUtf8();
            bufferedSource.close();
            list = JSONArray.parseArray(string,mClass);
        } catch (IOException e) {
            e.printStackTrace();
            onError(e);
        }

        onSuccess(200, "success", list);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        RequestManager.getInstance().remove(mTag);

        ExceptionUtil.Msg msg = ExceptionUtil.getException(e);
        int code = msg.getCode();
        String message = msg.getMsg();

        onError(code, message);
        onFinish();
    }

    @Override
    public void onComplete() {
        RequestManager.getInstance().remove(mTag);
        onFinish();
    }


    public String getTag() {
        return mTag;
    }
}
