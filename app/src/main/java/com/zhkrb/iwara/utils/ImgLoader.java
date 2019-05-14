package com.zhkrb.iwara.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhkrb.iwara.AppContext;

public class ImgLoader {

    private static RequestManager sManager;
    private static RequestOptions options;
    private static boolean isPause = false;

    static {
        options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager = Glide.with(AppContext.sInstance);
    }

    public static void display(String url, ImageView imageView) {
        sManager.load(url).apply(options).into(imageView);
    }

    public static void pause(){
        if (!isPause){
            sManager.pauseRequests();
            isPause = true;
        }
    }

    public static void resume(){
        if (isPause){
            sManager.resumeRequests();
            isPause = false;
        }
    }


}
