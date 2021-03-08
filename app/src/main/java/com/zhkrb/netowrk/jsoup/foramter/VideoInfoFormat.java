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
import com.zhkrb.iwara.bean.VideoInfoBean;
import com.zhkrb.netowrk.jsoup.BaseJsoupCallback;
import com.zhkrb.utils.L;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;

/**
 * @description：视频详情页解析
 * @author：zhkrb
 * @DATE： 2021/3/6 13:57
 */
public class VideoInfoFormat {

    public static BaseJsoupCallback.Formatter<VideoInfoBean> formatter = (callback, body) -> {
        Document document = Jsoup.parse(body);
        if (document == null || !document.body().hasText()) {
            callback.onSuccess(null);
            return;
        }
        callback.onSuccess(VideoInfoFormat.videoInfoFormat(document));
    };

    public static VideoInfoBean videoInfoFormat(Document document) {
        VideoInfoBean bean = new VideoInfoBean();
        Element infoElem = document.selectFirst("div.node-info");
        bean.setTitle(BaseFormatUtil.selectTextMayNull(infoElem, "h1.title"));
        bean.setAuthorName(BaseFormatUtil.selectTextMayNull(infoElem, "a.username"));
        String thumb = BaseFormatUtil.selectAttrMayNull(infoElem, "div.user-picture>a>img", "src");
        if (TextUtils.isEmpty(thumb) || thumb.contains("default_avatar.png")) {
            thumb = "default_avatar";
        } else {
            thumb = "https:" + thumb;
        }
        bean.setAuthorThumb(thumb);
        bean.setAuthorHref(infoElem.selectFirst("a.username").attr("href"));
        String[] a = BaseFormatUtil.selectTextMayNull(infoElem, "div.submitted").split("作成日:");
        String date = "";
        if (a.length == 2) {
            date = a[1];
        }
        bean.setAuthorVideoUploadDate(date);
        bean.setAuthorVideoInfo(BaseFormatUtil.selectHtmlMayNull(infoElem, "div.field-item"));
        //TODO format video tag
        Elements tags = infoElem.select("div.field-name-field-categories");

        String[] info = BaseFormatUtil.selectTextMayNull(infoElem, "div.node-views").split(" ");
        bean.setAuthorVideoLike(info.length == 2 ? info[0] : "");
        bean.setAuthorVideoView(info.length == 2 ? info[1] : "");
        Elements from = null;
        Element video_from = document.selectFirst("div.view-id-videos");
        if (video_from != null) {
            from = video_from.select("div.node-video");
        }
        bean.setAuthorVideoFromUser(VideoListFormat.videoListFormat(from));
        Elements recomm = null;
        Element video_recomm = document.selectFirst("div.view-id-search");
        if (video_recomm != null) {
            recomm = video_recomm.select("div.node-video");
        }
        bean.setAuthorVideoRecomm(VideoListFormat.videoListFormat(recomm));
        Element comment = document.selectFirst("#comments");
        bean.setCommentCount(CommentFormatUtil.getCommentCount(comment));
        bean.setCommentPages(CommentFormatUtil.getCommentPages(comment));
        bean.setCommentItemList(CommentFormatUtil.getCommentItemList(document));
        bean.setCommentItemReplyPageid(CommentFormatUtil.getCommentPageId(document));
        return bean;
    }
}
