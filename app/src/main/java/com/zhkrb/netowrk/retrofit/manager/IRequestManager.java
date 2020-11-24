package com.zhkrb.netowrk.retrofit.manager;


import io.reactivex.rxjava3.disposables.Disposable;

public interface IRequestManager {

    /**
     * 添加请求, 会返回tag
     * @param tag
     * @param subscription
     * @return
     */
    String add(String tag, Disposable subscription);

    /**
     * 移除一个请求tag, 但不会取消请求
     * @param tag
     */
    void remove(String tag);

    /**
     * 取消请求
     * @param tag
     */
    void cancel(String tag);

    /**
     * 取消全部请求
     */
    void cancelAll();

}
