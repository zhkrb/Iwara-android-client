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
 * Create by zhkrb on 2019/10/25 14:30
 */

package com.zhkrb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    /**
     * 获取系统时间戳
     * @return
     */
    public long getCurTimeLong(){
        return System.currentTimeMillis();
    }
    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

}
