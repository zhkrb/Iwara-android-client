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
 * Create by zhkrb on 2019/10/20 19:36
 */

package com.zhkrb.iwara.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.adapter.inter.AdapterClickInterface;
import com.zhkrb.iwara.bean.CommentBean;
import com.zhkrb.iwara.bean.UpdateInfoBean;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.iwara.custom.refreshView.RefreshAdapter;
import com.zhkrb.iwara.utils.ImgLoader;
import com.zhkrb.iwara.utils.ToastUtil;
import com.zhkrb.iwara.utils.WordUtil;

import java.util.List;

public class CommentAdapter extends RefreshAdapter<CommentBean> {

    private View.OnClickListener mClickListener;
    private AdapterClickInterface<CommentBean> mItemClickListener;

    public CommentAdapter(Context context) {
        super(context);
        mClickListener = v -> {
            Object tag = v.getTag();
            if (tag != null) {
                int position = (int) tag;
                CommentBean bean = mList.get(position);
                if (mItemClickListener != null) {
                    mItemClickListener.itemClick(bean);
                }
            }
        };
    }

    public CommentAdapter(Context context, List<CommentBean> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(@NonNull ViewGroup parent, int viewType) {
        return new holder(mInflater.inflate(R.layout.item_comment,parent,false));
    }

    @Override
    protected void onViewHolderBind(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((holder)holder).setData(mList.get(position),position);
    }

    @Override
    protected int getViewType(int pos) {
        return 0;
    }

    public void setItemClickListener(AdapterClickInterface<CommentBean> itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    class holder extends RecyclerView.ViewHolder {
        int pos;
        ViewGroup parent;
        ImageView thumb;
        TextView username;
        TextView time;
        TextView content;
        TextView reply;

        public holder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.comm_thumb);
            username = itemView.findViewById(R.id.comm_username);
            time = itemView.findViewById(R.id.comm_time);
            content = itemView.findViewById(R.id.comm_content);
            reply = itemView.findViewById(R.id.comm_reply_count);
            parent = itemView.findViewById(R.id.comm_parent);
        }

        public void setData(CommentBean bean,int position){
            if (bean == null){
                return;
            }
            parent.setTag(position);
            reply.setTag(position);
            pos = position;
            ImgLoader.displayCircle(bean.getComment_thumb(),thumb);
            username.setText(bean.getComment_user());
            time.setText(bean.getComment_date());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                content.setText(Html.fromHtml(bean.getComment_content(),Html.FROM_HTML_MODE_COMPACT));
            }else {
                content.setText(Html.fromHtml(bean.getComment_content()));
            }
            reply.setVisibility(bean.getReply_count() > 0 ? View.VISIBLE : View.GONE);
            reply.setText(String.format("%s %s",bean.getReply_count(),WordUtil.getString(R.string.reply)));
            if (!parent.hasOnClickListeners()){
                parent.setOnClickListener(mClickListener);
            }
            if (!reply.hasOnClickListeners()){
                reply.setOnClickListener(mClickListener);
            }

        }

    }


}
