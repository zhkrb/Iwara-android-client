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

package com.zhkrb.iwara.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhkrb.iwara.AppConfig;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.iwara.custom.refreshView.RefreshAdapter;
import com.zhkrb.iwara.utils.DpUtil;
import com.zhkrb.iwara.utils.ImgLoader;
import com.zhkrb.iwara.utils.SortUtil;
import com.zhkrb.iwara.utils.WordUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends RefreshAdapter<VideoListBean> {

    private static final int TYPE_NOM = 0;
    private static final int TYPE_BIG = 1;
    private static final int TYPE_SM = 2;
    private boolean isLastItemSingle = false;
    private boolean isListSort = false; //是否重新排序以适应 mode2
    private static int mListMode = 0;
    private int mLastBgSize = 0;

    private onItemClickListener mItemClickListener;
    private View.OnClickListener mClickListener;
    private View.OnLongClickListener mLongClickListener;

    private static final int mScreenWidth = DpUtil.getScreenWidth();

    public VideoListAdapter(Context context) {
        this(context,null);
    }

    public VideoListAdapter(Context context, List<VideoListBean> list) {
        super(context, list);
        mClickListener = v -> {
            Object tag = v.getTag();
            if (tag != null) {
                int position = (int) tag;
                VideoListBean bean = mList.get(position);
                if (mItemClickListener != null) {
                    mItemClickListener.itemClick(bean);
                }
            }
        };
        mLongClickListener = v -> {
            Object tag = v.getTag();
            if (tag != null) {
                int position = (int) tag;
                VideoListBean bean = mList.get(position);
                if (mItemClickListener != null&&mItemClickListener instanceof onItemLongClickListener){
                    ((onItemLongClickListener) mItemClickListener).itemLongClick(bean);
                }
            }
            return true;
        };
    }

    public void setListMode(int mListMode) {
        VideoListAdapter.mListMode = mListMode;
        if (mList!=null && mList.size()>0){
            notifyDataSetChanged();
        }
    }

    @Override
    protected int getViewType(int pos) {
        switch (mListMode){
            case 1:
                return TYPE_NOM;//单排
            case 2:
                if (Integer.parseInt(mList.get(pos).getLike())> AppConfig.getInstance().getShowLikeBg()){
                    return TYPE_BIG;
                }else {
                    return TYPE_SM;
                }
            case 0:
            default:
                return TYPE_SM;//双排
        }
    }

    @Override
    public void setList(List<VideoListBean> list) {
        if (isListSort){
            SortUtil.result result1 = SortUtil.SortVideoList(list,false);
            isLastItemSingle = result1.isLastSignle();
            mLastBgSize = result1.getLastBgSize();
            super.setList(result1.getList());
        }else {
            super.setList(list);
        }
    }

    @Override
    public void refreshData(List<VideoListBean> list) {
        if (isListSort){
            SortUtil.result result1 = SortUtil.SortVideoList(list,false);
            isLastItemSingle = result1.isLastSignle();
            mLastBgSize = result1.getLastBgSize();
            super.refreshData(result1.getList());
        }else {
            super.refreshData(list);
        }
    }

    @Override
    public void insertList(List<VideoListBean> list) {
        if (mList!=null&&mList.size()>0 && isListSort){
            if (mLastBgSize>0){
                List<VideoListBean> list1 = mList.subList(mList.size()-mLastBgSize,mList.size());
                list.addAll(0,list1);
                mList.removeAll(list1);
            }
            SortUtil.result result1 = SortUtil.SortVideoList(list,isLastItemSingle);
            isLastItemSingle = result1.isLastSignle();
            if (mLastBgSize>0){
                notifyItemRangeRemoved(mList.size()-mLastBgSize,mLastBgSize);
            }
            mLastBgSize = result1.getLastBgSize();
            super.insertList(result1.getList());
        }else {
            super.insertList(list);
        }

    }

    @Override
    public void clearData() {
        isLastItemSingle = false;
        super.clearData();
    }

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        Vh vh;
        if (viewType==TYPE_BIG){
            vh = new Vh(mInflater.inflate(R.layout.item_video_index_bg,parent,false));
        }else if (viewType == TYPE_SM){
            vh = new Vh(mInflater.inflate(R.layout.item_video_index_sm,parent,false));
        }else {
            vh = new Vh(mInflater.inflate(R.layout.item_video_index_nom,parent,false));
        }
        return vh;
    }

    @Override
    protected void onViewHolderBind(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Vh)holder).setData(mList.get(position),position);
    }

    public void setSortMode(boolean b) {
        isListSort = b;
    }

    public void setClickListener(onItemClickListener clickListener) {
        mItemClickListener = clickListener;
    }

    class Vh extends RecyclerView.ViewHolder {
        int pos;
        ImageView thumb;
        TextView title;
        TextView user;
        TextView views;
        TextView like;

        public Vh(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
            user = itemView.findViewById(R.id.user);
            views = itemView.findViewById(R.id.view);
            like = itemView.findViewById(R.id.like);
        }


        public void setData(VideoListBean videoListBean, int position) {
            if (videoListBean==null){
                return;
            }
            itemView.setTag(position);
            pos = position;
            if (getViewType(pos) == TYPE_BIG){
                thumb.setMaxWidth(mScreenWidth);
                ImgLoader.displayTryThumbnail(videoListBean.getThumb(),thumb);
            }else {
                ImgLoader.display(videoListBean.getThumb(),thumb);
            }
            title.setText(videoListBean.getTitle());
            user.setText(videoListBean.getUser_name());
            views.setText(String.format("%s %s", videoListBean.getView(), WordUtil.getString(R.string.video_view)));
            like.setText(videoListBean.getLike());
            if (!itemView.hasOnClickListeners()){
                itemView.setOnClickListener(mClickListener);
                if (mItemClickListener instanceof onItemLongClickListener){
                    itemView.setOnLongClickListener(mLongClickListener);
                }
            }
        }
    }

    public interface onItemClickListener{
        void itemClick(VideoListBean bean);
    }

    public interface onItemLongClickListener extends onItemClickListener{
        void itemLongClick(VideoListBean bean);
    }

}
