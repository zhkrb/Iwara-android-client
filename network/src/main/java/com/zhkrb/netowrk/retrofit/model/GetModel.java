package com.zhkrb.netowrk.retrofit.model;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetModel {

    @GET
    Observable<ResponseBody> get(@Url String url);

}
