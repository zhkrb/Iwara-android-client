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

    private long commentDateStamp;
    private String commentUser;
    private String commentDate;
    private String commentContent;
    private String commentId;
    private String commentThumb;
    private List<CommentBean> replyList;
    private int replyCount;
    private String commentReplyIdList;

    public CommentBean() {
    }

    public long getCommentDateStamp() {
        return commentDateStamp;
    }

    public void setCommentDateStamp(long commentDateStamp) {
        this.commentDateStamp = commentDateStamp;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentThumb() {
        return commentThumb;
    }

    public void setCommentThumb(String commentThumb) {
        this.commentThumb = commentThumb;
    }

    public List<CommentBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<CommentBean> replyList) {
        this.replyList = replyList;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.commentDateStamp);
        dest.writeString(this.commentUser);
        dest.writeString(this.commentDate);
        dest.writeString(this.commentContent);
        dest.writeString(this.commentId);
        dest.writeString(this.commentThumb);
        dest.writeInt(this.replyCount);
        dest.writeList(this.replyList);
        dest.writeString(this.commentReplyIdList);
    }

    public CommentBean(Parcel in) {
        this.commentDateStamp = in.readLong();
        this.commentUser = in.readString();
        this.commentDate = in.readString();
        this.commentContent = in.readString();
        this.commentId = in.readString();
        this.commentThumb = in.readString();
        this.replyCount = in.readInt();
        this.replyList = in.createTypedArrayList(CommentBean.CREATOR);
        this.commentReplyIdList = in.readString();
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

    public String getCommentReplyIdList() {
        return commentReplyIdList;
    }

    public void setCommentReplyIdList(String commentReplyIdList) {
        this.commentReplyIdList = commentReplyIdList;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "commentDateStamp=" + commentDateStamp +
                ", commentUser='" + commentUser + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", commentId='" + commentId + '\'' +
                ", commentThumb='" + commentThumb + '\'' +
                ", replyList=" + replyList +
                ", replyCount=" + replyCount +
                ", commentReplyIdList='" + commentReplyIdList + '\'' +
                '}';
    }
}
