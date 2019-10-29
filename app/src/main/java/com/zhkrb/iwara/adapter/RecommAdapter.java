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
 * Create by zhkrb on 2019/10/20 19:35
 */

package com.zhkrb.iwara.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.iwara.utils.ImgLoader;
import com.zhkrb.iwara.utils.WordUtil;

import java.util.List;

public class RecommAdapter extends RecyclerView.Adapter<RecommAdapter.holder> {

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<VideoListBean> mList;
    private onItemClickListener mItemClickListener;
    private View.OnClickListener mClickListener;
    private View.OnLongClickListener mLongClickListener;

    public RecommAdapter(Context context , @NonNull List<VideoListBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        setHasStableIds(true);
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
                if (mItemClickListener != null&&mItemClickListener instanceof VideoListAdapter.onItemLongClickListener){
                    ((VideoListAdapter.onItemLongClickListener) mItemClickListener).itemLongClick(bean);
                }
            }
            return true;
        };
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(mInflater.inflate(R.layout.view_video_index_nom,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.setData(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }
        return 0;
    }

    public void setClickListener(onItemClickListener clickListener) {
        mItemClickListener = clickListener;
    }

    class holder extends RecyclerView.ViewHolder {
        int pos;
        ImageView thumb;
        TextView title;
        TextView user;
        TextView views;
        TextView like;

        public holder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
            user = itemView.findViewById(R.id.user);
            views = itemView.findViewById(R.id.view);
            like = itemView.findViewById(R.id.like);
        }

        public void setData(VideoListBean videoListBean,int position){
            if (videoListBean==null){
                return;
            }
            itemView.setTag(position);
            pos = position;
            ImgLoader.display(videoListBean.getThumb(),thumb);
            title.setText(videoListBean.getTitle());
            views.setText(String.format("%s %s", videoListBean.getView(), WordUtil.getString(R.string.video_view)));
            like.setText(videoListBean.getLike());
            if (!itemView.hasOnClickListeners()){
                itemView.setOnClickListener(mClickListener);
                if (mItemClickListener instanceof VideoListAdapter.onItemLongClickListener){
                    itemView.setOnLongClickListener(mLongClickListener);
                }
            }
        }

    }

    public interface onItemClickListener{
        void itemClick(VideoListBean bean);
    }

    public interface onItemLongClickListener extends VideoListAdapter.onItemClickListener {
        void itemLongClick(VideoListBean bean);
    }

}
