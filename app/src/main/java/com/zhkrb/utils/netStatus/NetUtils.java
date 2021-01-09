package com.zhkrb.utils.netStatus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.zhkrb.iwara.AppContext;


/**
 * @description：网络状态获取
 * @author：zhkrb
 * @DATE： 2020/8/18 11:00
 */
public class NetUtils {


    public static  @NetType int getNetStatus(NetworkCapabilities capabilities){
//        if (!capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
//            return NetType.NONE;
//        }
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                ||capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)){
            return NetType.WIFI;
        }
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                ||capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
            return NetType.NET;
        }
        return NetType.NET_UNKNOWN;
    }


    /**
     * 检查网络状态
     *
     * @return
     */
    public static @NetType int checkNetworkState() {
        //得到网络连接信息
        ConnectivityManager connectManager = (ConnectivityManager) AppContext.sInstance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (connectManager == null){
                return NetType.NONE;
            }
            Network network = connectManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectManager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                return getNetStatus(networkCapabilities);
            }
            return NetType.NONE;
        } else {
            //去进行判断网络是否连接
            NetworkInfo info = connectManager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return NetType.NONE;
            }
            switch (info.getType()){
                case ConnectivityManager.TYPE_MOBILE:
                    return NetType.NET;
                case ConnectivityManager.TYPE_WIFI:
                    return NetType.WIFI;
                default:
                    return NetType.NET_UNKNOWN;
            }
        }
    }


}
