package com.zhkrb.netowrk;

import org.jsoup.HttpStatusException;
import org.jsoup.SerializationException;
import org.jsoup.UncheckedIOException;
import org.jsoup.UnsupportedMimeTypeException;

import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ExceptionUtil {

    public static msg getException(Throwable throwable){
        if (throwable instanceof HttpException){
            return new msg(((HttpException) throwable).code(),throwable.getMessage());//网络错误码异常
        }
        if (throwable instanceof SerializationException){
            return new msg(-998,throwable.getMessage());//序列化异常
        }
        if (throwable instanceof UncheckedIOException){
            return new msg(-997,throwable.getMessage());//未检查异常
        }
        if (throwable instanceof UnsupportedMimeTypeException){
            return new msg(-996,((UnsupportedMimeTypeException) throwable).getMimeType()+" "+throwable.getMessage());
        }
        if (throwable instanceof SocketTimeoutException){
            return new msg(-995,((SocketTimeoutException) throwable).getMessage());
        }
        return new msg(-999,"unknow exception");
    }

    public static class msg {
        int code;
        String msg;

        public msg(int code, String msg) {
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
