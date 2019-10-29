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
 * Create by zhkrb on 2019/10/26 22:32
 */

package com.zhkrb.iwara.bean;

import java.util.List;

public class VideoInfoBean {

    private String author_name;
    private String author_thumb;
    private int comment_count;
    private String author_href;
    private List<VideoListBean> author_video_recomm;
    private String author_video_info;
    private String author_video_upload_date;
    private List<VideoListBean> author_video_from_user;
    private String comment_item_reply_pageid;
    private String author_video_like;
    private String title;
    private String author_video_view;
    private List<CommentBean> comment_item_list;
    private int comment_pages;

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_thumb() {
        return author_thumb;
    }

    public void setAuthor_thumb(String author_thumb) {
        this.author_thumb = author_thumb;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getAuthor_href() {
        return author_href;
    }

    public void setAuthor_href(String author_href) {
        this.author_href = author_href;
    }

    public List<VideoListBean> getAuthor_video_recomm() {
        return author_video_recomm;
    }

    public void setAuthor_video_recomm(List<VideoListBean> author_video_recomm) {
        this.author_video_recomm = author_video_recomm;
    }

    public String getAuthor_video_info() {
        return author_video_info;
    }

    public void setAuthor_video_info(String author_video_info) {
        this.author_video_info = author_video_info;
    }

    public String getAuthor_video_upload_date() {
        return author_video_upload_date;
    }

    public void setAuthor_video_upload_date(String author_video_upload_date) {
        this.author_video_upload_date = author_video_upload_date;
    }

    public List<VideoListBean> getAuthor_video_from_user() {
        return author_video_from_user;
    }

    public void setAuthor_video_from_user(List<VideoListBean> author_video_from_user) {
        this.author_video_from_user = author_video_from_user;
    }

    public String getComment_item_reply_pageid() {
        return comment_item_reply_pageid;
    }

    public void setComment_item_reply_pageid(String comment_item_reply_pageid) {
        this.comment_item_reply_pageid = comment_item_reply_pageid;
    }

    public String getAuthor_video_like() {
        return author_video_like;
    }

    public void setAuthor_video_like(String author_video_like) {
        this.author_video_like = author_video_like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor_video_view() {
        return author_video_view;
    }

    public void setAuthor_video_view(String author_video_view) {
        this.author_video_view = author_video_view;
    }

    public List<CommentBean> getComment_item_list() {
        return comment_item_list;
    }

    public void setComment_item_list(List<CommentBean> comment_item_list) {
        this.comment_item_list = comment_item_list;
    }

    public int getComment_pages() {
        return comment_pages;
    }

    public void setComment_pages(int comment_pages) {
        this.comment_pages = comment_pages;
    }
}
