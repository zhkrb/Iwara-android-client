package com.zhkrb.iwara.utils;


import com.zhkrb.iwara.AppContext;

/**
 * Created by cxf on 2017/8/9.
 * dp转px工具类
 */

public class DpUtil {

    private static float scale;

    static {
        scale = AppContext.sInstance.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(int dpVal) {
        return (int) (scale * dpVal + 0.5f);
    }
}
