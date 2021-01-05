package com.zhkrb.netowrk;

import com.zhkrb.iwara.R;
import com.zhkrb.netowrk.retrofit.ApiException;
import com.zhkrb.utils.L;
import com.zhkrb.utils.WordUtil;


import org.jsoup.SerializationException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ExceptionUtil {

    public static Msg getException(Throwable throwable){
        if (throwable instanceof HttpException){
            //网络错误码异常
            return new Msg(((HttpException) throwable).code(),throwable.getMessage());
        }
        if (throwable instanceof ConnectException){
            //序列化异常
            L.e(throwable.getMessage());
            return new Msg(-997, WordUtil.getString(R.string.io_exception));
        }
        if (throwable instanceof SerializationException){
            L.e(throwable.getMessage());
            return new Msg(-998,WordUtil.getString(R.string.serialization_error));
        }
        if (throwable instanceof ApiException){
//            switch (((ApiException) throwable).getErrorCode()){
//                case ApiException.ApiErrorCode.ERROR_USER_AUTHORIZED:
//                    L.w("Connect",throwable.getMessage());
//                    L.w("Connect","token 验证失效，退出登录");
//                    HttpUtil.cancelAll();
//                    AppUser.getInstance().updateLogin(null);
//                    break;
//                case ApiException.ApiErrorCode.ERROR_LICENSE_TIME_OUT:
//                    AppUser.getInstance().pauseAll();
//                    break;
//                default:
//            }

            return new Msg(((ApiException) throwable).getErrorCode(),throwable.getMessage());
        }
        if (throwable instanceof SocketTimeoutException){
            return new Msg(-997,WordUtil.getString(R.string.client_timeout));
        }
        return new Msg(-999,WordUtil.getString(R.string.unknow_exception));
    }

    public static class Msg {
        int code;
        String msg;

        public Msg(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

}
