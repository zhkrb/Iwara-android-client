package com.zhkrb.netowrk.jsoup;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhkrb.netowrk.ExceptionUtil;
import com.zhkrb.netowrk.NetworkCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.HTTP;

public class JsoupUtil {


    public static void init(String url){
        JsoupClient.getInstance().init(url);
    }

    /**
     *
     * @param type  排序类型
     * @param p 页数
     * @param callback
     * json格式：
     * [
     * {
     *    "like" : String,
     *    "view" : String,
     *    "href" : String,
     *    "thumb" : String,
     *    "title" : String,
     *    "user_name" : String,
     *    "user_href" : String,
     *    "type" : int       //0 视频 1 图片
     * }
     * ...
     * ]
     */
    public static void getVideoList(int type,int p, @NonNull final NetworkCallback callback){
        Observer<ResponseBody> observer = new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                callback.onStart();
            }

            @Override
            public void onNext(ResponseBody body) {
                try {
                    Document document = Jsoup.parse(body.string());
                    Elements elements = document.select("div.node-video");
                    if (elements==null||elements.size()==0){
                        callback.onSuccess(200,"empty body","");
                        return;
                    }
                    callback.onSuccess(HttpURLConnection.HTTP_OK,"success",FormatUtil.videoListFormat(elements));
                } catch (Exception e) {
                    e.printStackTrace();
                    onError(e);
                }

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ExceptionUtil.msg msg1 = ExceptionUtil.getException(e);
                Log.e("jsoup exception",msg1.getCode()+": "+msg1.getMsg());
                callback.onError(msg1.getCode(),msg1.getMsg());
                callback.onFinish();
            }

            @Override
            public void onComplete() {
                callback.onFinish();
            }
        };
        String url ="/videos?sort=";
        switch (type){
            case 0://日期
                url += "date";
                break;
            case 1://点击
                url += "views";
                break;
            case 2://like
                url += "likes";
                break;
        }
        if (p>1){
            url += "&page="+ (p - 1);
        }
        JsoupClient.getInstance().getObservable(url).subscribe(observer);
    }




}
