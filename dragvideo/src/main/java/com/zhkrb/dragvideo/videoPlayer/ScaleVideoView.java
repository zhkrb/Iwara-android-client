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

package com.zhkrb.dragvideo.videoPlayer;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.Group;

import com.zhkrb.dragvideo.ConstViewWrapper;
import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.R.styleable;
import com.zhkrb.dragvideo.ViewWrapper;
import com.zhkrb.dragvideo.contentViewBase.ReloadListener;
import com.zhkrb.dragvideo.mainView.ScaleViewListener;
import com.zhkrb.dragvideo.videoPlayer.gsyvideoplayer.VideoPlayer;

import java.util.Map;

public class ScaleVideoView extends ConstraintLayout implements OnClickListener,PlayerStateListener{
    private Context mContext;
    private int mLayoutRes;
    private float mTargetWidth;
    private float mLayoutProgress;
    private FrameLayout mVideoContent;
    private ConstViewWrapper mVideoParentWrapper;
    private ConstViewWrapper mPlayWrapper;
    private ConstViewWrapper mCloseWrapper;
    private Group mInfoGroup;
    private ProgressBar mProgressBar;
    private ImageView btnPlay;
    private TextView mTitle;
    private TextView mUser;
    private IVideoPlayer mVideoPlayer;
    private int mVideoPlayState;
    private ScaleViewListener mScaleViewListener;
    private ReloadListener mReloadListener;
    private ImageView btnClose;
    private int mLastWidth;

    public ScaleVideoView(@NonNull Context context) {
        this(context, null);
    }

