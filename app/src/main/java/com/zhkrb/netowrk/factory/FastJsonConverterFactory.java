package com.zhkrb.netowrk.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

/**
 * @description：转换工厂类
 * @author：zhkrb
 * @DATE： 2020/8/7 8:53
 */
public class FastJsonConverterFactory extends Converter.Factory {

    public static FastJsonConverterFactory create(){
        return new FastJsonConverterFactory();
    }

    @EverythingIsNonNull
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConverter();
    }
}
