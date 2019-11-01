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
 * Create by zhkrb on 2019/11/1 14:57
 */

package com.zhkrb.dragvideo.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.RequestManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DnsUtil {

    protected String host;
    protected DnsUtilCallback mCallback;

    public DnsUtil() {
    }

    public void getHostDns(String url){

    }

    public void cancel(){

    }

    public void setCallback(DnsUtilCallback callback) {
        mCallback = callback;
    }

    public interface DnsUtilCallback{

        void onSuccess(String host,String url);
        void onFail();

    }

}
