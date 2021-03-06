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
 * Create by zhkrb on 2021-03-06 13:55:19
 *
 */

package com.zhkrb.netowrk.jsoup.foramter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @description：基础工具类
 * @author：zhkrb
 * @DATE： 2021/3/6 13:55
 */
public class BaseFormatUtil {

    public static String  selectTextMayNull(Element element, String s) {
        Elements elements = element.select(s);
        if (elements.size()==0){
            return "";
        }
        return elements.get(0).text();
    }

    public static String selectHtmlMayNull(Element element, String s){
        Elements elements = element.select(s);
        if (elements.size()==0){
            return "";
        }
        return elements.get(0).html();
    }

    public static String selectAttrMayNull(Element element, String s, String attr){
        Elements elements = element.select(s);
        if (elements.size()==0){
            return "";
        }
        return elements.get(0).attr(attr);
    }


}
