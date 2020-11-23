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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.zhkrb.iwara.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @description：带上下拉的recyclerview封装
 * @author：zhkrb
 * @DATE： 2020/7/9 16:52
 */
public class RefreshExView extends FrameLayout {

    private boolean mAttachedToWindow;

    /**
     * 布局
     */
    private int mLayoutRes;

    private boolean mEnableRefresh;
    private boolean mEnableLoadMore;

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;

    public RefreshExView(@NonNull Context context) {
        this(context,null);
    }

    public RefreshExView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshExView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshView);
        mLayoutRes = ta.getResourceId(R.styleable.RefreshView_layout, R.layout.view_refresh_group);
        mEnableRefresh = ta.getBoolean(R.styleable.RefreshView_enableRefresh, true);
        mEnableLoadMore = ta.getBoolean(R.styleable.RefreshView_enableLoadMore, true);
        ta.recycle();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(mLayoutRes,this,true);

        mRecyclerView = view.findViewById(R.id.view_recycler);
        mSmartRefreshLayout = view.findViewById(R.id.view_smart_refresh);

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
     * @return
     */
    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    /**
     * 设置分割线
     * @param itemDecoration
     */
    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        if (mRecyclerView != null){
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager){
        if (mRecyclerView != null){
            mRecyclerView.setLayoutManager(manager);
        }
    }

    /**
     * 初始化
     */
    public void initData(){

    }



}
