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
import android.widget.FrameLayout;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhkrb.custom.refreshView.helper.BaseDataHelper;
import com.zhkrb.iwara.R;
import com.zhkrb.netowrk.BaseDataLoadCallback;
import com.zhkrb.netowrk.callback.BaseListCallback;
import com.zhkrb.utils.L;

import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @description：带上下拉的recyclerview封装
 * @author：zhkrb
 * @DATE： 2020/7/9 16:52
 */
public class RefreshExView extends FrameLayout {


    private static final String TAG = "refreshView";


    private boolean mAttachedToWindow;

    /**
     * 布局
     */
    private int mLayoutRes;
    /**
     * 允许下拉刷新
     */
    private boolean mEnableRefresh;
    /**
     * 允许上拉加载更多
     */
    private boolean mEnableLoadMore;

    private final RecyclerView mRecyclerView;
    private final SmartRefreshLayout mSmartRefreshLayout;
    private BaseDataHelper<?> mDataHelper;
    private Class<?> mType;

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

    public RefreshExView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshExView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshExView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 设置分割线
     *
     * @param itemDecoration
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

    public void refresh() {
        if (mDataHelper != null) {
            mPage = 0;
            mDataHelper.loadData(mPage, mRefreshCallback);
        }
    }


    public void loadMore() {
        if (mDataHelper != null) {
            mPage++;
            mDataHelper.loadData(mPage, getLoadCallback());
        }
    }

    /**
     * 保存view状态，smartRecyclerView 要单独保存滚动距离
     *
     * @return
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
     * @param state
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
     * @param dataHelper
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
    }

    public void setInterceptorDropListener(InterceptorDropListener interceptorDropListener) {
        mInterceptorDropListener = interceptorDropListener;
    }

    public void setReversalLoad(boolean reversalLoad) {
        this.reversalLoad = reversalLoad;
    }


    private final BaseListCallback<Object> mRefreshCallback = new BaseListCallback<Object>() {
        @Override
        public void onStart() {
            mSmartRefreshLayout.setEnableLoadMore(false);
        }

        @Override
        public void onSuccess(int code, String msg, List<Object> info) {
            if (mDataHelper == null) {
                throw new RuntimeException("didn't set helper");
            }

            if (code != 200) {
                showFailView(msg);
                return;
            }




        }

        @Override
        public void onFinish() {
            mSmartRefreshLayout.finishRefresh();
            mSmartRefreshLayout.setEnableLoadMore(mEnableLoadMore);
        }

        @Override
        public void onError(int code, String msg) {
            mDataHelper.onNoData(true);
        }
    };


    private final BaseDataLoadCallback<Object> mLoadMoreCallback = new BaseDataLoadCallback<Object>() {
        @Override
        public void onStart() {
            mSmartRefreshLayout.setEnableRefresh(false);
        }

        @Override
        public void onSuccess(int code, String msg, Object info) {

        }

        @Override
        public void onFinish() {
            mSmartRefreshLayout.setEnableRefresh(mEnableRefresh);

        }

        @Override
        public void onError(int code, String msg) {

        }
    };
}
