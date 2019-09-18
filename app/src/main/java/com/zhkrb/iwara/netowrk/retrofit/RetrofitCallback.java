package com.zhkrb.iwara.netowrk.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.zhkrb.iwara.netowrk.ExceptionUtil;
import com.zhkrb.iwara.netowrk.NetworkCallback;
import com.zhkrb.iwara.netowrk.retrofit.manager.RequestManager;

import java.io.IOException;
import java.net.HttpURLConnection;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class RetrofitCallback extends NetworkCallback implements Observer<ResponseBody> {

    private String mTag;

    public RetrofitCallback addTag(String tag){
        mTag = tag;
        return this;
    }


    @Override
    public void onSubscribe(Disposable d) {
        RequestManager.getInstance().add(mTag,d);
        onStart();
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        String string = null;
        try {
            string = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            onSuccess(HttpURLConnection.HTTP_OK,"can't cast","");
        }
        if (TextUtils.isEmpty(string)){
           onSuccess(HttpURLConnection.HTTP_OK,"empty body","");
            return;
        }
        onSuccess(HttpURLConnection.HTTP_OK,"success",string);
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        ExceptionUtil.msg msg1 = ExceptionUtil.getException(t);
        Log.e("jsoup exception",msg1.getCode()+": "+msg1.getMsg());
        onError(msg1.getCode(),msg1.getMsg());
        RequestManager.getInstance().remove(mTag);
        onFinish();
    }

    @Override
    public void onComplete() {
        RequestManager.getInstance().remove(mTag);
        onFinish();

    }
}
