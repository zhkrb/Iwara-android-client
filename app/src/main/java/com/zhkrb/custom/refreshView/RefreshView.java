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
 * Create by zhkrb on 2020/11/22 21:35
 */

package com.zhkrb.custom.refreshView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhkrb.custom.refreshView.helper.BaseDataHelper;
import com.zhkrb.glide.ImgLoader;
import com.zhkrb.iwara.R;
import com.zhkrb.netowrk.callback.BaseListCallback;
import com.zhkrb.utils.L;
import com.zhkrb.utils.SystemUtil;
import com.zhkrb.utils.ToastUtil;
import com.zhkrb.utils.WordUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @description：带上下拉的recyclerview封装
 * @author：zhkrb
 * @DATE： 2020/7/9 16:52
 */
public class RefreshView extends FrameLayout {


    private static final String TAG = "refreshView";


    private boolean mAttachedToWindow;

    /**
     * 布局
     */
    private final int mLayoutRes;
    /**
     * 允许下拉刷新
     */
    private final boolean mEnableRefresh;
    /**
     * 允许上拉加载更多
     */
    private final boolean mEnableLoadMore;

    private final RecyclerView mRecyclerView;
    private final SmartRefreshLayout mSmartRefreshLayout;
    private BaseDataHelper<?> mDataHelper;
    private BaseListCallback<?> mRefreshCallback;
    private BaseListCallback<?> mLoadMoreCallback;

    private View mFailedView;

    /**
     * 上下拉响应拦截
     */
    private InterceptorDropListener mInterceptorDropListener;

    /**
     * 颠倒上下拉功能
     */
    private boolean reversalLoad;

    /**
     * 是否是第一次初始化
     */
    private boolean firstLoad;

    /**
     * 页数
     */
    private int mPage = 0;

    /**
     * 加载的条数
     */
    private int mDataCount;

    /**
     * 是否在加载
     */
    private volatile boolean mLoading = false;

