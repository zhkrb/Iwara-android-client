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
}
