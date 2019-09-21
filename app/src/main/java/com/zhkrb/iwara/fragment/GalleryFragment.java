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

package com.zhkrb.iwara.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.google.android.material.button.MaterialButton;
import com.zhkrb.iwara.AppConfig;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.activity.MainActivity;
import com.zhkrb.iwara.adapter.VideoListAdapter;
import com.zhkrb.iwara.base.AbsActivity;
import com.zhkrb.iwara.base.AbsFragment;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.iwara.custom.ItemDecoration;
import com.zhkrb.iwara.custom.refreshView.RefreshAdapter;
import com.zhkrb.iwara.custom.refreshView.RefreshView;
import com.zhkrb.iwara.custom.refreshView.ScaleRecyclerView;
import com.zhkrb.iwara.utils.SpUtil;
import com.zhkrb.iwara.utils.ToastUtil;
import com.zhkrb.iwara.utils.VibrateUtil;
import com.zhkrb.iwara.netowrk.NetworkCallback;
import com.zhkrb.iwara.netowrk.jsoup.JsoupUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryFragment extends AbsFragment implements View.OnClickListener, ScaleRecyclerView.onScaleListener, VideoListAdapter.onItemClickListener {

    private RefreshView mRefreshView;
    private VideoListAdapter mAdapter;
    private static int mGalleryMode = 0;//0 上传时间 1 播放 2 like
    private static int mListViewMode;// 0 双排 1单排 2 混合(仅首页)

    private List<MaterialButton> mButtons;


    @Override
    protected void main() {
        ((AbsActivity)mContext).getWindow().setBackgroundDrawable(null);
        Bundle bundle = getArguments();
//        MaterialButton btn_time = mRootView.findViewById(R.id.btn_time);
//        MaterialButton btn_play = mRootView.findViewById(R.id.btn_play);
//        MaterialButton btn_like = mRootView.findViewById(R.id.btn_like);
//        btn_time.setOnClickListener(this);
//        btn_play.setOnClickListener(this);
//        btn_like.setOnClickListener(this);
        mButtons = new ArrayList<>(3);
//        mButtons.add(btn_time);
//        mButtons.add(btn_play);
//        mButtons.add(btn_like);
        mGalleryMode = AppConfig.getInstance().getGalleryMode();
        mListViewMode = AppConfig.getInstance().getGalleryListMode(mGalleryMode);
        mRefreshView = mRootView.findViewById(R.id.refreshView);
        final GridLayoutManager manager = new GridLayoutManager(mContext,2, RecyclerView.VERTICAL,false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mAdapter.getItemViewType(position);//普通，1/2
                return type == 2 ? 1: manager.getSpanCount();//或占满一行
            }
        });
        mRefreshView.setLayoutManager(manager);
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000,5,5);
        decoration.setDrawBorderLeftAndRight(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setOnScaleListener(this);

        mAdapter = new VideoListAdapter(mContext);
        mAdapter.setSortMode(mGalleryMode == 0);
        mAdapter.setListMode(mListViewMode);
        mAdapter.setClickListener(this);
        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoListBean>() {
            @Override
            public RefreshAdapter<VideoListBean> getAdapter() {
                return mAdapter;
            }

            @Override
            public void loadData(int p, NetworkCallback callback) {
//                JsoupUtil.getVideoList(0,p,callback);
                JsoupUtil.getVideoList(mGalleryMode,p,callback);
            }

            @Override
            public List<VideoListBean> processData(String info) {
                return JSON.parseArray(info,VideoListBean.class);
            }

            @Override
            public void onRefresh(List<VideoListBean> list) {

            }

            @Override
            public void onNoData(boolean noData) {

            }

            @Override
            public void onLoadDataCompleted(int dataCount) {

            }

            @Override
            public int maxPageItemCount() {
                return 36;
            }
        });
        mRefreshView.initData();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void onNewArguments(Bundle args) {

    }

    @Override
    public void onClick(View view) {
//        int pos = 0;
//        switch (view.getId()){
//            case R.id.btn_time:
//                pos =  0;
//                break;
//            case R.id.btn_play:
//                pos =  1;
//                break;
//            case R.id.btn_like:
//                pos =  2;
//                break;
//        }
//        if (pos == mGalleryMode){
//            return;
//        }
//        targetMode(pos);
    }

    private void targetMode(int pos) {
        mGalleryMode = pos;
        mListViewMode = AppConfig.getInstance().getGalleryListMode(pos);
        mAdapter.setListMode(mListViewMode);
        mRefreshView.initData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onZoomIn() {    //放大
        if (mListViewMode == 2 || (mGalleryMode !=0 && mListViewMode == 1) || mAdapter == null){
            return;
        }
        switch (mListViewMode){
            case 0:
                mListViewMode = 1;
                break;
            case 1:
                mListViewMode = 2;
                break;
        }
        VibrateUtil.Short();
        saveListMode();
        mAdapter.setListMode(mListViewMode);
    }

    @Override
    public void onZoomOut() {   //缩小
        if (mListViewMode == 0 || mAdapter == null){
            return;
        }
        switch (mListViewMode){
            case 2:
                mListViewMode = 1;
                break;
            case 1:
                mListViewMode = 0;
                break;
        }
        VibrateUtil.Short();
        saveListMode();
        mAdapter.setListMode(mListViewMode);
    }

    private void saveListMode(){
        String key = "";
        switch (mGalleryMode){
            case 0:
                key = SpUtil.INDEX_VIDEO_LIST_MODE;
                break;
            case 1:
                key = SpUtil.VIEW_VIDEO_LIST_MODE;
                break;
            case 2:
                key = SpUtil.LIKE_VIDEO_LIST_MODE;
                break;
        }
        AppConfig.getInstance().putGalleryListMode(key,mListViewMode);
    }



    //        bundle.putString("url","http://192.168.0.6/test.flv");
    //        bundle.putString("share_url","test_share_url");
    //        bundle.putString("title","【魔法少女小圆MAD】The Eyes【叛逆的物語】");
    //        bundle.putString("user","Hiroki");
    //        bundle.putString("thumb","http://192.168.0.6/test.png");

    @Override
    public void itemClick(VideoListBean bean) {
        if (TextUtils.isEmpty(bean.getHref())){
            ToastUtil.show(R.string.url_is_empty);
        }else {
            ((MainActivity)mContext).playVideo(bean);
        }
    }
}
