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

package com.zhkrb.custom.refreshView;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zhkrb.iwara.R;
import com.zhkrb.netowrk.jsoup.BaseJsoupCallback;
import com.zhkrb.netowrk.retrofit.BaseRetrofitCallback;
import com.zhkrb.utils.ToastUtil;
import com.zhkrb.utils.WordUtil;
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
    private  boolean isHeaderRefresh;
    private boolean isFooterRefresh;
    private boolean isShowError;
    private int mLayoutRes;
    private Context mContext;
    private ErrorView mErrorView;
    private SwipeRefreshLayout mRefreshLayout;
    private ScaleRecyclerView mRecyclerView;
    private DataHelper mDataHelper;
    private ScaleRecyclerView.onScaleListener mOnScaleListener;
    private String mErrHint;
    private int mErrDrawable;
    private int mPage;

    private int mCallbackType = 0; //回调类型 0 jsoup 1 retrofit
    public static final int TYPE_JSOUP = 0;
    public static final int TYPE_RETROFIT = 1;

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
        isHeaderRefresh = ta.getBoolean(R.styleable.RefreshView_showLoading, true);
        mEnableRefresh = ta.getBoolean(R.styleable.RefreshView_enableRefresh, true);
        mEnableLoadMore = ta.getBoolean(R.styleable.RefreshView_enableLoadMore, true);
        ta.recycle();
        createErrorView();
    }

    private void createErrorView() {
        mErrorView = new ErrorView(mContext);
        mErrorView.setId(R.id.error_view);
        mErrorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mErrorView.setOnClickListener(this);
        mErrorView.setVisibility(GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(mLayoutRes,this,true);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setItemViewCacheSize(10);
        mRefreshLayout.setOnRefreshListener(this::refresh);
        OnScrollEndlessListener onScrollEndlessListener = new OnScrollEndlessListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        };
        mRecyclerView.addOnScrollListener(onScrollEndlessListener);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRefreshLayout.setColorSchemeResources(R.color.ref_blue,
                R.color.ref_green,
                R.color.ref_pink,
                R.color.ref_pop,
                R.color.ref_red);
        if (isHeaderRefresh){
            mRefreshLayout.setRefreshing(true);
        }
        if (isShowError){
            showError();
        }
        mErrorView.setOnClickListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public <T> RefreshView setDataHelper(DataHelper<T> dataHelper) {
        mDataHelper = dataHelper;
        return this;
    }

    public void init(){
        if (mDataHelper == null) {
            throw new RuntimeException("must set Helper");
        }
        BaseRefreshAdapter adapter = mDataHelper.getAdapter();
        if (adapter == null){
            return;
        }
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void resetAllstate(){
        hideLoadmore(false);
        hideLoading();
//        if (!mEnableLoadMore)
    }

    public void showLoading(){
        isHeaderRefresh = true;
        mRefreshLayout.setRefreshing(true);
    }

    public void hideLoading(){
        isHeaderRefresh = false;
        mRefreshLayout.setRefreshing(false);
    }

    public void hideLoadmore(boolean needAnim){
        isFooterRefresh = false;
        if (mDataHelper != null && mDataHelper.getAdapter() != null){
            mDataHelper.getAdapter().hideFooterLoad(needAnim);
        }
    }

    public void hideLoadmore(){
        isFooterRefresh = false;
        if (mDataHelper != null && mDataHelper.getAdapter() != null){
            mDataHelper.getAdapter().hideFooterLoad();
        }
    }

    public void showError(){
        isShowError = true;
        mRecyclerView.setVisibility(INVISIBLE);
        if (getChildCount() < 2){
            addView(mErrorView);
        }
        mErrorView.setVisibility(VISIBLE);
        mErrorView.setError(mErrHint,mErrDrawable);
    }

    public void hideError(){
        isShowError = false;
        mRecyclerView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
    }

    private boolean isShowFullScreen(){
        if (mRecyclerView == null){
            return false;
        }
        int childCount = mRecyclerView.getChildCount();
        //获取最后一个childView
        View lastChildView = mRecyclerView.getChildAt(childCount - 1);
        //获取第一个childView
        View firstChildView = mRecyclerView.getChildAt(0);
        int top = firstChildView.getTop();
        int bottom = lastChildView.getBottom();
        //recycleView显示itemView的有效区域的bottom坐标Y
        int bottomEdge = mRecyclerView.getHeight() - mRecyclerView.getPaddingBottom();
        //recycleView显示itemView的有效区域的top坐标Y
        int topEdge = mRecyclerView.getPaddingTop();
        //第一个view的顶部小于top边界值,说明第一个view已经部分或者完全移出了界面
        //最后一个view的底部小于bottom边界值,说明最后一个view已经完全显示在界面
        //若不处理这种情况,可能会存在recycleView高度足够高时,itemView数量很少无法填充一屏,但是滑动到最后一项时依然会发生回调
        //此时其实并不需要任何刷新操作的
        if (bottom <= bottomEdge && top < topEdge) {
            return true;
        }
        return false;
    }

    private BaseJsoupCallback mJsoupRefreshCallback = new BaseJsoupCallback() {

        private int mDataCount;

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(int code, String msg, String info) {
            if (mDataHelper == null) {
                return;
            }
            hideError();

            if (code!=200){
                mErrHint = WordUtil.getString(R.string.no_more_there);
                showError();
                return;
            }
            if (!TextUtils.isEmpty(info)){
                List list = mDataHelper.processData(info);
                if (list == null){
                    list = new ArrayList(0);
                }
                mDataCount = list.size();
                if (list.size()>0){
                    hideError();
                    mDataHelper.getAdapter().refreshData(list);
                    mDataHelper.onRefresh(list);
                }else {
                    mDataHelper.getAdapter().clearData();
                    mErrHint = WordUtil.getString(R.string.no_more_there);
                    showError();
                    mDataHelper.onNoData(true);
                }
            }else {
                mDataHelper.getAdapter().clearData();
                mErrHint = WordUtil.getString(R.string.no_more_there);
                showError();
                mDataHelper.onNoData(true);
            }
        }

        @Override
        public void onFinish() {
            setLoadMoreEnable(true);
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                hideLoading();
            }
            if (mDataHelper != null) {
                mDataHelper.onLoadDataCompleted(mDataCount);
            }
        }

        @Override
        public void onError(int code,String msg) {
            mErrHint = WordUtil.getErrorMsg(code,msg);
            showError();
            BaseRefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter != null && adapter.getItemCount() > 0) {
                adapter.clearData();
            }
            mDataHelper.onNoData(true);
        }
    };

    private BaseJsoupCallback mJsoupLoadMoreCallback = new BaseJsoupCallback() {

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
            BaseRefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter == null){
                return;
            }
            if (code!=200){
                mPage--;
                ToastUtil.show(WordUtil.getErrorMsg(code,msg));
                hideLoadmore();
                return;
            }
            hideError();
            if (!TextUtils.isEmpty(info)){
                List list = mDataHelper.processData(info);
                if (list == null ||list.size() == 0){
                    hideLoadmore();
                    mPage--;
                    return;
                }
                mDataCount = list.size();
                adapter.insertList(list);
            }else {
                hideLoadmore();
                mPage--;
            }
        }

        @Override
        public void onFinish() {
            mEnableLoadMore = true;
            if (mDataHelper != null) {
                mDataHelper.onLoadDataCompleted(mDataCount);
            }
        }

        @Override
        public void onError(int code,String msg) {
            mPage--;
            BaseRefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter == null){
                return;
            }
            ToastUtil.show(WordUtil.getErrorMsg(code,msg));
            hideLoadmore();
        }
    };

    private BaseRetrofitCallback mRetrofitRefreshCallback = new BaseRetrofitCallback() {

        private int mDataCount;

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(int code, String msg, String info) {
            if (mDataHelper == null) {
                return;
            }
            hideError();

            if (code!=200){
                mRecyclerView.setVisibility(GONE);
                mErrHint = WordUtil.getString(R.string.no_more_there);
                showError();
                return;
            }
            if (!TextUtils.isEmpty(info)){
                List list = mDataHelper.processData(info);
                if (list == null){
                    list = new ArrayList(0);
                }
                mDataCount = list.size();
                if (list.size()>0){
                    hideError();
                    mRecyclerView.setVisibility(VISIBLE);
                    mDataHelper.onNoData(false);
                    mDataHelper.getAdapter().refreshData(list);
                    mDataHelper.onRefresh(list);
                }else {
                    mDataHelper.getAdapter().clearData();
                    mErrHint = WordUtil.getString(R.string.no_more_there);
                    showError();
                    mDataHelper.onNoData(true);
                }
            }else {
                mDataHelper.getAdapter().clearData();
                mErrHint = WordUtil.getString(R.string.no_more_there);
                showError();
                mDataHelper.onNoData(true);
            }
        }

        @Override
        public void onFinish() {
            setLoadMoreEnable(true);
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                hideLoading();
            }
            if (mDataHelper != null) {
                mDataHelper.onLoadDataCompleted(mDataCount);
            }
        }

        @Override
        public void onError(int code,String msg) {
            mErrHint = WordUtil.getErrorMsg(code,msg);
            showError();
            BaseRefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter != null && adapter.getItemCount() > 0) {
                adapter.clearData();
            }
            mDataHelper.onNoData(true);
        }
    };

    private BaseRetrofitCallback mRetrofitLoadMoreCallback = new BaseRetrofitCallback() {

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
            BaseRefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter == null){
                return;
            }
            if (code!=200){
                mPage--;
                ToastUtil.show(msg);
                hideLoadmore();
                return;
            }
            hideError();
            if (!TextUtils.isEmpty(info)){
                List list = mDataHelper.processData(info);
                if (list == null ||list.size() == 0){
                    hideLoadmore();
                    mPage--;
                    return;
                }
                mDataCount = list.size();
                adapter.insertList(list);
            }else {
                hideLoadmore();
                mPage--;
            }
        }

        @Override
        public void onFinish() {
            mEnableLoadMore = true;
            if (mDataHelper != null) {
                mDataHelper.onLoadDataCompleted(mDataCount);
            }
        }

        @Override
        public void onError(int code,String msg) {
            mPage--;
            BaseRefreshAdapter adapter = mDataHelper.getAdapter();
            if (adapter == null){
                return;
            }
            ToastUtil.show(WordUtil.getErrorMsg(code,msg));
            hideLoadmore();
        }
    };

    public void initData() {
        refresh();
    }

    private void refresh() {
        if (mDataHelper != null) {
            mPage = 1;
            mDataHelper.loadData(mPage, getRefreshCallback());
            setLoadMoreEnable(false);
        }
        showLoading();
    }

    public void loadMore() {
        isFooterRefresh = true;
        if (mDataHelper != null&&mEnableLoadMore) {
            mPage++;
            mDataHelper.loadData(mPage, getLoadMoreCallback());
            setLoadMoreEnable(false);
        }
    }

    private NetworkCallback getRefreshCallback(){
        switch (mCallbackType){
            case TYPE_JSOUP:
                return mJsoupRefreshCallback;
            case TYPE_RETROFIT:
                return mRetrofitRefreshCallback;
            default:
                throw new RuntimeException("callback type error");
        }
    }

    private NetworkCallback getLoadMoreCallback(){
        switch (mCallbackType){
            case TYPE_JSOUP:
                return mJsoupLoadMoreCallback;
            case TYPE_RETROFIT:
                return mRetrofitLoadMoreCallback;
            default:
                throw new RuntimeException("callback type error");
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
            case R.id.error_view:
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

    public void restoreData(List list) {
        hideError();
        mDataHelper.onNoData(false);
        mDataHelper.getAdapter().setList(list);
        hideLoading();
        hideLoadmore();
    }

    public void setCallbackType(int callbackType) {
        mCallbackType = callbackType;
    }


    public interface DataHelper<T> {
        BaseRefreshAdapter<T> getAdapter();

        void loadData(int p, NetworkCallback callback);

        List<T> processData(String info);

        void onRefresh(List<T> list);

        void onNoData(boolean noData);

        void onLoadDataCompleted(int dataCount);

        int maxPageItemCount();
    }
}
