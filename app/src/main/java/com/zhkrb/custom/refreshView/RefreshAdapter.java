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
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhkrb.custom.refreshView.loading.LoadingView;
import com.zhkrb.iwara.R;

public abstract class RefreshAdapter<T> extends RecyclerView.Adapter {

    protected List<T> mList;
    private Context mContext;
    protected LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private boolean isFooterEnable = false;
    private Vh_more mView_loadmore;

    protected static final int TYPE_MORE = -1;      //加载更多Vh type


    public RefreshAdapter(Context context) {
        this(context,new ArrayList<T>());
    }

    public RefreshAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        setHasStableIds(true);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public int getItemCount() {
        if (mList !=null){
            return mList.size()+1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MORE){
            if (mView_loadmore ==null){
                mView_loadmore = new Vh_more(mInflater.inflate(R.layout.view_loadmore,parent,false));
            }
            return mView_loadmore;
        }else {
            return onViewHolderCreate(parent,viewType);
        }
    }

    protected abstract RecyclerView.ViewHolder onViewHolderCreate(@NonNull ViewGroup parent,int viewType);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != mList.size()){
            onViewHolderBind(holder,position);
        }
    }

    protected abstract void onViewHolderBind(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        if (mList != null && position==mList.size()){
            return TYPE_MORE;
        }
        return getViewType(position);
    }

    protected abstract int getViewType(int pos);

    public void enableFooter(boolean enable){
        isFooterEnable = enable;
        if (mList != null && mList.size() > 0){
            notifyItemChanged(mList.size() + 1);
        }
    }

    public void hideFooterLoad(){
        hideFooterLoad(true);
    }

    public void hideFooterLoad(boolean needAnim){
        if (mList == null ||mList.size() == 0 || mRecyclerView.getLayoutManager() == null){
            return;
        }
        View view = mRecyclerView.getLayoutManager().findViewByPosition(mList.size());
        if (view == null){
            return;
        }
        Rect visibleRect = new Rect();
        if (isViewVisibleRect(view,visibleRect)){
            LoadingView loadingView = view.findViewById(R.id.loading_view);
            loadingView.endAnimation(() -> {
                if (needAnim){
                    mRecyclerView.smoothScrollBy(0, -visibleRect.bottom,new AccelerateDecelerateInterpolator());
                }else {
                    mRecyclerView.scrollBy(0, -visibleRect.bottom);
                }
            });
        }
    }

    private boolean isViewVisibleRect(View v,Rect rect){
        return v.getLocalVisibleRect(rect);
    }

    public void setList(List<T> list){
        if (mList==null){
            mList = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void refreshData(List<T> list){
        if (mRecyclerView != null) {
            if (mList==null){
                mList = new ArrayList<>();
            }
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void insertList(List<T> list) {
        if (mRecyclerView != null && mList != null && list != null && list.size() > 0) {
            int p = mList.size();
            mList.addAll(list);
            notifyItemRangeInserted(p, list.size());
        }
    }

    public void clearData() {
        if (mRecyclerView != null && mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getList() {
        return mList;
    }


    class Vh_more extends RecyclerView.ViewHolder {

        Vh_more(@NonNull View itemView) {
            super(itemView);
            LoadingView lv = itemView.findViewById(R.id.loading_view);
            lv.setColorSchemeResources(R.color.ref_blue,
                    R.color.ref_green,
                    R.color.ref_pink,
                    R.color.ref_pop,
                    R.color.ref_red);
        }
    }

}
