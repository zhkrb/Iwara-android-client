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
 * Create by zhkrb on 2019/9/17 19:07
 */

package com.zhkrb.dragvideo.videoPlayer.gsyvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.transition.TransitionManager;

import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getActionBarHeight;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getStatusBarHeight;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.hideNavKey;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.hideSupportActionBar;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.showNavKey;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.showSupportActionBar;

public class MediaCodecVideoplayer extends StandardGSYVideoPlayer {

    private SurfaceTexture mSaveSurface = null;
    private ViewGroup msaveContainer;
    private ViewGroup.LayoutParams msaveLayoutPraams;

    public MediaCodecVideoplayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MediaCodecVideoplayer(Context context) {
        super(context);
    }

    public MediaCodecVideoplayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(final Context context, boolean actionBar, boolean statusBar) {

        mSystemUiVisibility = ((Activity) context).getWindow().getDecorView().getSystemUiVisibility();

        hideSupportActionBar(context, actionBar, statusBar);

        if (mHideKey) {
            hideNavKey(context);
        }

        this.mActionBar = actionBar;

        this.mStatusBar = statusBar;

        mListItemRect = new int[2];

        mListItemSize = new int[2];

        final ViewGroup vp = getViewGroup();

        removeVideo(vp, getFullId());

        //处理暂停的逻辑
        pauseFullCoverLogic();

        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }
//
        saveLocationStatus(context, statusBar, actionBar);
//
//        //切换时关闭非全屏定时器
//        cancelProgressTimer();
//
//        boolean hadNewConstructor = true;

        setId(getFullId());
        setIfCurrentIsFullscreen(true);
//        setVideoAllCallBack(mVideoAllCallBack);

        if (getFullscreenButton() != null) {
            getFullscreenButton().setImageDrawable(mContext.getResources().getDrawable(getShrinkImageRes(),mContext.getTheme()));
            getFullscreenButton().setOnClickListener(v -> {
                if (mBackFromFullScreenListener == null) {
                    clearFullscreenLayout();
                } else {
                    mBackFromFullScreenListener.onClick(v);
                }
            });
        }

        if (getBackButton() != null) {
            getBackButton().setVisibility(VISIBLE);
            getBackButton().setOnClickListener(v -> {
                if (mBackFromFullScreenListener == null) {
                    clearFullscreenLayout();
                } else {
                    mBackFromFullScreenListener.onClick(v);
                }
            });
        }

        final LayoutParams lpParent = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Color.BLACK);

        if (mShowFullAnimation){
            mFullAnimEnd = false;
            LayoutParams lp = new LayoutParams(getWidth(), getHeight());
            lp.setMargins(mListItemRect[0], mListItemRect[1], 0, 0);
            msaveContainer = (ViewGroup) getParent();
            msaveContainer.removeView(this);
            frameLayout.addView(this, lp);
            vp.addView(frameLayout, lpParent);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition(vp);
                    resolveFullVideoShow(context, MediaCodecVideoplayer.this, frameLayout);
                    mFullAnimEnd = true;
                }
            }, 300);
        }else {
            LayoutParams lp = new LayoutParams(getWidth(), getHeight());
            msaveContainer = (ViewGroup) getParent();
            msaveContainer.removeView(this);
            frameLayout.addView(this, lp);
            vp.addView(frameLayout, lpParent);
//        setVisibility(INVISIBLE);
//        frameLayout.setVisibility(INVISIBLE);
            resolveFullVideoShow(context, this, frameLayout);

        }



//        mSaveSurface = ((GSYTextureExView)mTextureView.getShowView()).getSaveSurfaceTexture();
//
        addTextureView();
//        mTextureViewContainer.setVisibility(GONE);
//        frameLayout.setVisibility(GONE);
//
//        startProgressTimer();
        checkoutState();

        return null;
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        if (oldF != null && oldF.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) oldF.getParent();
            vp.removeView(viewGroup);
        }
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        ((ViewGroup)getParent()).removeView(this);
        msaveContainer.addView(this,msaveLayoutPraams);

        setStateAndUi(mCurrentState);

        addTextureView();
        mSaveChangeViewTIme = System.currentTimeMillis();
        if (mVideoAllCallBack != null) {
            Debuger.printfError("onQuitFullscreen");
            mVideoAllCallBack.onQuitFullscreen(mOriginUrl, mTitle, this);
        }
        mIfCurrentIsFullscreen = false;
        if (mHideKey) {
            showNavKey(mContext, mSystemUiVisibility);
        }
        showSupportActionBar(mContext, mActionBar, mStatusBar);
        if(getFullscreenButton() != null) {
            getFullscreenButton().setImageDrawable(mContext.getResources().getDrawable(getEnlargeImageRes(),mContext.getTheme()));
            getFullscreenButton().setOnClickListener(this);
        }


    }

    private ViewGroup getViewGroup() {
        return (ViewGroup) (CommonUtil.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
    }

    /**
     * 移除没用的
     */
    private void removeVideo(ViewGroup vp, int id) {
        View old = vp.findViewById(id);
        if (old != null) {
            if (old.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) old.getParent();
                vp.removeView(viewGroup);
            }
        }
    }

    /**
     * 全屏的暂停的时候返回页面不黑色
     */
    private void pauseFullCoverLogic() {
        if (mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE && mTextureView != null
                && (mFullPauseBitmap == null || mFullPauseBitmap.isRecycled()) && mShowPauseCover) {
            try {
                initCover();
            } catch (Exception e) {
                e.printStackTrace();
                mFullPauseBitmap = null;
            }
        }
    }

    /**
     * 保存大小和状态
     */
    private void saveLocationStatus(Context context, boolean statusBar, boolean actionBar) {
        getLocationOnScreen(mListItemRect);
        int statusBarH = getStatusBarHeight(context);
        int actionBerH = getActionBarHeight((Activity) context);
        if (statusBar) {
            mListItemRect[1] = mListItemRect[1] - statusBarH;
        }
        if (actionBar) {
            mListItemRect[1] = mListItemRect[1] - actionBerH;
        }
        mListItemSize[0] = getWidth();
        mListItemSize[1] = getHeight();
        msaveLayoutPraams = getLayoutParams();

    }

    @Override
    protected void addTextureView() {
        if (mTextureView == null){
            mTextureView = new GSYRenderExView();
            ((GSYRenderExView)mTextureView).addViewEx(getContext(), mTextureViewContainer, mRotate, this, this, mEffectFilter, mMatrixGL, mRenderer, mMode,mSaveSurface);

        }else {
            ((GSYRenderExView)mTextureView).reAddView(mTextureViewContainer, mRotate, (GSYTextureExView) mTextureView.getShowView());
        }
        //        mSaveSurface = ((GSYTextureExView)mTextureView.getShowView()).getSaveSurfaceTexture();
    }


}
