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
 * Create by zhkrb on 2020/5/4 15:15
 */

package com.zhkrb.dragvideo.motionBase;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.contentViewBase.VideoContentView;
import com.zhkrb.dragvideo.utils.DragSettleUtil;

public abstract class MotionBaseView extends FrameLayout {

    //view
    protected View mRootView;
    protected FrameLayout mFirstView;
    protected VideoContentView mDescView;
    private SeekBar mProgressSeekBar;
    private ViewGroup mContentView;

    //value
    private int mTouchSlop;

    //class
    private DragSettleUtil mSettleUtil;

    private MotionBaseListener mMotionListener;

    public MotionBaseView(@NonNull Context context) {
        this(context,null);
    }

    public MotionBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MotionBaseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.view_motion_base,this,true);
        mFirstView = mRootView.findViewById(R.id.view_header);
        mDescView = mRootView.findViewById(R.id.view_desc);
        mProgressSeekBar = mRootView.findViewById(R.id.progress);
        setClipChildren(false);
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
        mSettleUtil = new DragSettleUtil(vc,this);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_to_top_enter);
        animation.setAnimationListener(loadAnimListener);
        setAnimation(animation);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void show(){
        if (mContentView == null){
            throw new RuntimeException("must set content view");
        }
        removeAllChildView(mContentView);
        mContentView.addView(this);
        requestLayout();
        post(showCompleteRunnable);
    }

    private Runnable showCompleteRunnable = () -> {

    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    public void release(){

    }

    private void removeAllChildView(ViewGroup contentView) {
        for (int i = 0;i < contentView.getChildCount();i++){
            View view = contentView.getChildAt(i);
            if (view instanceof MotionBaseView){
                ((MotionBaseView) view).release();
            }
            contentView.removeView(view);
        }
    }


    private Animation.AnimationListener loadAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mMotionListener != null){
                mMotionListener.onFullScreen();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void setMotionListener(MotionBaseListener motionListener) {
        mMotionListener = motionListener;
    }

    public void setContentView(ViewGroup contentView) {
        mContentView = contentView;
    }
}
