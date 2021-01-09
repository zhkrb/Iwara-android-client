package com.zhkrb.custom.refreshView;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/11/5 11:30
 */
public interface InterceptorDropListener {

    /**
     * 下拉刷新 true 拦截 false 不拦截
     * @return
     */
    boolean refresh();

    /**
     * 加载更多 true 拦截 false 不拦截
     * @return
     */
    boolean loadMore();

}
