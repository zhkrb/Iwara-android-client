package com.zhkrb.iwara.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.adapter.VideoListAdapter;
import com.zhkrb.iwara.base.AbsActivity;
import com.zhkrb.iwara.base.AbsFragment;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.iwara.custom.ItemDecoration;
import com.zhkrb.iwara.custom.RefreshView.RefreshAdapter;
import com.zhkrb.iwara.custom.RefreshView.RefreshView;
import com.zhkrb.iwara.custom.RefreshView.ScaleRecyclerView;
import com.zhkrb.iwara.utils.SpUtil;
import com.zhkrb.iwara.utils.VibrateUtil;
import com.zhkrb.netowrk.NetworkCallback;
import com.zhkrb.netowrk.jsoup.JsoupUtil;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryFragment extends AbsFragment implements View.OnClickListener, ScaleRecyclerView.onScaleListener {

    private RefreshView mRefreshView;
    private VideoListAdapter mAdapter;
    private static int mGalleryMode = 0;//0 首页 1 播放 2 like
    private static int mListViewMode;// 0 双排 1单排 2 混合(仅首页)

    @Override
    protected void main() {
        ((AbsActivity)mContext).getWindow().setBackgroundDrawable(null);
        Bundle bundle = getArguments();
        if (bundle!=null){
            mGalleryMode = bundle.getInt("mdoe",0);
        }

        if (mGalleryMode == 0){
            mListViewMode = SpUtil.getInstance().getIntValue(SpUtil.INDEX_VIDEO_LIST_MODE,2);
        }else {
            mListViewMode = SpUtil.getInstance().getIntValue(SpUtil.INDEX_VIDEO_LIST_MODE,0);
        }
        mRefreshView = mRootView.findViewById(R.id.refreshView);
        final GridLayoutManager manager = new GridLayoutManager(mContext,2, RecyclerView.VERTICAL,false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mAdapter.getItemViewType(position);
                if (type == 2){//普通，1/2
                    return 1;
                }else {
                    return manager.getSpanCount();//占满一行
                }
            }
        });
        mRefreshView.setLayoutManager(manager);
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000,5,0);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setOnScaleListener(this);

        mAdapter = new VideoListAdapter(mContext);
        mAdapter.setSortMode(mGalleryMode == 0);
        mAdapter.setListMode(mListViewMode);

        mRefreshView.setDataHelper(new RefreshView.DataHelper<VideoListBean>() {
            @Override
            public RefreshAdapter<VideoListBean> getAdapter() {
                return mAdapter;
            }

            @Override
            public void loadData(int p, NetworkCallback callback) {
                JsoupUtil.getVideoList(0,p,callback);
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
        switch (view.getId()){

        }
    }

    @Override
    public void onBackPressed() {
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
        SpUtil.getInstance().setIntValue(key,mListViewMode);
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
        mAdapter.setListMode(mListViewMode);
    }
}