    public RefreshView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshView);
        mLayoutRes = ta.getResourceId(R.styleable.RefreshView_layout, R.layout.view_refresh_group);
        mEnableRefresh = ta.getBoolean(R.styleable.RefreshView_enableRefresh, true);
        mEnableLoadMore = ta.getBoolean(R.styleable.RefreshView_enableLoadMore, true);
        ta.recycle();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(mLayoutRes, this, true);

        mRecyclerView = view.findViewById(R.id.view_recycler);
        mSmartRefreshLayout = view.findViewById(R.id.view_smart_refresh);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mInterceptorDropListener != null &&
                        mInterceptorDropListener.loadMore()) {
                    mSmartRefreshLayout.finishLoadMore();
                    return;
                }
                if (reversalLoad) {
                    refresh();
                } else {
                    loadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mInterceptorDropListener != null &&
                        mInterceptorDropListener.refresh()) {
                    mSmartRefreshLayout.finishRefresh();
                    return;
                }

                if (reversalLoad) {
                    loadMore();
                } else {
                    refresh();
                }
            }
        });
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    /**
     * 获取recyclerview
     *
     * @return recyclerView
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 设置分割线
     *
     * @param itemDecoration 分割线
     */
    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(manager);
        }
    }

    /**
     * 初始化
     */
    public void initData() {
        if (firstLoad) {
            refresh();
        } else {
            mDataHelper.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        if (mLoading){
            return;
        }
        if (mDataHelper != null) {
            mLoading = true;
            mPage = 0;
            mDataHelper.loadData(mPage, mRefreshCallback);
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        if (mLoading){
            return;
        }
        if (mDataHelper != null) {
            mLoading = true;
            mPage++;
            mDataHelper.loadData(mPage, mLoadMoreCallback);
        }
    }

    /**
     * 保存view状态，smartRecyclerView 要单独保存滚动距离
     *
     * @return bundle
     */
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable parcelable = super.onSaveInstanceState();
        bundle.putParcelable("super", parcelable);
        bundle.putBoolean("firstLoad", false);
        bundle.putInt("page", mPage);
        bundle.putBoolean("reversalLoad", reversalLoad);
        return bundle;
    }

    /**
     * 恢复view状态
     *
     * @param state 默认
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable parcelable = bundle.getParcelable("super");
        firstLoad = bundle.getBoolean("firstLoad", true);
        if (!firstLoad) {
            mPage = bundle.getInt("page");
            reversalLoad = bundle.getBoolean("reversalLoad");

            initData();
        }
        super.onRestoreInstanceState(parcelable);
    }


    /**
     * 设置数据类
     *
     * @param dataHelper 数据帮助类
     */
    public <T> void setDataHelper(BaseDataHelper<T> dataHelper, Class<T> clazz) {
        mDataHelper = dataHelper;
        if (mDataHelper != null) {
            BaseRefreshAdapter<?> adapter = mDataHelper.getAdapter();
            if (adapter == null) {
                L.e("adapter is null, didn't set");
                return;
            }
            if (adapter.getRecyclerView() == null || !adapter.getRecyclerView().equals(mRecyclerView)) {
                mRecyclerView.setAdapter(adapter);
            }
        }

        initDataCallback(clazz);
    }

    private <T> void initDataCallback(Class<T> clazz) {
        if (mRefreshCallback == null) {
            mRefreshCallback = new BaseListCallback<T>() {
                @Override
                public void onStart() {
                    mSmartRefreshLayout.setEnableLoadMore(false);
                }

                @Override
                public void onSuccess(int code, String msg, List<T> info) {
                    if (mDataHelper == null) {
                        throw new RuntimeException("didn't set helper");
                    }

                    if (code != 200) {
                        showFail(msg);
                        return;
                    }

                    post(() ->{
                        if (info == null || info.size() == 0) {
                            showEmpty();
                            return;
                        } else {
                            mDataCount = info.size();
                            showRefreshData(info, mDataCount);
                        }
                        mDataHelper.onViewLoadCompleted();
                    });
                }

                @Override
                public void onFinish() {
                    mLoading = false;
                    post(() -> {
                        mSmartRefreshLayout.setEnableLoadMore(mEnableLoadMore);
                        if (reversalLoad) {
                            mSmartRefreshLayout.finishLoadMore();
                        } else {
                            mSmartRefreshLayout.finishRefresh();
                        }
                        if (mDataHelper != null) {
                            mDataHelper.onLoadDataCompleted(mDataCount);
                        }
                    });
                }

                @Override
                public void onError(int code, String msg) {
                    post(() -> {
                        mDataHelper.onNoData(true);
                    });

                }
            };
        }

        if (mLoadMoreCallback == null) {
            mLoadMoreCallback = new BaseListCallback<T>() {
                @Override
                public void onStart() {
                    mSmartRefreshLayout.setEnableLoadMore(false);
                }

                @Override
                public void onSuccess(int code, String msg, List<T> info) {
                    if (mDataHelper == null) {
                        throw new RuntimeException("didn't set helper");
                    }

                    if (code != 200) {
                        ToastUtil.show(msg);
                        mPage--;
                        return;
                    }

                    post(() -> {
                        if (info == null || info.size() == 0) {
                            L.e("no more data");
                            mDataCount = 0;
                            mPage--;
                        } else {
                            mDataCount = info.size();
                            showLoadNewData(info, mDataCount);
                        }
                        mDataHelper.onViewLoadCompleted();
                        mDataHelper.onLoadDataCompleted(mDataCount);
                    });
                }

                @Override
                public void onFinish() {
                    mLoading = false;
                    post(() -> {
                        mSmartRefreshLayout.setEnableRefresh(mEnableRefresh);
                        if (reversalLoad) {
                            mSmartRefreshLayout.finishRefresh();
                        } else {
                            mSmartRefreshLayout.finishLoadMore();
                        }
                        if (mDataHelper != null) {
                            mDataHelper.onLoadDataCompleted(mDataCount);
                        }
                    });
                }

                @Override
                public void onError(int code, String msg) {
                    L.e(msg);
                    mPage--;
                }
            };
        }
    }

    /**
     * 加载更多数据
     * @param info
     * @param dataCount
     * @param <T>
     */
    private <T> void showLoadNewData(List<T> info, int dataCount) {

    }

    /**
     * 刷新数据
     * @param info
     * @param dataCount
     * @param <T>
     */
    private <T> void showRefreshData(List<T> info, int dataCount) {

    }

    /**
     * 展示空数据页面
     */
    private void showEmpty() {
        showFailView(WordUtil.getString(R.string.no_more_there),R.drawable.empty_drawable);
    }



    /**
     * 展示加载失败页面
     * @param msg
     */
    private void showFail(String msg) {

    }

    /**
     * 提示页面
     * @param string
     * @param drawable
     */
    private void showFailView(String string, int drawable) {
        if (mFailedView == null){
            mFailedView = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_error,this,false);
            mFailedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFailedView.setOnClickListener(v -> {
                if (SystemUtil.canClick()){
                    refresh();
                }
            });
            addView(mFailedView);
        }

        mFailedView.setVisibility(VISIBLE);
        mRecyclerView.setVisibility(GONE);

        ((TextView)mFailedView.findViewById(R.id.hint)).setText(string);
        ImgLoader.display(drawable,mFailedView.findViewById(R.id.no_data_img));
    }

    /**
     * 隐藏提示页面
     */
    private void hideFail(){
        if (mFailedView == null || mFailedView.getVisibility() == GONE){
            return;
        }

        mFailedView.setVisibility(GONE);
        mRecyclerView.setVisibility(VISIBLE);
    }

    /**
     * 设置上下拉拦截
     *
     * @param interceptorDropListener 拦截监听
     */
    public void setInterceptorDropListener(InterceptorDropListener interceptorDropListener) {
        mInterceptorDropListener = interceptorDropListener;
    }

    /**
     * 设置是否颠倒上下拉逻辑
     *
     * @param reversalLoad true 是 false 否
     */
    public void setReversalLoad(boolean reversalLoad) {
        this.reversalLoad = reversalLoad;
    }


}
