package com.zhkrb.netowrk.factory;

import com.alibaba.fastjson.JSON;
import com.zhkrb.netowrk.retrofit.ApiException;
import com.zhkrb.netowrk.retrofit.JsonBean;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;
import retrofit2.internal.EverythingIsNonNull;

/**
 * @description：fastjson转换以及异常处理
 * @author：zhkrb
 * @DATE： 2020/8/7 8:37
 */
public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, JsonBean> {


    public FastJsonResponseBodyConverter() {

    }


    @EverythingIsNonNull
    @Override
    public JsonBean convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String response = bufferedSource.readUtf8();
        bufferedSource.close();
        JsonBean bean = JSON.parseObject(response,JsonBean.class);
        if (!bean.isSuccess()){
            value.close();
            throw new ApiException(bean.getRet(),bean.getMsg());
        }
        return bean;
    }
}
