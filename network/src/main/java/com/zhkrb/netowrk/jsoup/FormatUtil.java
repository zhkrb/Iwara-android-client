package com.zhkrb.netowrk.jsoup;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FormatUtil {

    public static String videoListFormat(Elements elements){
        JSONArray array = new JSONArray();
        for (Element element:elements){
            JSONObject object = new JSONObject();
            object.put("like",selectTextMayNull(element,"div.right-icon"));
            object.put("view",selectTextMayNull(element,"div.left-icon"));
            object.put("href",element.selectFirst("div.field-item").getElementsByTag("a").attr("href"));
            if (element.selectFirst("img")!=null){
                object.put("thumb","https:"+element.selectFirst("img").attr("src"));
                String title;
                if (TextUtils.isEmpty(element.selectFirst("img").attr("title"))){
                    title = element.selectFirst("h3.title").selectFirst("a").text();
                }else {
                    title = element.selectFirst("img").attr("title");
                }
                object.put("title",title);
            }else {
                object.put("thumb","");
                object.put("title",element.selectFirst("h3.title").selectFirst("a").text());
            }

            object.put("user_name",element.selectFirst("a.username").text());
            object.put("user_href",element.selectFirst("a.username").attr("href"));
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



}
