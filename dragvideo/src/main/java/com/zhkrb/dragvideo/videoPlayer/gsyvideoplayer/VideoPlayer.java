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

package com.zhkrb.dragvideo.videoPlayer.gsyvideoplayer;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.transition.TransitionManager;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.videoPlayer.IVideoPlayer;
import com.zhkrb.dragvideo.videoPlayer.PlayerStateListener;
import com.zhkrb.dragvideo.widget.ImgLoader;

import java.util.Map;

import moe.codeest.enviews.ENDownloadView;

import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getActionBarHeight;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getStatusBarHeight;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.hideNavKey;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.hideSupportActionBar;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.showNavKey;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.showSupportActionBar;

public class VideoPlayer extends MediaCodecVideoplayer implements VideoAllCallBack, DefaultLifecycleObserver, IVideoPlayer, GSYVideoProgressListener {

    private Context mContext;
    private String mShareUrl;
    private OrientationUtils orientationUtils;
    private TextView mErrorTextView;

    protected boolean isPlay;
    protected boolean isPause;


    private PlayerStateListener mPlayerStateListener;

    //拖动时 控件状态
    private int mViewState = VIEW_STATE_NOM;
    //动画
    private Animation mFadeIn;
    private Animation mFadeOut;
    private Animation mFadeInAdd;
    private Animation mFadeOutAdd;
    private ValueAnimator mScaleShow;
    private ValueAnimator mScaleHide;
    private boolean isFirstLoad = true;

    private Map<String,String> mHeader;


