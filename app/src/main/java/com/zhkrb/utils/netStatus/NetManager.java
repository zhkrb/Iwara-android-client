package com.zhkrb.utils.netStatus;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;

import androidx.lifecycle.MutableLiveData;

/**
 * @description：网络状态管理类
 * @author：zhkrb
 * @DATE： 2020/8/18 9:40
 */
public class NetManager {

    private static NetManager sInstance;

    private static NetStatusCallback netStatusCallback;

    private Application mApplication;

    public static NetManager getInstance(Application application){
        if (sInstance == null){
            synchronized (NetManager.class){
                if (sInstance == null){
                    sInstance = new NetManager(application);
                }
            }
        }
        return sInstance;
    }

    public NetManager(Application application) {
        mApplication = application;
        netStatusCallback = new NetStatusCallback();
        NetworkRequest request = new NetworkRequest.Builder().build();
        ConnectivityManager manager = (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.registerNetworkCallback(request,netStatusCallback);
    }

    public synchronized void register(Object object){
        netStatusCallback.register(object);
    }

    public synchronized void unRegister(Object object){
        netStatusCallback.unRegister(object);
    }

    public synchronized void unRegisterAll(){
        netStatusCallback.unRegisterAll();
        ConnectivityManager manager = (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.unregisterNetworkCallback(netStatusCallback);
    }

    public Integer getNetType(){
        return netStatusCallback.getNetTypeLiveData().getValue();
    }

    public MutableLiveData<Integer> getNetTypeLiveData(){
        return netStatusCallback.getNetTypeLiveData();
    }

    public boolean isRegister(Object object) {
        return netStatusCallback.isRegister(object);
    }


}
