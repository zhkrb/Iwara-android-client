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

package com.zhkrb.iwara;

import com.zhkrb.iwara.utils.SpUtil;

public class AppConfig {

    //域名
    public static final String HOST = "https://ecchi.iwara.tv";
    public static final String GET_VIDEO_API = "/api/video/";
    public static final String UPDATE_URL = "https://iw.zhkrb.com/update";
    public static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";

    private static AppConfig sInstance;

    private static final int showLikeBg = 300;

    private AppConfig() {

    }

    public static AppConfig getInstance() {
        if (sInstance == null) {
            synchronized (AppConfig.class) {
                if (sInstance == null) {
                    sInstance = new AppConfig();
                }
            }
        }
        return sInstance;
    }

    public int getShowLikeBg() {
        return SpUtil.getInstance().getIntValue(SpUtil.SHOW_LIKE_BG,showLikeBg);
    }

    //喜好数大于多少时显示大图
    public void setShowLikeBg(int showLikeBg) {
        SpUtil.getInstance().setIntValue(SpUtil.SHOW_LIKE_BG,showLikeBg);
    }

    public long getVersionCode(){
        return SpUtil.getInstance().getLongValue(SpUtil.VERSION_CODE,0);
    }

    public void setVersionCode(long code){
        SpUtil.getInstance().setLongValue(SpUtil.VERSION_CODE,code);
    }

    public void putIgnoreVersion(int version) {
        SpUtil.getInstance().setIntValue(SpUtil.IGNORE_VERSION,version);
    }

    public int getIgnoreVersion(){
        return SpUtil.getInstance().getIntValue(SpUtil.IGNORE_VERSION,0);
    }

    public int getGalleryMode() {
        return SpUtil.getInstance().getIntValue(SpUtil.GALLERY_MODE,0);
    }

    public int getGalleryListMode(int mode) {
        return SpUtil.getInstance().getIntValue(SpUtil.INDEX_VIDEO_LIST_MODE,
                mode == 0 ? 2 : 0);
    }

    public void putGalleryListMode(String key, int mode) {
        SpUtil.getInstance().setIntValue(key,mode);
    }
}
