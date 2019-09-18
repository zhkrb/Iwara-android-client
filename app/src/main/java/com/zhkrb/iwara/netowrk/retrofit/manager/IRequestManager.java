package com.zhkrb.iwara.netowrk.retrofit.manager;


import io.reactivex.disposables.Disposable;

public interface IRequestManager<T> {

    void add(T tag, Disposable subscription);

    void remove(T tag);

    void cancel(T tag);

    void cancelAll();

}
