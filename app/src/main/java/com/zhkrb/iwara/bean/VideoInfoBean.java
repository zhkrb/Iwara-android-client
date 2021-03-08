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

public class VideoInfoBean{

    private String authorName;
    private String authorThumb;
    private int commentCount;
    private String authorHref;
    private List<VideoListBean> authorVideoRecomm;
    private String authorVideoInfo;
    private String authorVideoUploadDate;
    private List<VideoListBean> authorVideoFromUser;
    private String commentItemReplyPageid;
    private String authorVideoLike;
    private String title;
    private String authorVideoView;
    private List<CommentBean> commentItemList;
    private int commentPages;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorThumb() {
        return authorThumb;
    }

    public void setAuthorThumb(String authorThumb) {
        this.authorThumb = authorThumb;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getAuthorHref() {
        return authorHref;
    }

    public void setAuthorHref(String authorHref) {
        this.authorHref = authorHref;
    }

    public List<VideoListBean> getAuthorVideoRecomm() {
        return authorVideoRecomm;
    }

    public void setAuthorVideoRecomm(List<VideoListBean> authorVideoRecomm) {
        this.authorVideoRecomm = authorVideoRecomm;
    }

    public String getAuthorVideoInfo() {
        return authorVideoInfo;
    }

    public void setAuthorVideoInfo(String authorVideoInfo) {
        this.authorVideoInfo = authorVideoInfo;
    }

    public String getAuthorVideoUploadDate() {
        return authorVideoUploadDate;
    }

    public void setAuthorVideoUploadDate(String authorVideoUploadDate) {
        this.authorVideoUploadDate = authorVideoUploadDate;
    }

    public List<VideoListBean> getAuthorVideoFromUser() {
        return authorVideoFromUser;
    }

    public void setAuthorVideoFromUser(List<VideoListBean> authorVideoFromUser) {
        this.authorVideoFromUser = authorVideoFromUser;
    }

    public String getCommentItemReplyPageid() {
        return commentItemReplyPageid;
    }

    public void setCommentItemReplyPageid(String commentItemReplyPageid) {
        this.commentItemReplyPageid = commentItemReplyPageid;
    }

    public String getAuthorVideoLike() {
        return authorVideoLike;
    }

    public void setAuthorVideoLike(String authorVideoLike) {
        this.authorVideoLike = authorVideoLike;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorVideoView() {
        return authorVideoView;
    }

    public void setAuthorVideoView(String authorVideoView) {
        this.authorVideoView = authorVideoView;
    }

    public List<CommentBean> getCommentItemList() {
        return commentItemList;
    }

    public void setCommentItemList(List<CommentBean> commentItemList) {
        this.commentItemList = commentItemList;
    }

    public int getCommentPages() {
        return commentPages;
    }

    public void setCommentPages(int commentPages) {
        this.commentPages = commentPages;
    }

    @Override
    public String toString() {
        return "VideoInfoBean{" +
                "authorName='" + authorName + '\'' +
                ", authorThumb='" + authorThumb + '\'' +
                ", commentCount=" + commentCount +
                ", authorHref='" + authorHref + '\'' +
                ", authorVideoRecomm=" + authorVideoRecomm +
                ", authorVideoInfo='" + authorVideoInfo + '\'' +
                ", authorVideoUploadDate='" + authorVideoUploadDate + '\'' +
                ", authorVideoFromUser=" + authorVideoFromUser +
                ", commentItemReplyPageid='" + commentItemReplyPageid + '\'' +
                ", authorVideoLike='" + authorVideoLike + '\'' +
                ", title='" + title + '\'' +
                ", authorVideoView='" + authorVideoView + '\'' +
                ", commentItemList=" + commentItemList +
                ", commentPages=" + commentPages +
                '}';
    }
}
