package com.zhkrb.netowrk.retrofit.model;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PostModel {

    @POST
    Observable<ResponseBody> post(@Url String url, @Body RequestBody body);

    @POST
    Observable<ResponseBody> post(@HeaderMap Map<String, String> header, @Url String url, @Body RequestBody body);

}
