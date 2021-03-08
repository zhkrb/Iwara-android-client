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
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhkrb.dragvideo.mainView.VideoPlayerView;
import com.zhkrb.iwara.AppConfig;
import com.zhkrb.iwara.R;
import com.zhkrb.base.AbsFragment;
import com.zhkrb.base.FragmentFrame;
import com.zhkrb.iwara.bean.VideoListBean;
//import com.zhkrb.iwara.fragment.GalleryFragment;
import com.zhkrb.iwara.fragment.BaseChildFragment;
import com.zhkrb.iwara.fragment.BaseTestFragment1;
import com.zhkrb.iwara.fragment.BaseTestFragment2;
import com.zhkrb.iwara.fragment.BaseTestFragment3;
import com.zhkrb.iwara.fragment.TestFragment;
import com.zhkrb.utils.UpdateUtil;
import com.zhkrb.utils.VideoDnsUtil;
import com.zhkrb.utils.VideoNetWorkUtil;
//import com.zhkrb.iwara.videoInfoContent.VideoInfoContent;



public class MainActivity extends AppbarActivity implements VideoPlayerView.onHideFragmentListener {


    private VideoPlayerView mPlayerView;
    private ViewGroup mContentLayout;

    static {
//        setLaunchMode(GalleryFragment.class, AbsFragment.LAUNCH_MODE_BASE);
        setLaunchMode(TestFragment.class, AbsFragment.LAUNCH_MODE_PAGE);
        setLaunchMode(BaseTestFragment1.class, AbsFragment.LAUNCH_MODE_BASE);
        setLaunchMode(BaseTestFragment2.class, AbsFragment.LAUNCH_MODE_BASE);
        setLaunchMode(BaseTestFragment3.class, AbsFragment.LAUNCH_MODE_BASE);
        setLaunchMode(BaseChildFragment.class, AbsFragment.LAUNCH_MODE_PAGE);
    }

    private ConstraintLayout mParentLayout;
    private boolean mCanClick = true;

    @Override
    protected FragmentFrame getLaunchFrame() {
//        return new FragmentFrame(GalleryFragment.class);
        return new FragmentFrame(TestFragment.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void main() {
        super.main();
        UpdateUtil.checkUpdate(mContext,false);
        mParentLayout = findViewById(R.id.parent_layout);
        mContentLayout = findViewById(R.id.motion_layout);
    }

    @Override
    protected int getContentView() {
        return R.id.frame_layout;
    }


    public void playVideo(VideoListBean bean) {
        if (!mCanClick){
            return;
        }
        if (mPlayerView == null){
            mPlayerView = new VideoPlayerView(this).setContentView(mParentLayout);
            mPlayerView.setNetworkUtil(new VideoNetWorkUtil());
            mPlayerView.setDnsUtil(new VideoDnsUtil());
//            mPlayerView.setInfoView(new AbsContent(mContext));
            mPlayerView.setHideFragmentListener(this);
//            mPlayerView.setRootContentView(VideoInfoContent.class);
        }
//        if (mContentLayout.getVisibility() != View.VISIBLE){
//            mContentLayout.setVisibility(View.VISIBLE);
//        }
        Bundle bundle = new Bundle();
        bundle.putString("url", AppConfig.HOST+bean.getHref());
        bundle.putString("title",bean.getTitle());
        bundle.putString("thumb",bean.getThumb());
        bundle.putString("user",bean.getUserName());
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
        view.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRemove() {
//        if (mVideoPlayerLayout.getVisibility() != View.GONE){
//            mVideoPlayerLayout.setVisibility(View.GONE);
//        }
    }

    @Override
    public void canClick(boolean b) {
        mCanClick = b;
    }
}
