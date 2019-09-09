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
 * Create by zhkrb on 2019/9/9 9:12
 */

package com.zhkrb.dragvideo.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.adapter.ValueSelectAdapter;
import com.zhkrb.dragvideo.bean.ValueSelectBean;

import java.util.List;

public class SettingSelectDialogFragment extends AbsDialogFragment implements ValueSelectAdapter.onItemClickListener {

    private advRecyclerView mRecyclerView;
    private ValueSelectAdapter mAdapter;
    private onItemClickListener mListener;
    private List<ValueSelectBean> mList;


    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog_bottom;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_setting_list;
    }

    @Override
    protected void main() {
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ValueSelectAdapter(mContext,mList);
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void setAdapter(List<ValueSelectBean> list, String value){
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getValue().equals(value)){
                list.get(i).setSelected(true);
                break;
            }
        }
        mList = list;
    }

    @Override
    public void onItemClick(ValueSelectBean bean, int pos) {
        if (mListener != null){
            mListener.onItemClick(this, bean, pos);
        }
    }

    public void setListener(onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener{
        void onItemClick(DialogFragment fragment, ValueSelectBean bean, int pos);
    }
}
