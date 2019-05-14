package com.zhkrb.netowrk.jsoup;

import android.util.Log;

import com.zhkrb.netowrk.retrofit.HttpClient;
import com.zhkrb.netowrk.retrofit.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
         return HttpUtil.getBody(url).retry(1);

//        return Observable.create(new ObservableOnSubscribe<Document>() {
//            @Override
//            public void subscribe(ObservableEmitter<Document> emitter) throws Exception {
//                Log.e("rx","Jsoup getUrl ---> "+mUrl+url);
//                try {
//
//                    Document document = Jsoup.connect(mUrl+url).timeout(60000).get();
//                    emitter.onNext(document);
//                }catch (Exception e){
//                    emitter.onError(e);
//
//                }
//                emitter.onComplete();
//            }
//        })
//                .retry(1)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io()) ;
    }
}
