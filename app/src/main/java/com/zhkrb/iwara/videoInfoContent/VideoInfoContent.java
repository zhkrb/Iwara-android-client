///*
// * Copyright zhkrb
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// * Create by zhkrb on 2019/10/7 15:04
// */
//
//package com.zhkrb.iwara.videoInfoContent;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.text.Html;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewStub;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.constraintlayout.widget.Group;
//import androidx.core.widget.NestedScrollView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.fastjson.JSON;
//import com.zhkrb.dragvideo.contentViewBase.AbsContent;
//import com.zhkrb.dragvideo.contentViewBase.ContentFrame;
//import com.zhkrb.dragvideo.contentViewBase.VideoContentView;
//import com.zhkrb.iwara.R;
//import com.zhkrb.iwara.activity.MainActivity;
//import com.zhkrb.iwara.adapter.CommentAdapterBase;
//import com.zhkrb.iwara.adapter.RecommAdapter;
//import com.zhkrb.iwara.adapter.inter.AdapterClickInterface;
//import com.zhkrb.iwara.bean.VideoInfoBean;
//import com.zhkrb.iwara.bean.VideoListBean;
//import com.zhkrb.custom.FoldTextView;
//import com.zhkrb.custom.ItemDecoration;
//import com.zhkrb.custom.SwitchButton;
//import com.zhkrb.custom.AnimLinearLayoutManager;
//import com.zhkrb.custom.refreshView.OnScrollEndlessListener;
//import com.zhkrb.custom.refreshView.ScaleRecyclerView;
//import com.zhkrb.netowrk.jsoup.BaseJsoupCallback;
//import com.zhkrb.netowrk.jsoup.JsoupUtil;
//import com.zhkrb.netowrk.retrofit.HttpUtil;
//import com.zhkrb.utils.HttpConstsUtil;
//import com.zhkrb.glide.ImgLoader;
//import com.zhkrb.utils.ToastUtil;
//import com.zhkrb.utils.WordUtil;
//
//public class VideoInfoContent extends AbsContent implements View.OnClickListener, ViewStub.OnInflateListener {
//
//    private FoldTextView mTextTitle;
//    private FoldTextView mTextInfo;
//    private TextView mTextVideoView;
//    private TextView mTextVideoComment;
//    private TextView mTextVideoDate;
//    private Button mBtnLike;
//    private ImageView mImgThumb;
//    private TextView mTextAuthorName;
//    private TextView mTextAuthorFollow;
//    private SwitchButton mBtnFollow;
//    private ScaleRecyclerView mRecyclerViewCorr;
//    private ScaleRecyclerView mRecyclerViewRecomm;
//    private ScaleRecyclerView mRecyclerViewComment;
//
//    private VideoInfoBean mInfoBean;
//    private AnimLinearLayoutManager mCorrLayoutManager;
//    private AnimLinearLayoutManager layoutManager2;
//    private AnimLinearLayoutManager layoutManager3;
//    private ViewStub mStubComm;
//    private Group mGroupCorr;
//    private Group mGroupRecomm;
//
//    public VideoInfoContent(Context context) {
//        super(context);
//    }
//
//    public VideoInfoContent(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public VideoInfoContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    protected void prepareLoad() {
//        if (mArgs != null){
//            JsoupUtil.getVideoInfo(mArgs.getString("url"), HttpConstsUtil.GET_VIDEO_INFO,mCallback);
//        }
//    }
//
//    @Override
//    protected void main() {
//        mTextTitle = mRootView.findViewById(R.id.title);
//        mTextInfo = mRootView.findViewById(R.id.text_info);
//        mTextTitle.setOnClickListener(v ->{
//            boolean b = mTextTitle.isFold();
//            mTextTitle.setFold(!b);
//            mTextInfo.setFold(!b);
//        });
//        mTextVideoView = mRootView.findViewById(R.id.text_video_view);
//        mTextVideoComment = mRootView.findViewById(R.id.text_video_comment);
//        mTextVideoDate = mRootView.findViewById(R.id.text_video_upload_date);
//        mBtnLike = mRootView.findViewById(R.id.btn_like);
//        mImgThumb = mRootView.findViewById(R.id.artist_thumb);
//        mTextAuthorName = mRootView.findViewById(R.id.artist_name);
//        mTextAuthorFollow = mRootView.findViewById(R.id.artist_follow);
//        mBtnFollow = mRootView.findViewById(R.id.btn_follow);
//        mStubComm = mRootView.findViewById(R.id.stub_comment);
//        initView();
//    }
//
//    private void initView() {
//        mTextTitle.setText(mInfoBean.getTitle());
//        mTextAuthorName.setText(mInfoBean.getAuthor_name());
//        ((VideoContentView)getParent()).getReloadListener().reSetAuthorName(mInfoBean.getAuthor_name());
//        mTextAuthorFollow.setOnClickListener(this);
//        mTextVideoComment.setText(String.valueOf(mInfoBean.getComment_count()));
//        mTextVideoView.setText(mInfoBean.getAuthor_video_view());
//        mTextVideoDate.setText(String.format(WordUtil.getString(R.string.uploaddate),
//                mInfoBean.getAuthor_video_upload_date()));
//        mTextInfo.setText(Html.fromHtml(mInfoBean.getAuthor_video_info()));
//        mBtnLike.setText(mInfoBean.getAuthor_video_like());
//        ImgLoader.displayCircle(mInfoBean.getAuthor_thumb(),mImgThumb);
//        mBtnLike.setOnClickListener(this);
//        mBtnFollow.setOnClickListener(this);
//        mTextAuthorFollow.setOnClickListener(this);
//        mRootView.findViewById(R.id.btn_share).setOnClickListener(this);
//        mRootView.findViewById(R.id.btn_download).setOnClickListener(this);
//        mRootView.findViewById(R.id.btn_save).setOnClickListener(this);
//        mGroupCorr = mRootView.findViewById(R.id.group_corr);
//        mGroupRecomm = mRootView.findViewById(R.id.group_recomm);
//        mStubComm.setOnInflateListener(this);
//        post(() ->setOnScrollChangeListener(mScrollChangeListener));
//        initRecycler();
//    }
//
//    private void initRecycler() {
//        if (mInfoBean.getAuthor_video_from_user() != null && mInfoBean.getAuthor_video_from_user().size() > 0){
//            mGroupCorr.setVisibility(VISIBLE);
//            initCorr();
//        }else {
//            mGroupCorr.setVisibility(GONE);
//        }
//        if (mInfoBean.getAuthor_video_recomm() != null && mInfoBean.getAuthor_video_recomm().size() > 0){
//            mGroupRecomm.setVisibility(VISIBLE);
//            initRecomm();
//        }else {
//            mGroupRecomm.setVisibility(GONE);
//        }
//    }
//
//
//    private OnScrollChangeListener mScrollChangeListener = new OnScrollChangeListener() {
//        @Override
//        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//            View onlyChild = getChildAt(0);
//            if (onlyChild.getHeight() <= scrollY + getHeight()) {
//                if (mInfoBean.getComment_item_list() != null &&
//                        mInfoBean.getComment_item_list().size() >0 &&
//                        mStubComm != null && mStubComm.getParent() != null){
//                    mStubComm.inflate();
//                    mStubComm = null;
//                }
//            }
//        }
//    };
//
//
//    @Override
//    protected int getContentLayoutId() {
//        return R.layout.content_video_info;
//    }
//
//
//    private BaseJsoupCallback mCallback = new BaseJsoupCallback() {
//        @Override
//        public void onStart() {
//
//        }
//
//        @Override
//        public void onSuccess(int code, String msg, String info) {
//            if (code != 200 || TextUtils.isEmpty(info)){
//                loadFail();
//                return;
//            }
//            mInfoBean = JSON.parseObject(info,VideoInfoBean.class);
//            if (mInfoBean!=null){
//                loadComplete();
//                return;
//            }
//            loadFail();
//        }
//
//        @Override
//        public void onFinish() {
//
//        }
//
//        @Override
//        public void onError(int code, String msg) {
//            ToastUtil.show(msg);
//            loadFail();
//        }
//    };
//
//
//    @Override
//    public void release() {
//        super.release();
//        HttpUtil.cancel(HttpConstsUtil.GET_VIDEO_INFO);
//    }
//
//    @Override
//    public void setAnim(boolean anim) {
//        super.setAnim(anim);
//        if (mRecyclerViewCorr != null){
//            mRecyclerViewCorr.setAnim(isAnim);
//            mCorrLayoutManager.setAnim(isAnim);
//        }
//        if (mRecyclerViewRecomm != null){
//            mRecyclerViewRecomm.setAnim(isAnim);
//            layoutManager2.setAnim(isAnim);
//        }
//        if (mRecyclerViewComment != null){
//            mRecyclerViewComment.setAnim(isAnim);
//            layoutManager3.setAnim(isAnim);
//        }
//    }
//
//    @Override
//    public void onInflate(ViewStub stub, View inflated) {
//        initComment();
//    }
//
//    private void initComment() {
//        mRecyclerViewComment = mRootView.findViewById(R.id.recyclerView_user_comment);
//        layoutManager3 = new AnimLinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//        mRecyclerViewComment.setHasFixedSize(true);
//        mRecyclerViewComment.setLayoutManager(layoutManager3);
//        mRecyclerViewComment.addOnScrollListener(commitScrollEndListener);
//        CommentAdapterBase commentAdapter = new CommentAdapterBase(mContext);
//        mRecyclerViewComment.setAdapter(commentAdapter);
//        commentAdapter.setList(mInfoBean.getComment_item_list());
//        mRecyclerViewComment.addItemDecoration(new ItemDecoration(mContext, Color.parseColor("#14000000"),0,1));
//    }
//
//    private void initRecomm() {
//        mRecyclerViewRecomm = mRootView.findViewById(R.id.recyclerView_video_recomm);
//        layoutManager2 = new AnimLinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//        layoutManager2.setCanScrollVert(false);
//        mRecyclerViewRecomm.setHasFixedSize(true);
//        mRecyclerViewRecomm.setLayoutManager(layoutManager2);
//        RecommAdapter recommAdapter = new RecommAdapter(mContext, mInfoBean.getAuthor_video_recomm());
//        recommAdapter.setClickListener(this);
//        mRecyclerViewRecomm.setAdapter(recommAdapter);
//    }
//
//    private void initCorr() {
//        mRecyclerViewCorr = mRootView.findViewById(R.id.recyclerView_user_corr);
//        mCorrLayoutManager = new AnimLinearLayoutManager(mContext,RecyclerView.VERTICAL,false);
//        mRecyclerViewCorr.setHasFixedSize(true);
//        mRecyclerViewCorr.setLayoutManager(mCorrLayoutManager);
//        RecommAdapter corrAdapter = new RecommAdapter(mContext,mInfoBean.getAuthor_video_from_user());
//        corrAdapter.setClickListener(this);
//        mRecyclerViewCorr.setAdapter(corrAdapter);
//    }
//
//    private OnScrollEndlessListener commitScrollEndListener = new OnScrollEndlessListener() {
//        @Override
//        public void onLoadMore() {
//
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//        ContentFrame frame = new ContentFrame(TestContent.class);
//        loadNewContent(frame);
//    }
//
//    @Override
//    public void itemClick(VideoListBean bean) {
//        ((MainActivity)mContext).playVideo(bean);
//    }
//
//}
