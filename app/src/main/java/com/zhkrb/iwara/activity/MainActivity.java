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

package com.zhkrb.iwara.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.zhkrb.dragvideo.mainView.VideoPlayerView;
import com.zhkrb.iwara.AppConfig;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.base.AbsFragment;
import com.zhkrb.iwara.base.FragmentFrame;
import com.zhkrb.iwara.bean.VideoListBean;
import com.zhkrb.iwara.fragment.GalleryFragment;
import com.zhkrb.iwara.utils.UpdateUtil;
import com.zhkrb.iwara.utils.VideoNetWorkUtil;
import com.zhkrb.iwara.videoInfoContent.TestContent;
//import com.zhkrb.dragvideo.contentView.AbsContent;


public class MainActivity extends AppbarActivity implements VideoPlayerView.onHideFragmentListener {

    private FrameLayout mVideoPlayerLayout;
    private VideoPlayerView mPlayerView;

    static {
        setLaunchMode(GalleryFragment.class, AbsFragment.LAUNCH_MODE_SINGLE_TOP);
    }

    @Override
    protected FragmentFrame getLaunchFrame() {
        return new FragmentFrame(GalleryFragment.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void main() {
        super.main();
        UpdateUtil.checkUpdate(mContext,false);
        mVideoPlayerLayout = findViewById(R.id.video_layout);
    }

    @Override
    protected int getContentView() {
        return R.id.frame_layout;
    }


    public void playVideo(VideoListBean bean) {
        if (mPlayerView == null){
            mPlayerView = new VideoPlayerView(mContext).setContentView(mVideoPlayerLayout);
            mPlayerView.setNetworkUtil(new VideoNetWorkUtil());
//            mPlayerView.setInfoView(new AbsContent(mContext));
            mPlayerView.setHideFragmentListener(this);
            mPlayerView.setRootContentView(TestContent.class);
        }
        if (mVideoPlayerLayout.getVisibility() != View.VISIBLE){
            mVideoPlayerLayout.setVisibility(View.VISIBLE);
        }
        Bundle bundle = new Bundle();
        bundle.putString("url", AppConfig.HOST+bean.getHref());
        bundle.putString("title",bean.getTitle());
        bundle.putString("thumb",bean.getThumb());
        bundle.putString("user",bean.getUser_name());
        bundle.putString("referer",AppConfig.HOST+"/");
        bundle.putString("ua",AppConfig.UA);
        mPlayerView.show();
        mPlayerView.load(bundle);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView != null){
            if (!mPlayerView.canBackPressed()){
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onHide(boolean hide) {
        View view = findViewById(R.id.motion_layout);
        if (hide){
            if (view.getVisibility() != View.GONE){
                view.setVisibility(View.GONE);
            }
            enableSlideLayout(false);
        }else {
            if (view.getVisibility() != View.VISIBLE){
                view.setVisibility(View.VISIBLE);
            }
            enableSlideLayout(true);
        }
    }

    @Override
    public void onRemove() {
        if (mVideoPlayerLayout.getVisibility() != View.GONE){
            mVideoPlayerLayout.setVisibility(View.GONE);
        }
    }
}
