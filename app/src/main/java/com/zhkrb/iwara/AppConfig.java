package com.zhkrb.iwara;

public class AppConfig {

    //域名
    public static final String HOST = "https://ecchi.iwara.tv";

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
