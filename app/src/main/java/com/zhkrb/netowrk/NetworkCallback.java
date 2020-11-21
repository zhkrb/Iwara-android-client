package com.zhkrb.netowrk;

public interface NetworkCallback {

    /**
     * 请求开始
     */
    void onStart();

    /**
     * 请求成功
     * @param code 状态码
     * @param msg  信息
     * @param info 返回值
     */
    void onSuccess(int code,String msg,String info);

    /**
     * 完成请求
     */
    void onFinish();

    /**
     * 请求错误
     * @param code 状态码
     * @param msg 信息
     */
    void onError(int code,String msg);

}
