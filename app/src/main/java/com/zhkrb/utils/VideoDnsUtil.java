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
 * Create by zhkrb on 2019/11/1 16:28
 */

package com.zhkrb.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zhkrb.dragvideo.utils.DnsUtil;
import com.zhkrb.netowrk.retrofit.manager.RequestManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class VideoDnsUtil extends DnsUtil {

    public VideoDnsUtil() {
    }

    private final Observable<String> mObservable = Observable.create(emitter -> {
        if (TextUtils.isEmpty(host)){
            emitter.onError(new RuntimeException());
            return;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            if (inetAddress != null){
                String ip = inetAddress.getHostAddress();    //直接取第一个
                Log.e("dnsUtil",host+": "+ip);
                emitter.onNext(ip);
                emitter.onComplete();
            }
        }catch (UnknownHostException e){
            Log.e("dnsUtil","unKnow host");
            emitter.onError(e);
        }
    });

    @Override
    public synchronized void getHostDns(String url) {
        host = Uri.parse(url).getHost();
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                RequestManager.getInstance().add(HttpConstsUtil.GET_HOST_DNS,d);
            }

            @Override
            public void onNext(String s) {
                if (mCallback != null){
                    String newUrl = url.replace(host,s);
                    mCallback.onSuccess(host,newUrl);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mCallback != null){
                    mCallback.onFail();
                }
                RequestManager.getInstance().cancel(HttpConstsUtil.GET_HOST_DNS);
            }

            @Override
            public void onComplete() {
                RequestManager.getInstance().cancel(HttpConstsUtil.GET_HOST_DNS);
            }
        };
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    @Override
    public void cancel() {
        RequestManager.getInstance().cancel(HttpConstsUtil.GET_HOST_DNS);
    }
}
