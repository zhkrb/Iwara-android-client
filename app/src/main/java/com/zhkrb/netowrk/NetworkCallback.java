package com.zhkrb.netowrk;

public interface NetworkCallback {

    void onStart();

    void onSuccess(int code,String msg,String info);

    void onFinish();

    void onError(int code,String msg);

}
