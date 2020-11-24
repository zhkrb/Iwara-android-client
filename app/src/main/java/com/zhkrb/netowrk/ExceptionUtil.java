package com.zhkrb.netowrk;

import com.zhkrb.utils.L;

import org.jsoup.SerializationException;
import org.jsoup.UncheckedIOException;
import org.jsoup.UnsupportedMimeTypeException;

import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ExceptionUtil {

    public static Msg getException(Throwable throwable){
        if (throwable instanceof HttpException){
            return new Msg(((HttpException) throwable).code(),throwable.getMessage());//网络错误码异常
        }
        if (throwable instanceof SerializationException){
            return new Msg(-998,throwable.getMessage());//序列化异常
        }
        if (throwable instanceof UncheckedIOException){
            return new Msg(-997,throwable.getMessage());//未检查异常
        }
        if (throwable instanceof UnsupportedMimeTypeException){
            return new Msg(-996,((UnsupportedMimeTypeException) throwable).getMimeType()+" "+throwable.getMessage());
        }
        if (throwable instanceof SocketTimeoutException){
            return new Msg(-995,((SocketTimeoutException) throwable).getMessage());
        }
        L.e("network Exception",throwable.getCause() + " " +throwable.getMessage());
        return new Msg(-999,"unknow exception");
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
