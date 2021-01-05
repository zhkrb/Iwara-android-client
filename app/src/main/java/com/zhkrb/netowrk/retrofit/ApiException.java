package com.zhkrb.netowrk.retrofit;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/8/6 20:00
 */
public class ApiException extends RuntimeException {

    private int errorCode;

    public ApiException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public interface ApiErrorCode {

        /**
         * 权限不足
         * */
        int ERROR_USER_AUTHORIZED = 401;

        /**
         * 请求参数错误
         * */
        int ERROR_REQUEST_PARAM = 504;

        /**
         * Token 失效
         * */
        int ERROR_TOKEN_TIME_OUT = 501;

        /**
         * 授权到期
         */
        int ERROR_LICENSE_TIME_OUT = 9999;

        /**
         * 请求错误
         * */
        int ERROR_REQUEST = 400;

        /**
         * 无网络连接
         * */
        int ERROR_NO_INTERNET = 11;

        /**
         * 请求成功
         */
        int RESPONSE_SUCCESS = 200;
    }
}
