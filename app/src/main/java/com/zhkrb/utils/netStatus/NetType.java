package com.zhkrb.utils.netStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * @description：网络状态枚举
 * @author：zhkrb
 * @DATE： 2020/8/18 9:56
 */
@IntDef({
        NetType.WIFI,
        NetType.NET,
        NetType.NET_UNKNOWN,
        NetType.NONE
        })
@Retention(RetentionPolicy.SOURCE)
public @interface NetType {

    int WIFI = 0;

    int NET = 1;

    int NET_UNKNOWN = 2;

    int NONE = 3;

}
