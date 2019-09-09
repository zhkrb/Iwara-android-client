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

public class AppConfig {

    //域名
    public static final String HOST = "https://ecchi.iwara.tv";
    public static final String GET_VIDEO_API = "/api/video/";

    private static AppConfig sInstance;

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

    private int showLikeBg = 300;

    public int getShowLikeBg() {
        return showLikeBg;
    }

    public void setShowLikeBg(int showLikeBg) {
        this.showLikeBg = showLikeBg;
    }
}
