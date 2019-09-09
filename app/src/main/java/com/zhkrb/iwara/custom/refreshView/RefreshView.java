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

package com.zhkrb.iwara.custom.refreshView;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.utils.ImgLoader;
import com.zhkrb.iwara.utils.L;
import com.zhkrb.iwara.utils.ToastUtil;
import com.zhkrb.iwara.utils.WordUtil;
import com.zhkrb.netowrk.NetworkCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RefreshView extends FrameLayout implements View.OnClickListener {

    private  boolean mEnableLoadMore;
    private  boolean mEnableRefresh;
    private  boolean mShowLoading;
    private boolean mShowNoData;
    private int mLayoutRes;
    private Context mContext;
    private ViewGroup mNo_data;
    private TextView mHintText;
    private ImageView mNo_data_img;
    private SwipeRefreshLayout mRefreshLayout;
    private ScaleRecyclerView mRecyclerView;
    private DataHelper mDataHelper;
    private ScaleRecyclerView.onScaleListener mOnScaleListener;
    private int mPage;

    public RefreshView(@NonNull Context context) {
        this(context,null);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshView);
        mLayoutRes = ta.getResourceId(R.styleable.RefreshView_layout, R.layout.view_refresh_group);
        mShowNoData = ta.getBoolean(R.styleable.RefreshView_showNoData, true);
        mShowLoading = ta.getBoolean(R.styleable.RefreshView_showLoading, true);
        mEnableRefresh = ta.getBoolean(R.styleable.RefreshView_enableRefresh, true);
        mEnableLoadMore = ta.getBoolean(R.styleable.RefreshView_enableLoadMore, true);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(mLayoutRes,this,false);
        addView(view);
        mNo_data = view.findViewById(R.id.no_data);
        mNo_data_img = view.findViewById(R.id.no_data_img);
        mHintText = view.findViewById(R.id.hint);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(4);
        mRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                L.e("Fling", String.valueOf(Math.abs(velocityY)));
                if (Math.abs(velocityY)>4000){
                    ImgLoader.pause();
                }
                return false;
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        OnScrollEndlessListener onScrollEndlessListener = new OnScrollEndlessListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        };
        mRecyclerView.addOnScrollListener(onScrollEndlessListener);
        mRefreshLayout.setColorSchemeResources(R.color.ref_blue,
                R.color.ref_green,
                R.color.ref_pink,
                R.color.ref_pop,
                R.color.ref_red);
        if (mShowLoading){
            mRefreshLayout.setRefreshing(true);
        }
        mNo_data.setOnClickListener(this);

    }

    public <T> void setDataHelper(DataHelper<T> dataHelper) {
        mDataHelper = dataHelper;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void showLoading(){
        mRefreshLayout.setRefreshing(true);
    }

    public void hideLoading(){
        mRefreshLayout.setRefreshing(false);
    }

    private NetworkCallback mRefreshCallback = new NetworkCallback() {

        private int mDataCount;

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(int code, String msg, String info) {
            if (mDataHelper == null) {
                return;
            }
            if (mNo_data != null && mNo_data.getVisibility() == View.VISIBLE) {
                mNo_data.setVisibility(View.GONE);
            }
            RefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter == null){
                return;
            }
            adapter.setLoadMoreCallback(new RefreshAdapter.LoadMoreCallback() {
                @Override
                public void loadMore() {
                    adapter.setLoadState(RefreshAdapter.LOAD_TYPE_NOM,"");
                    RefreshView.this.loadMore();
                }
            });
            if (adapter.getRecyclerView() == null){
                mRecyclerView.setAdapter(adapter);
            }
            if (code!=200){
                mRecyclerView.setVisibility(GONE);
                mNo_data.setVisibility(View.VISIBLE);
                mHintText.setText(WordUtil.getString(R.string.no_more_there));
                return;
            }
            if (!TextUtils.isEmpty(info)){
                List list = mDataHelper.processData(info);
                if (list == null){
                    list = new ArrayList(0);
                }
                mDataCount = list.size();
                if (list.size()>0){
                    if (mShowNoData && mNo_data != null && mNo_data.getVisibility() == View.VISIBLE) {
                        mNo_data.setVisibility(View.GONE);
                    }
                    mRecyclerView.setVisibility(VISIBLE);
                    mDataHelper.onNoData(false);
                    adapter.refreshData(list);
                    mDataHelper.onRefresh(list);
                    if (list.size()<mDataHelper.maxPageItemCount()){
                        adapter.setLoadState(RefreshAdapter.LOAD_TYPE_EMPTY,"");
                    }else {
                        adapter.setLoadState(RefreshAdapter.LOAD_TYPE_NOM,"");
                    }
                }else {
                    adapter.clearData();
                    if (mShowNoData && mNo_data != null && mNo_data.getVisibility() != View.VISIBLE) {
                        mRecyclerView.setVisibility(GONE);
                        mNo_data.setVisibility(View.VISIBLE);
                        mHintText.setText(WordUtil.getString(R.string.no_more_there));
                    }
                    mDataHelper.onNoData(true);
                }
            }else {
                adapter.clearData();
                if (mShowNoData && mNo_data != null && mNo_data.getVisibility() != View.VISIBLE) {
                    mRecyclerView.setVisibility(GONE);
                    mNo_data.setVisibility(View.VISIBLE);
                    mHintText.setText(WordUtil.getString(R.string.no_more_there));
                }
                mDataHelper.onNoData(true);
            }
        }

        @Override
        public void onFinish() {
            setLoadMoreEnable(true);
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            if (mDataHelper != null) {
                mDataHelper.onLoadDataCompleted(mDataCount);
            }
        }

        @Override
        public void onError(int code,String msg) {
            if (mShowNoData && mNo_data != null && mNo_data.getVisibility() == View.VISIBLE) {
                mNo_data.setVisibility(View.GONE);
            }
            if (mNo_data != null) {
                if (mNo_data.getVisibility() != View.VISIBLE) {
                    RefreshAdapter adapter = mDataHelper.getAdapter();
                    if (adapter != null && adapter.getItemCount() > 0) {
                        adapter.clearData();
                    }
                    mRecyclerView.setVisibility(GONE);
                    mNo_data.setVisibility(View.VISIBLE);
                }
                mHintText.setText(WordUtil.getErrorMsg(code,msg));
            }
            mDataHelper.onNoData(true);
        }
    };

    private NetworkCallback mLoadMoreCallback = new NetworkCallback() {

        private int mDataCount;

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(int code, String msg, String info) {
            if (mDataHelper == null) {
                mPage--;
                return;
            }
            RefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter == null){
                return;
            }
            if (code!=200){
                mPage--;
                ToastUtil.show(msg);
                adapter.setLoadState(RefreshAdapter.LOAD_TYPE_ERROR,WordUtil.getErrorMsg(code,msg));
                return;
            }
            if (mShowNoData && mNo_data.getVisibility() == VISIBLE){
                mNo_data.setVisibility(GONE);
                mRecyclerView.setVisibility(VISIBLE);
            }
            if (!TextUtils.isEmpty(info)){
                List list = mDataHelper.processData(info);
                if (list == null){
                    adapter.setLoadState(RefreshAdapter.LOAD_TYPE_EMPTY,"");
                    mPage--;
                    return;
                }
                mDataCount = list.size();
                if (mDataCount > 0){
                    adapter.insertList(list);
                    if (mDataCount < mDataHelper.maxPageItemCount()){
                        adapter.setLoadState(RefreshAdapter.LOAD_TYPE_EMPTY,"");
                    }else {
                        adapter.setLoadState(RefreshAdapter.LOAD_TYPE_NOM,"");
                    }
                }else {
                    adapter.setLoadState(RefreshAdapter.LOAD_TYPE_EMPTY,"");
                    mPage--;
                }
            }else {
                adapter.setLoadState(RefreshAdapter.LOAD_TYPE_EMPTY,"");
                mPage--;
            }
        }

        @Override
        public void onFinish() {
            mEnableLoadMore = true;
            if (mShowLoading && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            if (mDataHelper != null) {
                mDataHelper.onLoadDataCompleted(mDataCount);
            }
        }

        @Override
        public void onError(int code,String msg) {
            RefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter == null){
                return;
            }
            adapter.setLoadState(RefreshAdapter.LOAD_TYPE_ERROR,WordUtil.getErrorMsg(code,msg));
        }
    };

    public void initData() {
        refresh();
    }

    private void refresh() {
        if (mDataHelper != null) {
            mPage = 1;
            mDataHelper.loadData(mPage, mRefreshCallback);
            setLoadMoreEnable(false);
        }
        showLoading();
    }

    public void loadMore() {
        if (mDataHelper != null&&mEnableLoadMore) {
            mPage++;
            mDataHelper.loadData(mPage, mLoadMoreCallback);
            setLoadMoreEnable(false);
        }
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public void setRefreshEnable(boolean enable) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnabled(enable);
        }
    }

    public void setLoadMoreEnable(boolean enable) {
        mEnableLoadMore = enable;
    }


    public DataHelper getDataHelper() {
        return mDataHelper;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.no_data:
                if (mRefreshLayout!=null&&mRefreshLayout.isRefreshing()){
                    return;
                }
                refresh();
                break;
        }
    }

    public void setOnScaleListener(ScaleRecyclerView.onScaleListener onScaleListener) {
        mOnScaleListener = onScaleListener;
        if (mRecyclerView!=null){
            mRecyclerView.setScaleListener(mOnScaleListener);
        }
    }


    public interface DataHelper<T> {
        RefreshAdapter<T> getAdapter();

        void loadData(int p, NetworkCallback callback);

        List<T> processData(String info);

        void onRefresh(List<T> list);

        void onNoData(boolean noData);

        void onLoadDataCompleted(int dataCount);

        int maxPageItemCount();
    }
}
