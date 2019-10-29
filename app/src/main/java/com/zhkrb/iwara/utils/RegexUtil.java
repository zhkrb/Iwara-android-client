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
 * Create by zhkrb on 2019/10/24 23:16
 */

package com.zhkrb.iwara.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    public static List<String> regex(String text, String pattern){
        try {
            Pattern pt = Pattern.compile(pattern);
            Matcher mt = pt.matcher(text);
            List<String> group = new ArrayList<>();
            while (mt.find()) {
                group.add(mt.group());
            }
            return group;
        }catch (NullPointerException e){
            Log.i("MATCH","null");
        }
        return null;
    }


}
