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

package com.zhkrb.iwara.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.material.card.MaterialCardView;
import com.zhkrb.dragvideo.ViewWrapper;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.activity.AppbarActivity;
import com.zhkrb.iwara.utils.DpUtil;

public class MaterialAppBarLayout extends ConstraintLayout implements View.OnClickListener {

    private Context mContext;

    private Group mChildFirstLayout;
    private FrameLayout mChildSecLayout;
    private CutCardView mCardView;
    private ViewWrapper mCardViewWrapper;
    private ImageView mFirstBtn;

    private int mProgress;


    public MaterialAppBarLayout(Context context) {
        this(context,null);
    }

    public MaterialAppBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaterialAppBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_material_appbar,this,true);
        mChildSecLayout = view.findViewById(R.id.second_layout);
        mChildFirstLayout = view.findViewById(R.id.first_layout);
        mFirstBtn = view.findViewById(R.id.btn_bar_first);
        ImageView secBtn = view.findViewById(R.id.btn_bar_second);

        mCardView = view.findViewById(R.id.cardView);
        mCardViewWrapper = new ViewWrapper(mCardView);

        mFirstBtn.setOnClickListener(this);
        secBtn.setOnClickListener(this);
        requestLayout();

    }


    public void firstInit(){
        post(measureRunable);
    }

    public void restoreState(boolean expand){
        post(() -> setProgress(expand ? 0 : 500));
    }

    private Runnable measureRunable = () -> {
        setProgress(500);
        setBarCollse(false);
    };

    public void setBarCollse(boolean b) {
        if ((b && mProgress == 500) || (!b && mProgress == 0)){
            return;
        }
        startAnim(b ? 500 : 0);
    }

    private void startAnim(int value){
        setLayerType(View.LAYER_TYPE_HARDWARE, (Paint)null);
        ObjectAnimator animator = ObjectAnimator.ofInt(this,"progress",value);
        animator.setAutoCancel(true);
        animator.setDuration(555);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setLayerType(View.LAYER_TYPE_NONE, (Paint)null);
            }
        });
        animator.start();
    }


    public void setProgress(int i){
        if (i == mProgress){
            return;
        }
        mProgress = i;
        update(i/500f);
        if (i == 0){
            mChildFirstLayout.setVisibility(GONE);
            mChildSecLayout.setVisibility(VISIBLE);
        }else if (i == 500){
            mChildFirstLayout.setVisibility(VISIBLE);
            mChildSecLayout.setVisibility(GONE);
        }else {
            mChildFirstLayout.setVisibility(VISIBLE);
            mChildSecLayout.setVisibility(VISIBLE);
        }
    }

    private void update(float v) {
        if (v >1){
            v = 1;
        }else if (v < 0){
            v = 0;
        }
        mCardViewWrapper.setRightMargin((int) ((getWidth() - DpUtil.dp2px(100)) * v));
        mCardView.setCutProgress(v);
        mChildFirstLayout.setAlpha(v);
        mChildSecLayout.setAlpha(1 - v);
        mCardView.requestLayout();
    }

    public int getProgress() {
        return mProgress;
    }

    public void setFirstBtnDrawable(Drawable drawable){
        mFirstBtn.setImageDrawable(drawable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bar_first:
                if (((AppbarActivity)mContext).canClickBackup()){
                    ((AppbarActivity) mContext).onBackPressed();
                }else {
                    ((AppbarActivity) mContext).openSlideLayout();
                }
                break;
            case R.id.btn_bar_second:
                ((AppbarActivity)mContext).expandAll();
                break;
        }
    }

    public void addTitleBar(View view){
        removeTitleBar();
        mChildSecLayout.addView(view);
        mChildSecLayout.requestLayout();
    }

    public void removeTitleBar(){
        if (mChildSecLayout.getChildCount() > 0){
            mChildSecLayout.removeAllViews();
        }
    }

}
