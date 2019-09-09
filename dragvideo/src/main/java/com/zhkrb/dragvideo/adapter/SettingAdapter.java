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
 * Create by zhkrb on 2019/9/8 16:38
 */

package com.zhkrb.dragvideo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.bean.SettingBean;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.Vh> {

    private List<SettingBean> mList;
    private LayoutInflater mLayoutInflater;
    private View.OnClickListener mClickListener;
    private onItemClickListener mListener;

    public SettingAdapter(Context context, List<SettingBean> list) {

        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        mClickListener = v -> {
            Object tag = v.getTag();
            if (tag != null) {
                int position = (int) tag;
                SettingBean bean = mList.get(position);
                if (mListener != null) {
                    mListener.onItemClick(bean, position);
                }
            }
        };
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mLayoutInflater.inflate(R.layout.item_setting,parent,false));
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        holder.setData(mList.get(position),position);
    }

    public void setListener(onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener{
        void onItemClick(SettingBean bean,int pos);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView thumb;
        TextView title;
        TextView point;
        TextView value;

        Vh(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            point = itemView.findViewById(R.id.point);
            value = itemView.findViewById(R.id.value);
        }


        void setData(SettingBean settingBean,int pos) {
            itemView.setTag(pos);
            itemView.setOnClickListener(mClickListener);
            thumb.setImageResource(settingBean.getThumb());
            title.setText(settingBean.getTitle());
            if (!TextUtils.isEmpty(settingBean.getValue())){
                point.setVisibility(View.VISIBLE);
                value.setVisibility(View.VISIBLE);
                value.setText(settingBean.getValue());
            }else {
                point.setVisibility(View.GONE);
                value.setVisibility(View.GONE);
            }
        }

    }
}