    public ScaleVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutProgress = 0.0F;
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, styleable.ScaleVideoView);
        mLayoutRes = ta.getResourceId(styleable.ScaleVideoView_view_layout, R.layout.view_scale_video);
        mTargetWidth = ta.getDimension(styleable.ScaleVideoView_target_width, dp2px(130));
        ta.recycle();
    }

    public void initView(@NonNull SeekBar seekBar) {
        if (getChildCount() == 0){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(mLayoutRes, this, true);
            setClickable(true);
            mVideoContent = view.findViewById(R.id.video_content);
            mInfoGroup = view.findViewById(R.id.info_content);
            mVideoParentWrapper = new ConstViewWrapper(mVideoContent);
            btnPlay =  view.findViewById(R.id.btn_play);
            mPlayWrapper = new ConstViewWrapper(btnPlay);
            btnClose = view.findViewById(R.id.btn_close);
            mCloseWrapper = new ConstViewWrapper(btnClose);
            mProgressBar = view.findViewById(R.id.progress);
            mTitle = view.findViewById(R.id.title);
            mUser = view.findViewById(R.id.user);
            btnClose.setOnClickListener(this);
            btnPlay.setOnClickListener(this);
        }

        if (mVideoPlayer ==null){
            mVideoPlayer = getVideoPlayer();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mVideoContent.addView((View) mVideoPlayer,layoutParams);
            mVideoPlayer.setPlayerStateListener(this);
            mVideoPlayer.setSeekbar(seekBar);
            requestLayout();
        }
    }

    public void initData(String mainUrl,String title,String thumb,String user) {
        mVideoPlayer.load(mainUrl,title,thumb);
        mTitle.setText(title);
        mUser.setText(user);
        mTitle.setSelected(true);
    }

    public void setHeader(Map<String,String> header){
        mVideoPlayer.setHeader(header);
    }

    public void playUrl(String host,String url){
        playUrl(host,url,0);
    }

    public void playUrl(String host,String url,long startTime){
        mVideoPlayer.playUrl(host,url,startTime);
    }

    protected IVideoPlayer getVideoPlayer() {
        return new VideoPlayer(mContext);
    }


    public void setProgress(float progress) {
        if (progress > 1){
            progress = 1;
        }else if (progress < 0||progress < 0.01f){
            progress = 0;
        }
        if (mLayoutProgress == progress)
            return;
        mLayoutProgress = progress;
        Log.e("prog", String.valueOf(mLayoutProgress));
        if (mLayoutProgress == 0 ||mLayoutProgress == 1){
            setLayerType(View.LAYER_TYPE_NONE, null);
        }else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        reLayoutView();
    }

    public void reLayoutView() {
        int mParentWidth = this.getWidth();
        int newWidth = mParentWidth - (int)(((float)mParentWidth - this.mTargetWidth) * this.mLayoutProgress);
        if (newWidth == mLastWidth){
            return;
        }
        mLastWidth = newWidth;
        mVideoParentWrapper.setWidth(mLastWidth);
        Log.e("mParentWidth", String.valueOf(mVideoParentWrapper.getWidth()));
        int deltaX = mParentWidth - this.mVideoParentWrapper.getWidth() < 0 ? 0 : mParentWidth - this.mVideoParentWrapper.getWidth();
        if (mLayoutProgress>0&&(mInfoGroup.getVisibility()!=VISIBLE||mProgressBar.getVisibility() != VISIBLE)){
            mInfoGroup.setVisibility(VISIBLE);
            mProgressBar.setVisibility(VISIBLE);
        }else if (mLayoutProgress==0&&(mInfoGroup.getVisibility()!=GONE||mProgressBar.getVisibility()!=GONE)){
            mInfoGroup.setVisibility(GONE);
            mProgressBar.setVisibility(GONE);
        }
        if (deltaX <= dp2px(48)) {
            mCloseWrapper.setWidth(deltaX);
        } else if (deltaX <= dp2px(96) && deltaX > dp2px(48)) {
            mPlayWrapper.setWidth(deltaX - dp2px(48));
        }else {
            mCloseWrapper.setWidth(dp2px(48));
            mPlayWrapper.setWidth(dp2px(48));
        }
        if (mLayoutProgress <0.01f){
            mVideoParentWrapper.setWidth(0);
        }
        mTitle.setAlpha(mLayoutProgress);
        mUser.setAlpha(mLayoutProgress);
        btnClose.setAlpha(mLayoutProgress);
        btnPlay.setAlpha(mLayoutProgress);
//        mInfoGroup.setAlpha(mLayoutProgress);
        mProgressBar.setAlpha(mLayoutProgress);
        mVideoContent.requestLayout();
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                mContext.getResources().getDisplayMetrics());
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_play) {
            if (mVideoPlayState == LOADING){
                return;
            }
            if (mVideoPlayState == PLAY){
                mVideoPlayer.pause();
            }else if (mVideoPlayState == PAUSE){
                mVideoPlayer.resume();
            }
        }else if (view.getId() == R.id.btn_close){
            if (mScaleViewListener != null){
                mScaleViewListener.onClickViewClose();
            }
        }
    }

    private void setIsPlayingIcon(boolean isPlaying){
        if (!isPlaying){
            btnPlay.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play_arrow_24dp,null));
        }else {
            btnPlay.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pause_24dp,null));
        }
    }


    @Override
    public void onStateChange(int state) {
        mVideoPlayState = state;
        switch (state){
            case PLAY:
                setIsPlayingIcon(true);
                break;
            case PAUSE:
                setIsPlayingIcon(false);
                break;
        }
    }

    public void setPlayerState(int state){
        if (mVideoPlayer.getViewState() == state){
            return;
        }
        mVideoPlayer.setViewState(state);
    }



    @Override
    public void onVideoFirstPrepared(int width, int height) {
        if (mVideoPlayer!=null&&((View)mVideoPlayer).getLayoutParams()!=null){
            float rate = ((float)width/(float)height);
            if (rate > 1f) {
                mTargetWidth = rate* dp2px(55);
                mVideoPlayer.setscaleType(IVideoPlayer.SCALE_DEFAULT);
            }else {
                mVideoPlayer.setscaleType(IVideoPlayer.SACLE_CROP);
            }
            if (mScaleViewListener!=null){
                mScaleViewListener.onVideoFirstPrepared(width,height);
            }
        }
    }

    @Override
    public void onBtnViewDown() {
        if (mScaleViewListener!=null){
            mScaleViewListener.onBtnViewDown();
        }
    }

    @Override
    public void onProgressUpdate(int progress, int secProgress) {
        mProgressBar.setProgress(progress);
        mProgressBar.setSecondaryProgress(secProgress);
    }

    @Override
    public void onBtnMore() {
        if (mScaleViewListener!=null){
            mScaleViewListener.onBtnMore();
        }
    }

    @Override
    public void onClickViewToNom() {
        if (mScaleViewListener!=null){
            mScaleViewListener.onClickViewToNom();
        }
    }

    @Override
    public void onReload() {
        if (mReloadListener != null){
            mReloadListener.needReload();
        }
    }

    @Override
    public void onVideoAutoComplete() {
        if (mScaleViewListener != null){
            mScaleViewListener.onVideoAutoComplete();
        }
    }

    public void setViewListener(ScaleViewListener listener){
        mScaleViewListener = listener;
    }

    public void setSeekbar(@NonNull SeekBar seekbar){
        if (mVideoPlayer != null){
            mVideoPlayer.setSeekbar(seekbar);
        }
    }

    public IVideoPlayer getVideoView() {
        return mVideoPlayer;
    }

    public float getProgress() {
        return mLayoutProgress;
    }

    public void release() {
        if (mVideoPlayer!=null){
            mVideoPlayer.releasePlayer();
        }
        mVideoContent.removeAllViews();
        mVideoPlayer = null;
    }

    public void setReloadListener(ReloadListener reloadListener) {
        mReloadListener = reloadListener;
    }
}
