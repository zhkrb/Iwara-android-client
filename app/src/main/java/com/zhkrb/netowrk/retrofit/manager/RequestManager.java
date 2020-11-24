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
 * Create by zhkrb on 2019/9/9 12:23
 */

package com.zhkrb.netowrk.retrofit.manager;

import android.util.ArrayMap;

import java.util.Objects;
import java.util.Set;

import io.reactivex.rxjava3.disposables.Disposable;


public class RequestManager implements IRequestManager {

    private static RequestManager sInstance;
    private final ArrayMap<String, Disposable> mMap;

    public static RequestManager getInstance(){
        if (sInstance == null){
            synchronized (RequestManager.class){
                if (sInstance == null){
                    sInstance = new RequestManager();
                }
            }
        }
        return sInstance;
    }

    public RequestManager() {
        mMap = new ArrayMap<>(0);
    }

    @Override
    public synchronized String add(String tag, Disposable subscription) {
        if (mMap.containsKey(tag)){
            tag += System.currentTimeMillis();
        }
        mMap.put(tag,subscription);
        return tag;
    }

    @Override
    public void remove(String tag) {
        if (!mMap.isEmpty()){
            mMap.remove(tag);
        }
    }

    public void removeAll(){
        if (!mMap.isEmpty()){
            mMap.clear();
        }
    }

    @Override
    public synchronized void cancel(String tag) {
        if (mMap.isEmpty()){
            return;
        }
        if (mMap.get(tag) != null){
            if (!Objects.requireNonNull(mMap.get(tag)).isDisposed()){
                Objects.requireNonNull(mMap.get(tag)).dispose();
                remove(tag);
            }
        }

    }

    @Override
    public void cancelAll() {
        if (mMap.isEmpty()) {
            return;
        }
        Set<String> keys = mMap.keySet();
        for (String apiKey : keys) {
            cancel(apiKey);
        }
    }
}
