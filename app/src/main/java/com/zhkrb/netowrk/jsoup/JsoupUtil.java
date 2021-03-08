package com.zhkrb.netowrk.jsoup;

import com.zhkrb.iwara.bean.VideoInfoBean;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.netowrk.jsoup.foramter.VideoInfoFormat;
import com.zhkrb.netowrk.jsoup.foramter.VideoListFormat;

import java.util.List;

public class JsoupUtil {

    /**
     * @param type     排序类型
     * @param p        页数
     * @param callback json格式：
     *                 [
     *                 {
     *                 "like" : String,
     *                 "view" : String,
     *                 "href" : String,
     *                 "thumb" : String,
     *                 "title" : String,
     *                 "user_name" : String,
     *                 "user_href" : String,
     *                 "private" : boolean
     *                 "type" : int       //0 视频 1 图片
     *                 }
     *                 ...
     *                 ]
     */
    public static void getVideoList(int type, int p, String tag, final BaseJsoupCallback<List<VideoListBean>> callback) {
        String url = "/videos?sort=";
//        switch (type){
//            case 0://日期
//                url += "date";
//                break;
//            case 1://点击
//                url += "views";
//                break;
//            case 2://like
//                url += "likes";
//                break;
//        }
        url += "likes&f%5B0%5D=created%3A2020";
        if (p > 1) {
//            url += "&page="+ (p - 1);
            url = "/vides?sort=";
        }
//        JsoupClient.getInstance().getObservable("/videos/wqlwatgmvhqg40kg").subscribe(observer);
        JsoupClient.getInstance().getObservable(url).subscribe(callback
                .setFormatter(VideoListFormat.formatter)
                .addTag(tag));
    }

    /**
     * @param url      页面链接
     * @param callback json格式:
     *                 <p>
     *                 {
     *                 "author_name": "作者名称",
     *                 "author_thumb": "作者头像",
     *                 "comment_count": 评论数, int
     *                 "author_href": "作者主页",
     *                 "author_video_recomm": "相关视频(类型同视频列表，缺少作者名)",
     *                 "author_video_info": "视频简介",
     *                 "author_video_upload_date": "上传日期",
     *                 "author_video_from_user": "作者其他视频",
     *                 "comment_item_reply_pageid": "本页评论id",
     *                 "author_video_like": "like数",
     *                 "title": "标题",
     *                 "author_video_view": "观看数",
     *                 "comment_item_list": "评论",
     *                 "comment_pages": 评论页数 int
     *                 }
     *                 <p>
     *                 <p>
     *                 评论
     *                 [
     *                 {
     *                 "comment_date_stamp": 时间戳,
     *                 "comment_user": "用户名",
     *                 "comment_date": "时间",
     *                 "comment_content": "内容",
     *                 "comment_id": "本条评论id",
     *                 "comment_thumb": "头像",
     *                 "reply_list": "回复",
     *                 "reply_count": "回复消息数",
     *                 ""
     *                 }
     *                 ]
     */

    public static void getVideoInfo(String url, String tag,  BaseJsoupCallback<VideoInfoBean> callback) {
        JsoupClient.getInstance().getObservable(url).subscribe(callback.
                setFormatter(VideoInfoFormat.formatter).
                addTag(tag));
    }

}
