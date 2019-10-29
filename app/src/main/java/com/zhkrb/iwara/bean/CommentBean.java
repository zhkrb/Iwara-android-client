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
 * Create by zhkrb on 2019/10/26 21:55
 */

package com.zhkrb.iwara.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CommentBean  implements Parcelable {

    private long comment_date_stamp;
    private String comment_user;
    private String comment_date;
    private String comment_content;
    private String comment_id;
    private String comment_thumb;
    private List<CommentBean> reply_list;
    private int reply_count;

    public CommentBean() {
    }

    public long getComment_date_stamp() {
        return comment_date_stamp;
    }

    public void setComment_date_stamp(long comment_date_stamp) {
        this.comment_date_stamp = comment_date_stamp;
    }

    public String getComment_user() {
        return comment_user;
    }

    public void setComment_user(String comment_user) {
        this.comment_user = comment_user;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_thumb() {
        return comment_thumb;
    }

    public void setComment_thumb(String comment_thumb) {
        this.comment_thumb = comment_thumb;
    }

    public List<CommentBean> getReply_list() {
        return reply_list;
    }

    public void setReply_list(List<CommentBean> reply_list) {
        this.reply_list = reply_list;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.comment_date_stamp);
        dest.writeString(this.comment_user);
        dest.writeString(this.comment_date);
        dest.writeString(this.comment_content);
        dest.writeString(this.comment_id);
        dest.writeString(this.comment_thumb);
        dest.writeInt(this.reply_count);
        dest.writeList(this.reply_list);
    }

    public CommentBean(Parcel in) {
        this.comment_date_stamp = in.readLong();
        this.comment_user = in.readString();
        this.comment_date = in.readString();
        this.comment_content = in.readString();
        this.comment_id = in.readString();
        this.comment_thumb = in.readString();
        this.reply_count = in.readInt();
        this.reply_list = in.createTypedArrayList(CommentBean.CREATOR);
    }

    public static final Parcelable.Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
        @Override
        public CommentBean[] newArray(int size) {
            return new CommentBean[size];
        }

        @Override
        public CommentBean createFromParcel(Parcel in) {
            return new CommentBean(in);
        }
    };
}
