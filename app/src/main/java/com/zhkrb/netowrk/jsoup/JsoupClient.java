package com.zhkrb.netowrk.jsoup;

import com.zhkrb.netowrk.retrofit.HttpUtil;
import com.zhkrb.netowrk.retrofit.SchedulerProvider;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

public class JsoupClient {

    private static JsoupClient mUtil;

    private JsoupClient() {
    }

    static JsoupClient getInstance() {
        if (mUtil == null) {
            synchronized (JsoupClient.class) {
                if (mUtil == null) {
                    mUtil = new JsoupClient();
                }
            }
        }
        return mUtil;
    }


    Observable<ResponseBody> getObservable(final String url) {
        return HttpUtil.getBody(url).compose(SchedulerProvider.getInstance().applySchedulersAllIO());
    }

}
