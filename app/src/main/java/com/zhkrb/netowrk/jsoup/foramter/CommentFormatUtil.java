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
 * Create by zhkrb on 2021-03-06 14:06:58
 *
 */

package com.zhkrb.netowrk.jsoup.foramter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhkrb.iwara.bean.CommentBean;
import com.zhkrb.utils.RegexUtil;
import com.zhkrb.utils.TimeUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description：评论格式化
 * @author：zhkrb
 * @DATE： 2021/3/6 14:06
 */
public class CommentFormatUtil {


    public static String getCommentPageId(Document document) {
        if (document.selectFirst("#block-system-main>div.content") == null) {
            return "";
        }
        return document.selectFirst("#block-system-main>div.content").
                child(0).attr("id").split("node-")[1];
    }

    public static List<CommentBean> getCommentItemList(Element document) {
        List<CommentBean> commentBeanList = new ArrayList<>();
        Elements elements = document.select("#comments>div.comment,#comments>div.indented,#comments>a");
        if (elements == null || elements.size() == 0) {
            return commentBeanList;
        }
        for (Element item : elements) {
            if ("a".equals(item.tagName())) {
                String id = item.attr("id");
                CommentBean bean = new CommentBean();
                bean.setCommentId(!TextUtils.isEmpty(id) ? id.split("comment-")[1] : "");
                bean.setReplyList(new ArrayList<>());
                commentBeanList.add(bean);
            } else if (item.className().contains("comment")) {
                getCommentItem(item, commentBeanList.get(commentBeanList.size() - 1));
            } else {
                List<CommentBean> replyList = new ArrayList<>();
                int replyCount = getCommentReply(item, commentBeanList.get(commentBeanList.size() - 1).getCommentId(), replyList);
                commentBeanList.get(commentBeanList.size() - 1).setReplyList(replyList);
                commentBeanList.get(commentBeanList.size() - 1).setReplyCount(replyCount);
            }
        }
        return commentBeanList;
    }

    public static int getCommentReply(Element element, String commentId, List<CommentBean> jsonArray) {
        Elements elements = element.children();
        if (elements == null || elements.size() == 0) {
            return 0;
        }
        Stack<Node> stack = new Stack<>();
        List<CommentBean> sortList = new ArrayList<>();
        List<Node> nodeList = formartNode(elements, commentId, null);
        stack.addAll(nodeList);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            CommentBean bean = new CommentBean();
            getCommentItem(node.getCom(), bean);
            bean.setCommentReplyIdList(node.getReply().toString());
            bean.setCommentId(node.getId());
            sortList.add(bean);
            if (node.getInd() != null && node.getInd().size() > 0) {
                List<Node> nodes = formartNode(node.getInd(), node.getId(), node.getReply());
                stack.addAll(nodes);
            }
        }
        Collections.sort(sortList, (o1, o2) -> {
                    return Long.compare(o1.getCommentDateStamp(), o2.getCommentDateStamp());
                }
        );
        jsonArray.addAll(sortList);
        return jsonArray.size();
    }

    public static List<Node> formartNode(Elements elements, String comment_id, List<String> comment_parent_id) {
        List<Node> nodeList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            Element item = elements.get(i);
            if ("a".equals(item.tagName())) {
                String id = item.attr("id");
                nodeList.add(new Node(!TextUtils.isEmpty(id) ? id.split("comment-")[1] : ""));
            } else if (item.className().contains("comment")) {
                nodeList.get(nodeList.size() - 1).setCom(item);
                List<String> strings = new ArrayList<>();
                if (comment_parent_id != null) {
                    strings.addAll(comment_parent_id);
                }
                strings.add(comment_id);
                nodeList.get(nodeList.size() - 1).setReply(strings);
            } else {
                nodeList.get(nodeList.size() - 1).setInd(item.children());
            }
        }
        return nodeList;
    }

    public static void getCommentItem(Element element, CommentBean bean) {
        List<String> list = RegexUtil.regex(element.selectFirst("div.submitted").text(), "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        String date = "";
        if (list != null && list.size() > 0) {
            date = list.get(0);
        }
        bean.setCommentDate(date);
        bean.setCommentDateStamp(TimeUtil.getStringToDate(date, "yyyy-MM-dd HH:mm"));
        bean.setCommentUser(BaseFormatUtil.selectTextMayNull(element, "div.submitted>a"));

        String thumb = BaseFormatUtil.selectAttrMayNull(element, "div.user-picture>a>img", "src");
        if (TextUtils.isEmpty(thumb) || thumb.contains("default_avatar.png")) {
            thumb = "default_avatar";
        } else {
            thumb = "https:" + thumb;
        }
        bean.setCommentThumb(thumb);
        bean.setCommentContent(BaseFormatUtil.selectHtmlMayNull(element, "div.field-item"));
    }

    public static int getCommentPages(Element comment) {
        int page = 1;
        if (comment != null && comment.selectFirst("li.pager-last") != null) {
            Element element = comment.selectFirst("li.pager-last");
            String p = BaseFormatUtil.selectAttrMayNull(element, "a", "href");
            if (!TextUtils.isEmpty(p)) {
                List<String> list = RegexUtil.regex(p, "/?page=(.*\\d)");
                if (list != null && list.size() > 0) {
                    page = Integer.parseInt(list.get(0).split("page=")[1]);
                }

            }
        }
        return page;
    }

    public static int getCommentCount(Element element) {
        if (element == null || element.selectFirst("h2.title") == null) {
            return 0;
        }
        String[] count = element.selectFirst("h2.title").text().split(" ");
        for (String s : count) {
            if (isNumeric(s)) {
                return Integer.parseInt(s);
            }
        }
        return 0;
    }

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[0-9]*");

    public static boolean isNumeric(String str) {
        Matcher isNum = NUMERIC_PATTERN.matcher(str);
        return isNum.matches();
    }

    public static class Node {

        String id;
        Element com;
        Elements ind;
        List<String> reply = new ArrayList<>();

        Node(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Element getCom() {
            return com;
        }

        public void setCom(Element com) {
            this.com = com;
        }

        Elements getInd() {
            return ind;
        }

        void setInd(Elements ind) {
            this.ind = ind;
        }

        List<String> getReply() {
            return reply;
        }

        void setReply(List<String> reply) {
            this.reply = reply;
        }
    }


}
