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
 * Create by zhkrb on 2021-03-06 13:54:02
 *
 */

package com.zhkrb.netowrk.jsoup.foramter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhkrb.iwara.AppConfig;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.netowrk.jsoup.BaseJsoupCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：视频列表解析
 * @author：zhkrb
 * @DATE： 2021/3/6 13:54
 */
public class VideoListFormat {

    public static BaseJsoupCallback.Formatter<List<VideoListBean>> formatter = ((callback, body) -> {
        Document document = Jsoup.parse(body);
        Elements elements = document.select("div.node-video");
        if (elements == null || elements.size() == 0) {
            callback.onSuccess(null);
            return;
        }
        callback.onSuccess(VideoListFormat.videoListFormat(elements));
    });

    public static List<VideoListBean> videoListFormat(Elements elements){
        List<VideoListBean> beanList = new ArrayList<>();
        if (elements == null){
            return null;
        }
        for (Element element:elements){
            VideoListBean bean = new VideoListBean();
            bean.setLike(BaseFormatUtil.selectTextMayNull(element,"div.right-icon"));
            bean.setView(BaseFormatUtil.selectTextMayNull(element,"div.left-icon"));
            bean.setHref(element.selectFirst("div.field-item").getElementsByTag("a").attr("href"));
            if (element.selectFirst("img")!=null){
                String thumb = BaseFormatUtil.selectAttrMayNull(element,"img","src");
                if (!TextUtils.isEmpty(thumb)&&!thumb.contains(AppConfig.NOM_HOST_URL)){
                    thumb = "//" + AppConfig.HOST_URL + thumb;
                }
                bean.setThumb("https:" + thumb);
                String title = "";
                if (TextUtils.isEmpty(BaseFormatUtil.selectAttrMayNull(element,"img","title"))){
                    if (element.selectFirst("h3.title") != null){
                        title = element.selectFirst("h3.title").selectFirst("a").text();
                    }else if (!TextUtils.isEmpty(element.attr("data-original-title"))){
                        title = element.attr("data-original-title");
                    }
                }else {
                    title = BaseFormatUtil.selectAttrMayNull(element,"img","title");
                }
                bean.setTitle(title);
            }else {
                bean.setThumb("");
                bean.setTitle(BaseFormatUtil.selectTextMayNull(element,"h3.title>a"));
            }

            bean.setUserName(BaseFormatUtil.selectTextMayNull(element,"a.username"));
            bean.setUserHref(BaseFormatUtil.selectAttrMayNull(element,"a.username","href"));
            bean.setPrivated(element.selectFirst("div.private-video") != null);
            bean.setType(0);

            beanList.add(bean);
        }
        return beanList;
    }


}
