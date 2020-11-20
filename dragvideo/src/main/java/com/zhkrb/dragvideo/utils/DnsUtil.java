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

/**
 * FFmpeg设置的url最终由tcp.c文件中的getaddrinfo()来解析host
 * 他是线程阻塞的，所以碰到DNS污染或者DNS设置错误会导致ANR
 * 需要自己实现DNS解析然后替换掉url中的域名
 * 然后在FFmpeg的header中添加Host: 原始域名(注意域名前面要添加空格)
 */

public class DnsUtil {

    protected String host;
    protected DnsUtilCallback mCallback;

    protected DnsUtil() {
    }

    public synchronized void getHostDns(String url){

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
