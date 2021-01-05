package com.zhkrb.netowrk.retrofit.model;

import com.zhkrb.netowrk.retrofit.JsonBean;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface GetModel {

    @GET
    Observable<ResponseBody> get(@Url String url);

    @GET
    Observable<ResponseBody> get(@HeaderMap Map<String, String> headers, @Url String url);

    @GET
    Observable<JsonBean> getJson(@Url String url);

    @GET
    Observable<JsonBean> getJson(@HeaderMap Map<String, String> headers, @Url String url);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> donwload(@Header("RANGE") String range, @Url String url);

}
