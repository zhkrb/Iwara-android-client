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

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class VideoListBean implements Parcelable {

    public VideoListBean() {
    }

    private String like;
    private String view;
    private String href;
    private String thumb;
    private String title;
    private String userName;
    private String userHref;
    private boolean privated;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHref() {
        return userHref;
    }

    public void setUserHref(String userHref) {
        this.userHref = userHref;
    }

    public boolean isPrivated() {
        return privated;
    }

    public void setPrivated(boolean privated) {
        this.privated = privated;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.like);
        dest.writeString(this.view);
        dest.writeString(this.href);
        dest.writeString(this.thumb);
        dest.writeString(this.title);
        dest.writeString(this.userName);
        dest.writeString(this.userHref);
        dest.writeInt(this.privated ? 1 : 0);
        dest.writeInt(this.type);
    }

    public VideoListBean(Parcel in) {
        this.like = in.readString();
        this.view = in.readString();
        this.href = in.readString();
        this.thumb = in.readString();
        this.title = in.readString();
        this.userName = in.readString();
        this.userHref = in.readString();
        this.privated = in.readInt() == 1;
        this.type = in.readInt();
    }


    public static final Parcelable.Creator<VideoListBean> CREATOR = new Creator<VideoListBean>() {
        @Override
        public VideoListBean[] newArray(int size) {
            return new VideoListBean[size];
        }

        @Override
        public VideoListBean createFromParcel(Parcel in) {
            return new VideoListBean(in);
        }
    };

    @Override
    public String toString() {
        return "VideoListBean{" +
                "like='" + like + '\'' +
                ", view='" + view + '\'' +
                ", href='" + href + '\'' +
                ", thumb='" + thumb + '\'' +
                ", title='" + title + '\'' +
                ", userName='" + userName + '\'' +
                ", userHref='" + userHref + '\'' +
                ", privated=" + privated +
                ", type=" + type +
                '}';
    }
}
