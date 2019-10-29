package com.zhkrb.iwara.netowrk.jsoup;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhkrb.iwara.utils.RegexUtil;
import com.zhkrb.iwara.utils.TimeUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

    public static String videoListFormat(Elements elements){
        JSONArray array = new JSONArray();
        if (elements == null){
            return array.toJSONString();
        }
        for (Element element:elements){
            JSONObject object = new JSONObject();
            object.put("like",selectTextMayNull(element,"div.right-icon"));
            object.put("view",selectTextMayNull(element,"div.left-icon"));
            object.put("href",element.selectFirst("div.field-item").getElementsByTag("a").attr("href"));
            if (element.selectFirst("img")!=null){
                object.put("thumb","https:"+selectAttrMayNull(element,"img","src"));
                String title = "";
                if (TextUtils.isEmpty(selectAttrMayNull(element,"img","title"))){
                    if (element.selectFirst("h3.title") != null){
                        title = element.selectFirst("h3.title").selectFirst("a").text();
                    }else if (!TextUtils.isEmpty(element.attr("data-original-title"))){
                        title = element.attr("data-original-title");
                    }
                }else {
                    title = selectAttrMayNull(element,"img","title");
                }
                object.put("title",title);
            }else {
                object.put("thumb","");
                object.put("title",selectTextMayNull(element,"h3.title>a"));
            }

            object.put("user_name",selectTextMayNull(element,"a.username"));
            object.put("user_href",selectAttrMayNull(element,"a.username","href"));
            object.put("privated",element.selectFirst("div.private-video") != null);
            object.put("type",0);
            array.add(object);
        }
        return array.toJSONString();
    }

    private static String  selectTextMayNull(Element element,String s) {
        Elements elements = element.select(s);
        if (elements.size()==0){
            return "";
        }
        return elements.get(0).text();
    }

    private static String selectHtmlMayNull(Element element, String s){
        Elements elements = element.select(s);
        if (elements.size()==0){
            return "";
        }
        return elements.get(0).html();
    }

    private static String selectAttrMayNull(Element element, String s, String attr){
        Elements elements = element.select(s);
        if (elements.size()==0){
            return "";
        }
        return elements.get(0).attr(attr);
    }


    public static String videoInfoFormat(Document document) {
        JSONObject object = new JSONObject();
        Element infoElem = document.selectFirst("div.node-info");
        object.put("title",selectTextMayNull(infoElem,"h1.title"));
        object.put("author_name",selectTextMayNull(infoElem,"a.username"));
        String thumb = selectAttrMayNull(infoElem,"div.user-picture>a>img","src");
        if (TextUtils.isEmpty(thumb) || thumb.contains("default_avatar.png")){
            thumb = "default_avatar";
        }else {
            thumb = "https:"+thumb;
        }
        object.put("author_thumb",thumb);
        object.put("author_href",infoElem.selectFirst("a.username").attr("href"));
        String[] a = selectTextMayNull(infoElem,"div.submitted").split("作成日:");
        String date = "";
        if (a.length == 2){
            date = a[1];
        }
        object.put("author_video_upload_date",date);
        object.put("author_video_info",selectHtmlMayNull(infoElem,"div.field-item"));
        //TODO format video tag
        Elements tags = infoElem.select("div.field-name-field-categories");

        String[] info = selectTextMayNull(infoElem,"div.node-views").split(" ");
        object.put("author_video_like",info.length == 2 ? info[0] : "");
        object.put("author_video_view",info.length == 2 ? info[1] : "");
        Elements from = null;
        Element video_from = document.selectFirst("div.view-id-videos");
        if (video_from != null){
            from = video_from.select("div.node-video");
        }
        object.put("author_video_from_user",JSON.parseArray(videoListFormat(from)));
        Elements recomm = null;
        Element video_recomm = document.selectFirst("div.view-id-search");
        if (video_recomm != null){
            recomm = video_recomm.select("div.node-video");
        }
        object.put("author_video_recomm", JSON.parseArray(videoListFormat(recomm)));
        Element comment = document.selectFirst("#comments");
        object.put("comment_count", getCommentCount(comment));
        object.put("comment_pages", getCommentPages(comment));
        object.put("comment_item_list", getCommentItemList(document));
        object.put("comment_item_reply_pageid",getCommentPageId(document));
        return object.toJSONString();
    }

    private static String getCommentPageId(Document document) {
        if (document.selectFirst("#block-system-main>div.content") == null){
            return "";
        }
        return document.selectFirst("#block-system-main>div.content").
                child(0).attr("id").split("node-")[1];
    }

    private static JSONArray getCommentItemList(Element document) {
        Elements elements = document.select("#comments>div.comment,#comments>div.indented,#comments>a");
        if (elements == null || elements.size() == 0){
            return new JSONArray();
        }
        JSONArray array = new JSONArray();
        for (Element item : elements){
            if ("a".equals(item.tagName())){
                String id = item.attr("id");
                JSONObject object = new JSONObject();
                object.put("comment_id",!TextUtils.isEmpty(id) ? id.split("comment-")[1] : "");
                object.put("reply_list",new JSONArray());
                array.add(object);
            }else if (item.className().contains("comment")){
                ((JSONObject)array.get(array.size() - 1)).putAll(getCommentItem(item));
            }else {
                JSONArray jsonArray = new JSONArray();
                int replyCount = getCommentReply(item,
                        ((JSONObject) array.get(array.size() - 1)).getString("comment_id"), jsonArray);
                ((JSONObject) array.get(array.size() - 1)).put("reply_list", jsonArray);
                ((JSONObject) array.get(array.size() - 1)).put("reply_count", replyCount);
            }
        }
        return array;
    }

    private static int getCommentReply(Element element, String comment_id, JSONArray jsonArray) {
        Elements elements = element.children();
        if (elements == null || elements.size() == 0){
            return 0;
        }
        Stack<Node> stack = new Stack<>();
        List<JSONObject> sortList = new ArrayList<>();
        List<Node> nodeList = formartNode(elements,comment_id,null);
        stack.addAll(nodeList);
        while (!stack.isEmpty()){
            Node node = stack.pop();
            JSONObject object = getCommentItem(node.getCom());
            object.put("comment_reply_idlist",node.getReply().toString());
            object.put("comment_id",node.getId());
            sortList.add(object);
            if (node.getInd()!=null&&node.getInd().size()>0){
                List<Node> nodes = formartNode(node.getInd(),node.getId(),node.getReply());
                stack.addAll(nodes);
            }
        }
        Collections.sort(sortList, (o1, o2) ->
                o1.getLong("comment_date_stamp").compareTo(o2.getLong("comment_date_stamp")));
        jsonArray.addAll(sortList);
        return jsonArray.size();
    }

    private static List<Node> formartNode(Elements elements, String comment_id,List<String> comment_parent_id){
        List<Node> nodeList = new ArrayList<>();
        for (int i=0;i<elements.size();i++){
            Element item = elements.get(i);
            if ("a".equals(item.tagName())){
                String id = item.attr("id");
                nodeList.add(new Node(!TextUtils.isEmpty(id) ? id.split("comment-")[1] : ""));
            }else if (item.className().contains("comment")){
                nodeList.get(nodeList.size() - 1).setCom(item);
                List<String> strings = new ArrayList<>();
                if (comment_parent_id!=null){
                    strings.addAll(comment_parent_id);
                }
                strings.add(comment_id);
                nodeList.get(nodeList.size() - 1).setReply(strings);
            }else{
                nodeList.get(nodeList.size() - 1).setInd(item.children());
            }
        }
        return nodeList;
    }

    private static JSONObject getCommentItem(Element element) {
        JSONObject object = new JSONObject();
        List<String> list = RegexUtil.regex(element.selectFirst("div.submitted").text(),"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        String date = "";
        if (list != null && list.size()>0){
            date = list.get(0);
        }
        object.put("comment_date", date);
        object.put("comment_date_stamp", TimeUtil.getStringToDate(date,"yyyy-MM-dd HH:mm"));
        object.put("comment_user",selectTextMayNull(element,"div.submitted>a"));
        String thumb = selectAttrMayNull(element,"div.user-picture>a>img","src");
        if (TextUtils.isEmpty(thumb) || thumb.contains("default_avatar.png")){
            thumb = "default_avatar";
        }else {
            thumb = "https:"+thumb;
        }
        object.put("comment_thumb",thumb);
        object.put("comment_content",selectHtmlMayNull(element,"div.field-item"));
        return object;
    }

    private static int getCommentPages(Element comment) {
        int page = 1;
        if (comment != null &&comment.selectFirst("li.pager-last") != null){
            Element element = comment.selectFirst("li.pager-last");
            String p = selectAttrMayNull(element,"a","href");
            if (!TextUtils.isEmpty(p)){
                page = Integer.parseInt(p.split("/?page=")[1]);
            }
        }
        return page;
    }

    private static int getCommentCount(Element element){
        if (element == null ||element.selectFirst("h2.title") == null){
            return 0;
        }
        String[] count = element.selectFirst("h2.title").text().split(" ");
        for (String s : count){
            if (isNumeric(s)){
                return Integer.parseInt(s);
            }
        }
        return 0;
    }


    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    static class Node{

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
