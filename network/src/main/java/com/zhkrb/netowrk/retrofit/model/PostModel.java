package com.zhkrb.netowrk.retrofit.model;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PostModel {

    @POST
    Observable<ResponseBody> post(@Url String url, @Body RequestBody body);

}
