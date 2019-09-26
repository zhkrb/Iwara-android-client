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

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.activity.AppbarActivity;

public class MaterialAppBarLayout extends FrameLayout implements View.OnClickListener {

    private Context mContext;

    private MotionLayout mParerntLayout;
    private MotionLayout mViewParentLayout;
    private LinearLayout mChildSecLayout;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_material_appbar,this,false);
        mParerntLayout = view.findViewById(R.id.all_parent);
        mViewParentLayout = view.findViewById(R.id.view_parent);
        mChildSecLayout = view.findViewById(R.id.second_layout);
        mFirstBtn = view.findViewById(R.id.btn_bar_first);
        ImageView secBtn = view.findViewById(R.id.btn_bar_second);
        mFirstBtn.setOnClickListener(this);
        secBtn.setOnClickListener(this);
        addView(view);
        requestLayout();
    }


    public void setProgress(int i){
        if (i == mProgress){
            return;
        }
        mProgress = i;
        mParerntLayout.setProgress(i/1000f);
        mViewParentLayout.setProgress(i/1000f);
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
}
