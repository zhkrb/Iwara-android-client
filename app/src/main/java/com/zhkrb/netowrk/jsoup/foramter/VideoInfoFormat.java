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
 * Create by zhkrb on 2021-03-06 13:57:38
 *
 */

package com.zhkrb.netowrk.jsoup.foramter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @description：视频详情页解析
 * @author：zhkrb
 * @DATE： 2021/3/6 13:57
 */
public class VideoInfoFormat {

    public static String videoInfoFormat(Document document) {
        JSONObject object = new JSONObject();
        Element infoElem = document.selectFirst("div.node-info");
        object.put("title",BaseFormatUtil.selectTextMayNull(infoElem,"h1.title"));
        object.put("author_name",BaseFormatUtil.selectTextMayNull(infoElem,"a.username"));
        String thumb = BaseFormatUtil.selectAttrMayNull(infoElem,"div.user-picture>a>img","src");
        if (TextUtils.isEmpty(thumb) || thumb.contains("default_avatar.png")){
            thumb = "default_avatar";
        }else {
            thumb = "https:"+thumb;
        }
        object.put("author_thumb",thumb);
        object.put("author_href",infoElem.selectFirst("a.username").attr("href"));
        String[] a = BaseFormatUtil.selectTextMayNull(infoElem,"div.submitted").split("作成日:");
        String date = "";
        if (a.length == 2){
            date = a[1];
        }
        object.put("author_video_upload_date",date);
        object.put("author_video_info",BaseFormatUtil.selectHtmlMayNull(infoElem,"div.field-item"));
        //TODO format video tag
        Elements tags = infoElem.select("div.field-name-field-categories");

        String[] info = BaseFormatUtil.selectTextMayNull(infoElem,"div.node-views").split(" ");
        object.put("author_video_like",info.length == 2 ? info[0] : "");
        object.put("author_video_view",info.length == 2 ? info[1] : "");
        Elements from = null;
        Element video_from = document.selectFirst("div.view-id-videos");
        if (video_from != null){
            from = video_from.select("div.node-video");
        }
        object.put("author_video_from_user", JSON.parseArray(VideoListFormat.videoListFormat(from)));
        Elements recomm = null;
        Element video_recomm = document.selectFirst("div.view-id-search");
        if (video_recomm != null){
            recomm = video_recomm.select("div.node-video");
        }
        object.put("author_video_recomm", JSON.parseArray(VideoListFormat.videoListFormat(recomm)));
        Element comment = document.selectFirst("#comments");
        object.put("comment_count", CommentFormatUtil.getCommentCount(comment));
        object.put("comment_pages", CommentFormatUtil.getCommentPages(comment));
        object.put("comment_item_list", CommentFormatUtil.getCommentItemList(document));
        object.put("comment_item_reply_pageid",CommentFormatUtil.getCommentPageId(document));
        return object.toJSONString();
    }
}
