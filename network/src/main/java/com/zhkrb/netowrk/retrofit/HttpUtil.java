package com.zhkrb.netowrk.retrofit;

import com.zhkrb.netowrk.NetworkCallback;
import com.zhkrb.netowrk.retrofit.bean.GetBean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class HttpUtil {

    public static void init(String url){
        HttpClient.getInstance().init(url);
    }

    public static Observable<ResponseBody> getBody(String api){
        return HttpClient.getInstance().get(api,"",null);
    }

    public static void testSend(@NonNull RetrofitCallback callback){
        callback.onStart();
        HttpClient.getInstance().get("aa","123",
                new GetBean()
                        .param("test",1)
                        .param("cc",2)
                ).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody body) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


}
