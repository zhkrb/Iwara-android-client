package com.zhkrb.netowrk.retrofit;

import com.zhkrb.netowrk.NetworkCallback;

import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallback extends NetworkCallback implements Callback<ResponseBody> {


    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(int code, String msg, String info) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(int code ,String msg) {

    }


    @Override

    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }
}
