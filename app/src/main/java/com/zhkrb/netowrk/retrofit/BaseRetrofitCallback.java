package com.zhkrb.netowrk.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.zhkrb.netowrk.BaseDataLoadCallback;
import com.zhkrb.netowrk.ExceptionUtil;
import com.zhkrb.netowrk.retrofit.manager.RequestManager;

import java.io.IOException;
import java.net.HttpURLConnection;


import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

public abstract class BaseRetrofitCallback implements Observer<ResponseBody>, BaseDataLoadCallback<String> {

    private String mTag;

    public BaseRetrofitCallback addTag(String tag){
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
            BufferedSource bufferedSource = Okio.buffer(responseBody.source());
            string = bufferedSource.readUtf8();
            bufferedSource.close();
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
        ExceptionUtil.Msg msg1 = ExceptionUtil.getException(t);
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

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }
}
