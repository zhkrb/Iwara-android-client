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
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.activity.AppbarActivity;
import com.zhkrb.iwara.utils.DpUtil;

public class MaterialAppBarLayout extends CutCardView implements View.OnClickListener {

    private Context mContext;
    private FrameLayout mChildSecLayout;
    private ImageView mIconBtn;
    private float mProgress;
    private boolean mCanCollse = true;
    private boolean isExpand = true;
    private static final String BAR_EXPAND = "bar_expand";
    private static final String BAR_SUPER = "bar_super";


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
        mIconBtn = view.findViewById(R.id.btn_bar_second);
        mIconBtn.setOnClickListener(this);
        requestLayout();
    }

    public void restoreState(boolean expand){
        post(() -> setProgress(expand ? 0 : 1));
    }



    public void setBarClose(boolean b) {
        if (!mCanCollse && b){
            return;
        }
        if ((b && mProgress == 1f) || (!b && mProgress == 0)){
            return;
        }
        startAnim(b ? 1f : 0);
    }

    private void startAnim(float value){
        setLayerType(View.LAYER_TYPE_HARDWARE, (Paint)null);
        ObjectAnimator animator = ObjectAnimator.ofFloat(this,"progress",value);
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


    public void setProgress(float i){
        if (i == mProgress){
            return;
        }
        mProgress = i;
        update(i);
        if (i == 0){
            mIconBtn.setVisibility(GONE);
            mChildSecLayout.setVisibility(VISIBLE);
        }else if (i == 1f){
            mIconBtn.setVisibility(VISIBLE);
            mChildSecLayout.setVisibility(GONE);
        }else {
            mIconBtn.setVisibility(VISIBLE);
            mChildSecLayout.setVisibility(VISIBLE);
        }
    }

    private void update(float v) {
        if (v >1){
            v = 1;
        }else if (v < 0){
            v = 0;
        }
        setCutProgress(v);
        mIconBtn.setAlpha(v);
        mChildSecLayout.setAlpha(1 - v);
    }

    public float getProgress() {
        return mProgress;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_bar_second) {
            ((AppbarActivity) mContext).expandAll();
        }
    }

    public void addTitleBar(View view,boolean canClose){
        removeTitleBar();
        mCanCollse = canClose;
        if (view == null){
            return;
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        mChildSecLayout.addView(view);
        mChildSecLayout.requestLayout();
    }

    public void removeTitleBar(){
        if (mChildSecLayout.getChildCount() > 0){
            mChildSecLayout.removeAllViews();
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable parcelable = super.onSaveInstanceState();
        bundle.putParcelable(BAR_SUPER,parcelable);
        bundle.putBoolean(BAR_EXPAND,isExpand);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable parcelable = bundle.getParcelable(BAR_SUPER);
        isExpand = bundle.getBoolean(BAR_EXPAND);
        restoreState(isExpand);
        super.onRestoreInstanceState(parcelable);
    }

}