    public VideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }


    public VideoPlayer(Context context) {
        super(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_player_controller;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mContext = context;
        ((AppCompatActivity)mContext).getLifecycle().addObserver(this);
        mErrorTextView = findViewById(R.id.text_error_info);
        findViewById(R.id.btn_down).setOnClickListener(this);
        findViewById(R.id.btn_more).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        getFullscreenButton().setOnClickListener(this);
        setVideoAllCallBack(this);
        orientationUtils = new OrientationUtils((Activity) mContext, this);
        orientationUtils.setEnable(false);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setThumbImageView(imageView);
        if (mContext instanceof AppCompatActivity){
            ((AppCompatActivity)mContext).getLifecycle().addObserver(this);
        }else {
            throw new RuntimeException("Activity need extends AppCompatActivity");
        }
        mFadeIn = AnimationUtils.loadAnimation(mContext,R.anim.anim_show);
        mFadeOut = AnimationUtils.loadAnimation(mContext,R.anim.anim_hide);
        mFadeInAdd = AnimationUtils.loadAnimation(mContext,R.anim.anim_show);
        mFadeOutAdd = AnimationUtils.loadAnimation(mContext,R.anim.anim_hide);

        mScaleShow = new ValueAnimator().setDuration(333);
        mScaleShow.setFloatValues(0f,1.0f);
        mScaleShow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mProgressBar.getThumb()!=null){
                    mProgressBar.getThumb().setAlpha((int) (255*((float)animation.getAnimatedValue())));
                }
            }
        });
        mScaleHide = new ValueAnimator().setDuration(200);
        mScaleHide.setFloatValues(1.0f,0.0f);
        mScaleHide.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mProgressBar.getThumb()!=null){
                    mProgressBar.getThumb().setAlpha((int) (255*((float)animation.getAnimatedValue())));
                }
            }
        });
        mScaleHide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setEnabled(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        GSYVideoType.enableMediaCodec();
        GSYVideoType.enableMediaCodecTexture();
        setSeekRatio(0.1f);
        setGSYVideoProgressListener(this);
        GSYVideoManager.instance().setTimeOut(30000,true);
    }

    //扩大Seekbar点击范围
    private void setSeekBarDelegate() {
        Rect touchRect = new Rect();
        mProgressBar.getHitRect(touchRect);
        touchRect.top += mContext.getResources().getDimensionPixelOffset(R.dimen.cut_4dp);
        TouchDelegate mSeekTouchDelegate = new TouchDelegate(touchRect,mProgressBar);
        if (mProgressBar.getParent() instanceof View){
            ((View) mProgressBar.getParent()).setTouchDelegate(mSeekTouchDelegate);
        }
    }

    @Override
    public boolean isAutoFullWithSize() {
        return true;
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.i("videoplayer","onPause");
        onVideoPause();
        if (orientationUtils != null) {
            orientationUtils.setIsPause(true);
        }
        if (mCurrentState == CURRENT_STATE_PAUSE){
            isPause = true;
        }
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.i("videoplayer","onResume");
        if (isPlay&&!isPause){
            onVideoResume();
            isPause = false;
        }
        if (orientationUtils != null) {
            orientationUtils.setIsPause(false);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.i("videoplayer","onDestroy");
        if (isPlay) {
            release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    protected void changeUiToClear() {
        super.changeUiToClear();
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.i("videoplayer","onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {

    }




    @Override
    public int getShrinkImageRes() {
        return R.drawable.ic_fullscreen_exit_black_24dp;
    }

    @Override
    public int getEnlargeImageRes() {
        return R.drawable.ic_fullscreen_black_24dp;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isPlay && !isPause) {
            onConfigurationChanged((Activity) mContext, newConfig, orientationUtils, true, true);
        }
    }

    @Override
    protected void updateStartImage() {
        ImageView imageView = (ImageView) mStartButton;
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            imageView.setImageResource(R.drawable.ic_pause_24dp);
        } else if (mCurrentState == CURRENT_STATE_ERROR) {
            imageView.setImageResource(R.drawable.ic_error_outline_24dp);
        } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE){
            imageView.setImageResource(R.drawable.ic_replay_24dp);
        }else {
            imageView.setImageResource(R.drawable.ic_play_arrow_24dp);
        }
    }





    @Override
    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch (state){
            case CURRENT_STATE_PLAYING:
                if (mPlayerStateListener!= null){
                    mPlayerStateListener.onStateChange(PlayerStateListener.PLAY);
                }
                break;
            case CURRENT_STATE_PAUSE:
                if (mPlayerStateListener!= null){
                    mPlayerStateListener.onStateChange(PlayerStateListener.PAUSE);
                }
                break;
            case CURRENT_STATE_PREPAREING:
                if (mPlayerStateListener != null){
                    mPlayerStateListener.onStateChange(PlayerStateListener.LOADING);
                }
                break;
            case CURRENT_STATE_ERROR:
                if (mErrorTextView.getVisibility()!=VISIBLE){
                    mErrorTextView.setVisibility(VISIBLE);
                }
                break;
        }
        if (mCurrentState != CURRENT_STATE_ERROR){
            if (mErrorTextView.getVisibility()!=GONE){
                mErrorTextView.setVisibility(GONE);
            }
            mErrorTextView.setText("");
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_down) {
            if (isIfCurrentIsFullscreen()){
                clearFullscreenLayout();
            }
            if (mPlayerStateListener != null){
                mPlayerStateListener.onBtnViewDown();
            }
        } else if (v.getId() == R.id.btn_more){
            if (mPlayerStateListener != null){
                mPlayerStateListener.onBtnMore();
            }
        }else if (v.getId() == R.id.btn_share){
            shareUrl();
        }else if (v.getId() == R.id.fullscreen){
            showFull();
        }else if (v.getId() == R.id.surface_container && mCurrentState == CURRENT_STATE_ERROR) {
            if (mVideoAllCallBack != null) {
                Debuger.printfLog("onClickStartError");
                mVideoAllCallBack.onClickStartError(mOriginUrl, mTitle, this);
            }
            mPlayerStateListener.onReload();
        }else {
            super.onClick(v);
        }

    }

    @Override
    protected void clickStartIcon() {
        if (mCurrentState == CURRENT_STATE_ERROR){
            if (mPlayerStateListener != null){
                mPlayerStateListener.onReload();
            }
            return;
        }
        super.clickStartIcon();
    }



    private void showFull() {
        if (orientationUtils.getIsLand() != 1) {
            //直接横屏
            orientationUtils.resolveByClick();
        }
        //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
        startWindowFullscreen(mContext, true, true);
    }

    //禁用亮度调整和音量调整
    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        mChangeVolume = false;
        mBrightness = false;
    }





    @Override
    public void releasePlayer() {
        release();
    }

    @Override
    public void load(String shareUrl,String title,String thumb) {
        ImgLoader.display(mContext,thumb, (ImageView) getThumbImageView());
        isFirstLoad = true;
        mShareUrl = shareUrl;
        mTitle = title;
    }

    @Override
    public void playUrl(String url,long startTime){
        if (startTime!=0){
            setSeekOnStart(startTime);
        }
        if (mHeader!=null&&!mHeader.isEmpty()){
            setHeader(mHeader);
        }
        setUp(url,false,mTitle);
        startPlayLogic();
    }

    @Override
    public void setPlayerStateListener(PlayerStateListener listener) {
        mPlayerStateListener = listener;
    }

    @Override
    public void setViewState(int state) {
        if (state == mViewState){
            return;
        }
        mViewState = state;
        switch (state){
            case VIEW_STATE_DOWN:
                setViewShowState(mThumbImageViewLayout, VISIBLE);
                break;
            case VIEW_STATE_SMILL:
                hideAllWidget();
                resolveUIState(mCurrentState);
                break;
            case VIEW_STATE_NOM:
                resolveUIState(mCurrentState);
                break;
        }
    }

    @Override
    public void pause() {
        if (getCurrentState() != CURRENT_STATE_PLAYING){
            return;
        }
        onVideoPause();
    }

    @Override
    public void resume() {
        if (getCurrentState() == CURRENT_STATE_PAUSE){
            onVideoResume();
            return;
        }
        if (getCurrentState() == CURRENT_STATE_AUTO_COMPLETE){
            startPlayLogic();
        }
    }

    private void shareUrl() {
        if (TextUtils.isEmpty(mShareUrl)){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,mShareUrl);
        mContext.startActivity(Intent.createChooser(intent,mContext.getResources().getString(R.string.share)));
    }

    @Override
    protected void touchDoubleUp() {
//        super.touchDoubleUp();
    }

    public void setShareUrl(String shareUrl) {
        mShareUrl = shareUrl;
    }

    @Override
    public void onStartPrepared(String url, Object... objects) {

    }

    @Override
    public void onPrepared(String url, Object... objects) {
        if (orientationUtils == null) {
            throw new NullPointerException("initVideo() or initVideoBuilderMode() first");
        }
        //开始播放了才能旋转和全屏
        orientationUtils.setEnable(!isAutoFullWithSize());
        GSYVideoManager.instance().getVideoHeight();
        isPlay = true;
        if (mPlayerStateListener!=null&& isFirstLoad){
            mPlayerStateListener.onVideoFirstPrepared(GSYVideoManager.instance().getVideoWidth(),
                    GSYVideoManager.instance().getVideoHeight());
            isFirstLoad = false;
        }
    }

    @Override
    protected int getProgressDialogLayoutId() {
        return R.layout.view_video_progress_dialog;
    }

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(mContext).inflate(getProgressDialogLayoutId(), null);
            if (localView.findViewById(getProgressDialogCurrentDurationTextId()) instanceof TextView) {
                mDialogSeekTime = ((TextView) localView.findViewById(getProgressDialogCurrentDurationTextId()));
            }
            if (localView.findViewById(getProgressDialogAllDurationTextId()) instanceof TextView) {
                mDialogTotalTime = ((TextView) localView.findViewById(getProgressDialogAllDurationTextId()));
            }
            mProgressDialog = new Dialog(getActivityContext(), R.style.dialog2);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(getWidth(), getHeight());
            if (mDialogProgressNormalColor != -11 && mDialogTotalTime != null) {
                mDialogTotalTime.setTextColor(mDialogProgressNormalColor);
            }
            if (mDialogProgressHighLightColor != -11 && mDialogSeekTime != null) {
                mDialogSeekTime.setTextColor(mDialogProgressHighLightColor);
            }
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.CENTER;
            localLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            localLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            int location[] = new int[2];
