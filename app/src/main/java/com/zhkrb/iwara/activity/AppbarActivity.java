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
 * Create by zhkrb on 2019/9/19 18:23
 */

package com.zhkrb.iwara.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.base.AbsActivity;
import com.zhkrb.iwara.base.FragmentFrame;
import com.zhkrb.iwara.base.TransitionHelper;
import com.zhkrb.iwara.custom.DrawerArrowDrawable;
import com.zhkrb.iwara.custom.MaterialAppBarLayout;
import com.zhkrb.iwara.utils.L;

@SuppressLint("Registered")
public class AppbarActivity extends AbsActivity {

    protected MotionLayout mMainMotionLayout;
    protected MaterialAppBarLayout mBarLayout;
    private DrawerArrowDrawable mArrowDrawable;
    private boolean isExpand = true;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void main() {
        super.main();
        mMainMotionLayout = findViewById(R.id.motion_layout);
        mBarLayout = findViewById(R.id.appBarLayout);
        if (mMainMotionLayout != null){
            mMainMotionLayout.setTransitionListener(mTransitionListener);
        }
        if (mBarLayout != null){
            mArrowDrawable = new DrawerArrowDrawable(mContext.getResources());
            mArrowDrawable.setStrokeColor(mContext.getResources().getColor(R.color.textColor_disable));
            mBarLayout.setFirstBtnDrawable(mArrowDrawable);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mArrowDrawable = null;
    }

    @Override
    public void startFragment(FragmentFrame frame, int launchMode) {
        mArrowDrawable.setStateWithAnim(false,!isExpand);
        super.startFragment(frame, launchMode);
    }

    @Override
    protected void loadRootFragment(FragmentFrame frame, int launchMode) {
        mArrowDrawable.setStateWithAnim(true,!isExpand);
        super.loadRootFragment(frame, launchMode);
    }

    @Override
    public void finish(String tag, TransitionHelper transitionHelper) {
        if (mFragmentStack.size() == 2){
            mArrowDrawable.setStateWithAnim(true,!isExpand);
        }
        super.finish(tag, transitionHelper);
    }

    public boolean canClickBackup(){
        return mFragmentStack.size() > 2;
    }

    private MotionLayout.TransitionListener mTransitionListener = new MotionLayout.TransitionListener() {
        @Override
        public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

        }

        @Override
        public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
        }

        @Override
        public void onTransitionCompleted(MotionLayout motionLayout, int i) {
            if (i == R.id.end){
                mBarLayout.setBarCollse(true);
                isExpand = false;
            }else if (i == R.id.start){
                mBarLayout.setBarCollse(false);
                isExpand = true;
            }
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

        }
    };

    public void expandAll(){
        if (isExpand){
            return;
        }
        mMainMotionLayout.transitionToStart();
    }

    public void openSlideLayout(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null){
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

}
