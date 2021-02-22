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
 * Create by zhkrb on 2019/9/9 9:32
 */

package com.zhkrb.dragvideo.adapter;

import android.content.Context;
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
import com.zhkrb.dragvideo.bean.ValueSelectBean;

import java.util.List;

public class ValueSelectAdapter extends RecyclerView.Adapter<ValueSelectAdapter.Vh> {

    private List<ValueSelectBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mClickListener;
    private onItemClickListener mListener;


    public ValueSelectAdapter(Context context,List<ValueSelectBean> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
        mClickListener = v->{
            Object tag = v.getTag();
            if (tag != null) {
                int position = (int) tag;
                ValueSelectBean bean = mList.get(position);
                if (mListener != null) {
                    mListener.onItemClick(bean, position);
                }
            }
        };

    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(0,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        holder.setData(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    public void setListener(onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener{
        void onItemClick(ValueSelectBean bean,int pos);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView thumb;
        TextView title;


        Vh(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);

        }


        void setData(ValueSelectBean settingBean,int pos) {
            itemView.setTag(pos);
            itemView.setOnClickListener(mClickListener);
            if (settingBean.isSelected()){
                thumb.setImageResource(R.drawable.ic_check_white_24dp);
            }else {
                thumb.setImageDrawable(null);
            }
            title.setText(settingBean.getValueText());
        }
    }
}
