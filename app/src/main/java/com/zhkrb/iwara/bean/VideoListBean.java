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

package com.zhkrb.iwara.bean;

import android.text.TextUtils;

public class VideoListBean {

    public VideoListBean() {
    }

    private String like;
    private String view;
    private String href;
    private String thumb;
    private String title;
    private String user_name;
    private String user_href;
    private String play_href;
    private int type;

    public String getLike() {
        if (TextUtils.isEmpty(like)){
            return "0";
        }
        return like;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_href() {
        return user_href;
    }

    public void setUser_href(String user_href) {
        this.user_href = user_href;
    }

    public String getPlay_href() {
        return play_href;
    }

    public void setPlay_href(String play_href) {
        this.play_href = play_href;
    }
}
