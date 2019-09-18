package com.zhkrb.iwara.netowrk;

public abstract class NetworkCallback {

    public abstract void onStart();

    public abstract void onSuccess(int code,String msg,String info);

    public abstract void onFinish();

    public abstract void onError(int code,String msg);

}
