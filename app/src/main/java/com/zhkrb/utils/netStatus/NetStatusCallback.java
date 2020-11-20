package com.zhkrb.utils.netStatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.qiyou.id.AppContext;
import com.qiyou.id.utils.L;
import com.qiyou.id.utils.WordUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * 使用
 *
 * @description：网络状态监听
 * @author：zhkrb
 * @DATE： 2020/8/18 9:15
 */
public class NetStatusCallback extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetStatus";

    private HashMap<Object, Method> mCheckMap = new HashMap<>(4);

    private MutableLiveData<Integer> mTypeLiveData = new MutableLiveData<>();

    public NetStatusCallback() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        AppContext.sInstance.registerReceiver(new NetStatusReceiver(),filter);
        post(NetUtils.checkNetworkState());
    }

    /**
     * 返回一个type livedata
     * @return
     */
    public MutableLiveData<Integer> getNetTypeLiveData() {
        return mTypeLiveData;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        L.e(TAG, "net connect success 网络已连接");
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        L.e(TAG, "net disconnect 网络已断开连接");
        Integer type = NetUtils.checkNetworkState();
        if (type.equals(mTypeLiveData.getValue())){
            return;
        }
        L.e(TAG, "当前网络连接为 type: " + WordUtil.getNetworkType(type));
        post(type);
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        Integer type = NetUtils.checkNetworkState();
        if (type.equals(mTypeLiveData.getValue())){
            return;
        }
        L.e(TAG, "net status change 网络连接改变 type: " + WordUtil.getNetworkType(type));
        post(type);
    }

    /**
     * 分发事件
     * @param type
     */
    private void post(@NetType int type){
        mTypeLiveData.postValue(type);
        Set<Object> objects = mCheckMap.keySet();
        for (Object obj : objects){
            Method method = mCheckMap.get(obj);
            if (method == null){
                continue;
            }
            invoke(obj,method,type);
        }
    }

    /**
     * 反射执行方法
     * @param obj
     * @param method
     * @param type
     */
    private void invoke(Object obj, Method method, int type) {
        try {
            method.invoke(obj,type);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 注册
     * @param object
     */
    public void register(Object object){
        Class<?> clazz = object.getClass();
        if (!mCheckMap.containsKey(clazz)){
            Method method = findAnnotationMethod(clazz);
            if (method == null){
                return;
            }
            mCheckMap.put(object,method);
        }
    }

    /**
     * 取消注册
     * @param object
     */
    public void unRegister(Object object){
        mCheckMap.remove(object);
    }

    /**
     * 取消所有注册
     */
    public void unRegisterAll(){
        mCheckMap.clear();
    }

    /**
     * 判断是否注册
     * @param object
     * @return
     */
    public boolean isRegister(Object object) {
        return mCheckMap.containsKey(object);
    }


    /**
     * 查找注解方法
     * @param clazz
     * @return
     */
    public Method findAnnotationMethod(Class<?> clazz){
        Method[] methods = clazz.getMethods();
        for (Method method : methods){
            Annotation annotation = method.getAnnotation(NetStatus.class);
            if (annotation == null){
                continue;
            }
            String type = method.getGenericReturnType().toString();
            if (!"void".equals(type)){
                //使用注解的方法必须为void
                throw new RuntimeException("The return type of the method【 " + method.getName()+ " 】 must be void");
            }
            //检查参数，必须为1个int类型
            Type[] types = method.getGenericParameterTypes();
            if (types.length != 1 || !("class " + Integer.class.getName()).equals(types[0].toString())){
                throw new RuntimeException("The parameter types size of the method【 "
                        + method.getName()
                        + " 】must be one (type name must be java.lang.Integer)!");
            }
            return method;
        }
        return null;
    }


    private class NetStatusReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null){
                return;
            }

            Integer type = NetUtils.checkNetworkState();
            if (type.equals(mTypeLiveData.getValue())){
                return;
            }
            post(type);
        }
    }


}
