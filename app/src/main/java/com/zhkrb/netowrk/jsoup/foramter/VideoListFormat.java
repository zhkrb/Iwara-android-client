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

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @description：视频列表解析
 * @author：zhkrb
 * @DATE： 2021/3/6 13:54
 */
public class VideoListFormat {

    public static String videoListFormat(Elements elements){
        JSONArray array = new JSONArray();
        if (elements == null){
            return array.toJSONString();
        }
        for (Element element:elements){
            JSONObject object = new JSONObject();
            object.put("like",BaseFormatUtil.selectTextMayNull(element,"div.right-icon"));
            object.put("view",BaseFormatUtil.selectTextMayNull(element,"div.left-icon"));
            object.put("href",element.selectFirst("div.field-item").getElementsByTag("a").attr("href"));
            if (element.selectFirst("img")!=null){
                String thumb = BaseFormatUtil.selectAttrMayNull(element,"img","src");
                if (!TextUtils.isEmpty(thumb)&&!thumb.contains(AppConfig.NOM_HOST_URL)){
                    thumb = "//" + AppConfig.HOST_URL + thumb;
                }
                object.put("thumb","https:" + thumb);
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
                object.put("title",title);
            }else {
                object.put("thumb","");
                object.put("title",BaseFormatUtil.selectTextMayNull(element,"h3.title>a"));
            }

            object.put("user_name",BaseFormatUtil.selectTextMayNull(element,"a.username"));
            object.put("user_href",BaseFormatUtil.selectAttrMayNull(element,"a.username","href"));
            object.put("privated",element.selectFirst("div.private-video") != null);
            object.put("type",0);
            array.add(object);
        }
        return array.toJSONString();
    }


}
