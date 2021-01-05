package com.zhkrb.custom.refreshView.helper;

import com.zhkrb.custom.refreshView.BaseRefreshAdapter;
import com.zhkrb.netowrk.BaseDataLoadCallback;
import com.zhkrb.netowrk.callback.BaseListCallback;

import java.util.List;

/**
 * @description：数据帮助类
 * @author：zhkrb
 * @DATE： 2020/11/24 9:49
 */
public interface BaseDataHelper <T> {

    /**
     * 返回创建的adapter
     * @return
     */
    BaseRefreshAdapter<T> getAdapter();

    /**
     * 数据请求，传入callback回调
     * @param p
     * @param callback
     */
    void loadData(int p, BaseListCallback<Object> callback);

    /**
     * 转换数据类
     * @param info
     * @return
     */
    List<T> processData(List<T> info);

    /**
     * 数据刷新回调
     * @param list
     */
    void onRefresh(List<T> list);

    /**
     * 空数据回调
     * @param noData
     */
    void onNoData(boolean noData);

    /**
     * 数据加载完成回调
     * @param dataCount
     */
    void onLoadDataCompleted(int dataCount);

    /**
     * 视图更新完成
     */
    void onViewLoadCompleted();

}