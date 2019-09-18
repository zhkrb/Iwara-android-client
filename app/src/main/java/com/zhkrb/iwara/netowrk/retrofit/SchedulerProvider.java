package com.zhkrb.iwara.netowrk.retrofit;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider implements BaseSchedulerProvider {

    @NonNull
    private static SchedulerProvider sInstance;

    public SchedulerProvider() {
    }

    public static synchronized SchedulerProvider getInstance(){
        if (sInstance == null){
            synchronized (SchedulerProvider.class){
                if (sInstance == null){
                    sInstance = new SchedulerProvider();
                }
            }
        }
        return sInstance;
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    public <T> ObservableTransformer<T,T> applaySchedulers(){
        return observable -> observable.subscribeOn(io())
                .observeOn(ui());
    }

}

interface BaseSchedulerProvider {

    Scheduler ui();

    Scheduler io();

}
