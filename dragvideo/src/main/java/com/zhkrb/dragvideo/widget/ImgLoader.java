//Copyright zhkrb
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

//Create by zhkrb on 2019/9/7 22:12

package com.zhkrb.dragvideo.widget;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
public class ImgLoader {

    private static RequestOptions options;

    static {
        options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    //TODO 加载失败时占位图
    public static void display(Context context,String url, ImageView imageView) {
        Glide.with(context).load(url).apply(options).into(imageView);
    }

}
