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

package com.zhkrb.iwara.utils;

import com.zhkrb.iwara.AppConfig;
import com.zhkrb.iwara.bean.VideoListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortUtil {

    public static result SortVideoList(List<VideoListBean> beanList,boolean isLastSingle){
        boolean single = isLastSingle;
        int likeBg = AppConfig.getInstance().getShowLikeBg();
        List<VideoListBean> list = new ArrayList<>(beanList.size());
        List<VideoListBean> bgList = new ArrayList<>(0);
        for (int i=0;i<beanList.size();i++){
            int like = Integer.valueOf(beanList.get(i).getLike());
            if (single){
                if (like > likeBg){
                    bgList.add(beanList.get(i));
                    if (i == beanList.size() -1){
                        list.addAll(bgList);
                    }
                }else {
                    list.add(beanList.get(i));
                    if (bgList.size()>0){
                        list.addAll(bgList);
                        bgList.clear();
                    }
                    single = false;
                }
            }else {
                list.add(beanList.get(i));
                single = like <= likeBg;
            }
        }
        int bgSize = bgList.size();
//        VideoListBean bean = beanList.get(beanList.size()-1);
//        if (single
//                && (Integer.valueOf(list.get(list.size()-1).getLike())>likeBg)){
//            if (Integer.valueOf(bean.getLike())>likeBg){
//                list.add(bean);
//                bgSize = bgList.size()+1;
//            }else {
//                list.add(list.size()-bgList.size(),bean);
//                single = false;
//            }
//            list.add(list.size()-2,beanList.get(beanList.size()-1));
//        }else {
//            list.add(beanList.get(beanList.size()-1));
//        }
        bgList.clear();
        return new result(list,single,bgSize);
    }

    public static class result{
        List<VideoListBean> mList;
        boolean isLastSignle;
        int lastBgSize = 0;

        public result(List<VideoListBean> list, boolean isLastSignle, int lastBgSize) {
            mList = list;
            this.isLastSignle = isLastSignle;
            this.lastBgSize = lastBgSize;
        }

        public int getLastBgSize() {
            return lastBgSize;
        }

        public void setLastBgSize(int lastBgSize) {
            this.lastBgSize = lastBgSize;
        }

        public List<VideoListBean> getList() {
            return mList;
        }

        public void setList(List<VideoListBean> list) {
            mList = list;
        }

        public boolean isLastSignle() {
            return isLastSignle;
        }

        public void setLastSignle(boolean lastSignle) {
            isLastSignle = lastSignle;
        }
    }


}
