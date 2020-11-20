package com.zhkrb.netowrk.retrofit;

import com.zhkrb.iwara.AppConfig;
import com.zhkrb.netowrk.retrofit.bean.GetBean;
import com.zhkrb.netowrk.retrofit.manager.RequestManager;
import com.zhkrb.utils.HttpConstsUtil;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;

public class HttpUtil {

    public static void init(String url){
        HttpClient.getInstance().init(url);
    }

    public static void cancel(String tag){
        RequestManager.getInstance().cancel(tag);
    }

    public static void cancelAll(){
        RequestManager.getInstance().cancelAll();
    }

    public static Observable<ResponseBody> getBody(String api){
        return HttpClient.getInstance().get(api,null,null);
    }

    public static Observable<ResponseBody> getBodyWithoutHost(String url){
        return HttpClient.getInstance().getFullUrl(url,null,null);
    }

    public static void testSend(String tag,@NonNull RetrofitCallback callback){
        HttpClient.getInstance().get("aa",
                new GetBean()
                        .param("test",1)
                        .param("cc",2)
                ,null).subscribe(callback.addTag(tag));
    }

    public static void getVideoUrlList(String url,String tag,@NonNull RetrofitCallback callback){
        HttpClient.getInstance().get(url,null,null).
                subscribe(callback.addTag(tag));
    }

    public static void getUpdateInfo(@NonNull RetrofitCallback callback) {
        HttpClient.getInstance().getFullUrl(AppConfig.UPDATE_URL,null,null).
                subscribe(callback.addTag(HttpConstsUtil.CHECK_UPDATE));
    }
}