package com.zhkrb.netowrk;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/7/9 19:06
 */
public interface BaseDataLoadCallback<T> {

    /**
     * 数据请求开始
     */
    void onStart();

    /**
     * 数据请求成功
     * @param code
     * @param msg
     * @param info
     */
    void onSuccess(int code, String msg, T info);

    /**
     * 数据请求结束
     */
    void onFinish();

    /**
     * 数据请求错误
     * @param code
     * @param msg
     */
    void onError(int code, String msg);


}
