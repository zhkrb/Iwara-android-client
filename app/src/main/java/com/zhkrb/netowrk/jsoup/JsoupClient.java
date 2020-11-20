package com.zhkrb.netowrk.jsoup;

import com.zhkrb.netowrk.retrofit.HttpUtil;

import io.reactivex.Observable;
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
