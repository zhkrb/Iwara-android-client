package com.zhkrb.iwara.netowrk.jsoup;

import com.zhkrb.iwara.netowrk.retrofit.HttpUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.ResponseBody;

public class JsoupClient {

    private static String mUrl;
    private static JsoupClient mUtil;

    private JsoupClient(){
    }

    static JsoupClient getInstance(){
        if (mUtil==null){
            synchronized (JsoupClient.class){
                if (mUtil == null){
                    mUtil = new JsoupClient();
                }
            }
        }
        return mUtil;
    }

    void init(String url){
        mUrl = url;
    }

     Observable<ResponseBody> getObservable(final String url){
         return HttpUtil.getBody(url);
    }

    Observable<ResponseBody> getObservableWithoutHost(final String url){
        return HttpUtil.getBodyWithoutHost(url);
    }
}