//            getLocationOnScreen(location);
//            localLayoutParams.x = location[0];
//            localLayoutParams.y = location[1];
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        if (mDialogSeekTime != null) {
            mDialogSeekTime.setText(seekTime);
        }
        if (mDialogTotalTime != null) {
            mDialogTotalTime.setText(" / " + totalTime);
        }

    }

    @Override
    protected void showWifiDialog() {
        if (!NetworkUtils.isAvailable(mContext)) {
            //Toast.makeText(mContext, getResources().getString(R.string.no_net), Toast.LENGTH_LONG).show();
            startPlayLogic();
            return;
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_video_alertdialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.AlertDialog_alpha);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setContentView(view);
//        builder.setView(R.layout.view_video_alertdialog);
        ((TextView)view.findViewById(R.id.message)).setText(getResources().getString(R.string.tips_not_wifi));
        TextView cancel = ((TextView)view.findViewById(R.id.cancel_button));
        TextView confirm = ((TextView)view.findViewById(R.id.confirm_button));
        cancel.setText(getResources().getString(R.string.tips_not_wifi_cancel));
        confirm.setText(getResources().getString(R.string.tips_not_wifi_confirm));
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startButtonLogic();
            }
        });
    }

    //添加过渡动画
    @Override
    protected void setViewShowState(View view, int visibility) {
        if (view != null&&view.getVisibility() != visibility) {
            Animation animation;
            if (visibility == VISIBLE){
                mFadeInAdd.setAnimationListener(progressThumbShowAnimListener);
                animation = (view == mBottomContainer) ? mFadeInAdd : mFadeIn;
            }else {
                mFadeOutAdd.setAnimationListener(progressThumbHideAnimListener);
                animation = (view == mBottomContainer) ? mFadeOutAdd : mFadeOut;
            }
            view.setVisibility(visibility);
            view.startAnimation(animation);
        }
    }

    private Animation.AnimationListener progressThumbHideAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
           mScaleHide.start();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener progressThumbShowAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mProgressBar.setEnabled(true);
            mScaleShow.start();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    protected void onClickUiToggle() {
        if (mViewState == VIEW_STATE_NOM){
            super.onClickUiToggle();
        }else {
            mPlayerStateListener.onClickViewToNom();
        }
    }

    @Override
    protected void changeUiToNormal() {
        if (mViewState == VIEW_STATE_NOM){
            super.changeUiToNormal();
        }else {
            setViewShowState(mLoadingProgressBar, INVISIBLE);
            if (mViewState == VIEW_STATE_SMILL){
                setViewShowState(mThumbImageViewLayout, VISIBLE);
            }
            setViewShowState(mStartButton, INVISIBLE);
            updateStartImage();
        }
    }

    @Override
    protected void changeUiToPreparingShow() {
        if (mViewState == VIEW_STATE_NOM){
            super.changeUiToPreparingShow();
        }else {
            setViewShowState(mLoadingProgressBar, VISIBLE);
            if (mViewState == VIEW_STATE_SMILL){
                setViewShowState(mThumbImageViewLayout, INVISIBLE);
            }
            setViewShowState(mStartButton, INVISIBLE);
        }
    }

    @Override
    protected void changeUiToPrepareingClear() {
        Debuger.printfLog("changeUiToPrepareingClear");

        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, VISIBLE);
        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
    }

    @Override
    protected void changeUiToPlayingShow() {
        if (mViewState == VIEW_STATE_NOM){
            super.changeUiToPlayingShow();
        }else {
            setViewShowState(mLoadingProgressBar, INVISIBLE);
            if (mViewState == VIEW_STATE_SMILL){
                setViewShowState(mThumbImageViewLayout, INVISIBLE);
            }
            setViewShowState(mStartButton, INVISIBLE);
            updateStartImage();
        }
    }

    @Override
    protected void changeUiToPauseShow() {
        if (mViewState == VIEW_STATE_NOM){
            super.changeUiToPauseShow();
        }else {
            setViewShowState(mLoadingProgressBar, INVISIBLE);
            if (mViewState == VIEW_STATE_SMILL){
                setViewShowState(mThumbImageViewLayout, INVISIBLE);
            }
            setViewShowState(mStartButton, INVISIBLE);
            updateStartImage();
            updatePauseCover();
        }
    }



    @Override
    protected void changeUiToError() {
        if (mViewState == VIEW_STATE_NOM){
            super.changeUiToError();
        }else {
            setViewShowState(mLoadingProgressBar, INVISIBLE);
            if (mViewState == VIEW_STATE_SMILL){
                setViewShowState(mThumbImageViewLayout, INVISIBLE);
            }
            setViewShowState(mStartButton, VISIBLE);
            updateStartImage();
        }
    }

    @Override
    protected void changeUiToCompleteShow() {
        if (mViewState == VIEW_STATE_NOM){
            super.changeUiToCompleteShow();
        }else {
            setViewShowState(mLoadingProgressBar, INVISIBLE);
            if (mViewState == VIEW_STATE_SMILL){
                setViewShowState(mThumbImageViewLayout, VISIBLE);
            }
            setViewShowState(mStartButton, INVISIBLE);
            updateStartImage();
        }
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        if (mViewState == VIEW_STATE_NOM){
            super.changeUiToPlayingBufferingShow();
        }else {
            setViewShowState(mLoadingProgressBar, VISIBLE);
            if (mViewState == VIEW_STATE_SMILL){
                setViewShowState(mThumbImageViewLayout, INVISIBLE);
            }
            setViewShowState(mStartButton, INVISIBLE);
            updateStartImage();
        }
    }

    private void clearShowTime() {
        mProgressBar.setProgress(0);
        mProgressBar.setEnabled(false);
        mCurrentTimeTextView.setText(mContext.getString(R.string.zero_time));
    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {

    }

    @Override
    public void onClickStartError(String url, Object... objects) {

    }

    @Override
    public void onClickStop(String url, Object... objects) {

    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickResume(String url, Object... objects) {

    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {

    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
        clearShowTime();
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {

    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {

    }

    @Override
    public void onPlayError(String url, Object... objects) {

    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
        mErrorTextView.setText(String.format(mContext.getString(R.string.play_error),what));
    }

    @Override
    public void onClickStartThumb(String url, Object... objects) {

    }

    @Override
    public void onClickBlank(String url, Object... objects) {

    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {

    }


    @Override
    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
        if (mPlayerStateListener!=null){
            mPlayerStateListener.onProgressUpdate(progress,secProgress);
        }
    }

    //在seek到起始位置时重置progressbar进度
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            if (isIfCurrentIsFullscreen()) {
                Debuger.printfLog("onClickSeekbarFullscreen");
                mVideoAllCallBack.onClickSeekbarFullscreen(mOriginUrl, mTitle, this);
            } else {
                Debuger.printfLog("onClickSeekbar");
                mVideoAllCallBack.onClickSeekbar(mOriginUrl, mTitle, this);
            }
        }
        if (getGSYVideoManager() != null && mHadPlay) {
            try {
                int time = seekBar.getProgress() * getDuration() / 1000;
                getGSYVideoManager().seekTo(time);
            } catch (Exception e) {
                Debuger.printfWarning(e.toString());
            }
        }
        mHadSeekTouch = false;
        if (seekBar.getProgress() == 0){
            setTextAndProgress(0);
        }
    }

    //因为效果原因需要将Seekbar独立出去
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setSeekbar(SeekBar seekbar) {
        mProgressBar = seekbar;
        mProgressBar.setOnSeekBarChangeListener(this);
        mProgressBar.setOnTouchListener(this);
        setSeekBarDelegate();
    }

    @Override
    public int getViewState() {
        return mViewState;
    }

    @Override
    public float getPlaySpeed() {
        return getSpeed();
    }

    @Override
    public void setPlaySpeed(float speed) {
        setSpeed(speed);
    }

    @Override
    public long getPlayingPos() {
        return getCurrentPositionWhenPlaying();
    }


    //重写progressbar的Max为1000，使进度增加更平滑
    @Override
    protected void setTextAndProgress(int secProgress) {
        int position = getCurrentPositionWhenPlaying();
        int duration = getDuration();
        int progress = position * 1000 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    //同setTextAndProgress()
    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (mGSYVideoProgressListener != null && mCurrentState == CURRENT_STATE_PLAYING) {
            mGSYVideoProgressListener.onProgress(progress, secProgress, currentTime, totalTime);
        }

        if (mProgressBar == null || mTotalTimeTextView == null || mCurrentTimeTextView == null) {
            return;
        }
        if(mHadSeekTouch) {
            return;
        }
        if (!mTouchingProgressBar) {
            if (progress != 0) mProgressBar.setProgress(progress);
        }
        if (getGSYVideoManager().getBufferedPercentage() > 0) {
            secProgress = getGSYVideoManager().getBufferedPercentage();
        }
        if (secProgress > 940) secProgress = 1000;
        setSecondaryProgress(secProgress);
        mTotalTimeTextView.setText(CommonUtil.stringForTime(totalTime));
        if (currentTime > 0)
            mCurrentTimeTextView.setText(CommonUtil.stringForTime(currentTime));

        if (mBottomProgressBar != null) {
            if (progress != 0) mBottomProgressBar.setProgress(progress);
            setSecondaryProgress(secProgress);
        }
    }

    public void setHeader(Map<String, String> header) {
        mHeader = header;
    }

    @Override
    public boolean isFullScreen() {
        return isIfCurrentIsFullscreen();
    }

    @Override
    public void exitFullScreen() {
        if (isIfCurrentIsFullscreen()){
            clearFullscreenLayout();
        }
    }

    @Override
    public void setLoading(boolean b) {
        if (b){
            setStateAndUi(CURRENT_STATE_PREPAREING);
        }else {
            setStateAndUi(CURRENT_STATE_NORMAL);
        }
    }

    @Override
    public void setFailed(String text) {
        setStateAndUi(CURRENT_STATE_ERROR);
        mErrorTextView.setText(text);
    }

    @Override
    public void setscaleType(int scale) {
        if (scale == IVideoPlayer.SCALE_DEFAULT){
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        }else if (scale == IVideoPlayer.SACLE_CROP){
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        }
    }
}
