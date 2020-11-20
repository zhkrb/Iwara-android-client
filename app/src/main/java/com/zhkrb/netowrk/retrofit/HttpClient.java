package com.zhkrb.netowrk.retrofit;

import com.zhkrb.netowrk.retrofit.bean.GetBean;
import com.zhkrb.netowrk.retrofit.bean.PostBean;
import com.zhkrb.netowrk.retrofit.model.GetModel;
import com.zhkrb.netowrk.retrofit.model.PostModel;
import com.zhkrb.utils.L;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class HttpClient {

    private static HttpClient mClient;
    private static final int TIMEOUT = 30000;
    private static OkHttpClient mOkHttpClient;
    private static Retrofit mRetrofit;

    private HttpClient(){
    }

    public static HttpClient getInstance(){
        if (mClient==null){
            synchronized (HttpClient.class){
                if (mClient == null){
                    mClient = new HttpClient();
                }
            }
        }
        return mClient;
    }

    public void init(String url){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(true);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String message) {
                L.e(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(interceptor);

        mOkHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                .addConverterFactory(FastJsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    public Observable<ResponseBody> get(String apiName, GetBean bean, Map<String, String> headers){
        GetModel getModel = mRetrofit.create(GetModel.class);
        String a = "";
        if (bean!=null){
            a = bean.create();
        }
        if (headers == null){
            return getModel.get(apiName+a).compose(SchedulerProvider.getInstance().applySchedulers());
        }
        return getModel.get(headers,apiName+a).compose(SchedulerProvider.getInstance().applySchedulers());
    }

    /**
     * 完整请求url
     * @param url
     * @param bean
     * @param headers
     * @return
     */
    public Observable<ResponseBody> getFullUrl(String url, GetBean bean, Map<String, String> headers){
        GetModel getModel = mRetrofit.create(GetModel.class);
        String a = "";
        if (bean!=null){
            a = bean.create();
        }
        if (headers == null){
            return getModel.get(url+a).compose(SchedulerProvider.getInstance().applySchedulers());
        }
        return getModel.get(headers,url+a).compose(SchedulerProvider.getInstance().applySchedulers());
    }

    /**
     * 待header的请求
     * @param apiName
     * @param header
     * @param bean
     * @return
     */
    Observable<JsonBean> post(String apiName, HashMap<String,String> header, PostBean bean){
        PostModel postModel = mRetrofit.create(PostModel.class);
        RequestBody body = null;
        if (bean!=null&&!bean.isEmpry()){
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),bean.create());
        }else {
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"");
        }
        return postModel.post(header,apiName,body).compose(SchedulerProvider.getInstance().applySchedulers());
    }

    /**
     * 不带header的请求
     * @param apiName
     * @param bean
     * @return
     */
    public Observable<JsonBean> post(String apiName, PostBean bean){
        PostModel postModel = mRetrofit.create(PostModel.class);
        RequestBody body = null;
        if (bean!=null&&!bean.isEmpry()){
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),bean.create());
        }else {
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"");
        }
        return postModel.post(apiName,body).compose(SchedulerProvider.getInstance().applySchedulers());
    }


}
