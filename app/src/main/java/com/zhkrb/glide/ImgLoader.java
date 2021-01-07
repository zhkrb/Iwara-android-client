/*
 * Copyright zhkrb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Create by zhkrb on 2019/9/7 22:12
 */

package com.zhkrb.glide;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.zhkrb.iwara.AppContext;

public class ImgLoader {

    private static RequestManager sManager;
    private static boolean isPause = false;

    static {
        sManager = Glide.with(AppContext.sInstance);
    }

    //TODO 加载失败时占位图
    public static void display(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(url).apply(options).into(imageView);
    }

    public static void display(int res, ImageView imageView) {
        sManager.load(res).into(imageView);
    }

    public static void displayCircle(String url, ImageView imageView){
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .circleCrop();
        sManager.load(url).apply(options).into(imageView);
    }

    //TODO 加载失败时占位图
    public static void displayTryThumbnail(String url,ImageView imageView){
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        sManager.load(url.replaceFirst("thumbnail","frontpage_mid "))
                .apply(options)
                .error(sManager
                        .load(url)
                        .apply(options)
                        )
                .into(imageView);
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
