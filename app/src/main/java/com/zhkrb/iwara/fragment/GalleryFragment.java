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
import com.zhkrb.iwara.netowrk.jsoup.JsoupCallback;
import com.zhkrb.iwara.netowrk.retrofit.HttpUtil;
import com.zhkrb.iwara.utils.HttpConstsUtil;
import com.zhkrb.iwara.utils.SpUtil;
import com.zhkrb.iwara.utils.ToastUtil;
import com.zhkrb.iwara.utils.VibrateUtil;
import com.zhkrb.iwara.netowrk.NetworkCallback;
import com.zhkrb.iwara.netowrk.jsoup.JsoupUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryFragment extends AbsFragment implements View.OnClickListener, ScaleRecyclerView.onScaleListener, VideoListAdapter.onItemClickListener {

    private static final String DATA_LIST = "data_list";
    private static final String DATA_LOADED = "isdata_loaded";
    private static final String SAVED_POSITION = "saved_position";
    private static final String SAVED_POSITION_OFFSET = "saved_position_offset";
    private RefreshView mRefreshView;
    private VideoListAdapter mAdapter;
    private static int mGalleryMode = 0;//0 上传时间 1 播放 2 like
    private static int mListViewMode;// 0 双排 1单排 2 混合(仅首页)
    private boolean isFirstLoad = true;


    @Override
    protected void main(Bundle savedInstanceState) {
        ((AbsActivity)mContext).getWindow().setBackgroundDrawable(null);
        Bundle bundle = getArguments();

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
//        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000,5,5);
//        decoration.setDrawBorderLeftAndRight(true);
//        decoration.setOnlySetItemOffsetsButNoDraw(true);
//        mRefreshView.setItemDecoration(decoration);
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
                JsoupUtil.getVideoList(mGalleryMode,p, HttpConstsUtil.GET_VIDEO_LIST+getTag(), (JsoupCallback) callback);
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
                isFirstLoad = dataCount<= 0;
            }

            @Override
            public int maxPageItemCount() {
                return 36;
            }
        }).init();

        if (savedInstanceState != null){
            isFirstLoad = savedInstanceState.getBoolean(DATA_LOADED,true);
            if (!isFirstLoad){
                List list = (List) savedInstanceState.getSerializable(DATA_LIST);
                if (list!=null && list.size()>0){
                    mRefreshView.restoreData(list);
                    int pos = savedInstanceState.getInt(SAVED_POSITION,0);
                    int offset = savedInstanceState.getInt(SAVED_POSITION_OFFSET,0);
                    if (pos > 0){
                        manager.scrollToPositionWithOffset(pos, offset);
                    }
                }
            }
        }

        if (isFirstLoad){
            mRefreshView.initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!isFirstLoad){
            outState.putSerializable(DATA_LIST, (Serializable) mAdapter.getList());
            int pos = 0,offset = 0;
            GridLayoutManager layoutManager = ((GridLayoutManager)mRefreshView.getDataHelper().getAdapter().getRecyclerView().getLayoutManager());
            if (layoutManager != null){
                pos = layoutManager.findLastVisibleItemPosition();
                View view = layoutManager.findViewByPosition(pos);
                if (view != null){
                    offset = view.getTop();
                }
            }
            outState.putInt(SAVED_POSITION,pos);
            outState.putInt(SAVED_POSITION_OFFSET,offset);
        }
        outState.putBoolean(DATA_LOADED,isFirstLoad);
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
    public void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(HttpConstsUtil.GET_VIDEO_LIST+getTag());
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
