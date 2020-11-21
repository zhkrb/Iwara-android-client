package com.zhkrb.netowrk.retrofit.manager;


import io.reactivex.rxjava3.disposables.Disposable;

public interface IRequestManager<T> {

    void add(T tag, Disposable subscription);

    void remove(T tag);

    void cancel(T tag);

    void cancelAll();

}
