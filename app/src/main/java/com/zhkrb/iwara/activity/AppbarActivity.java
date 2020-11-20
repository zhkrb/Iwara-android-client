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

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;

import com.zhkrb.iwara.R;
import com.zhkrb.base.AbsActivity;
import com.zhkrb.base.FragmentFrame;
import com.zhkrb.base.TransitionHelper;
import com.zhkrb.custom.MaterialAppBarLayout;
import com.zhkrb.iwara.fragment.BarBaseFragment;

@SuppressLint("Registered")
public abstract class AppbarActivity extends AbsActivity {

    private static final String BAR_EXPAND = "bar_expand";
    private static final String BAR_FIXTOP = "bar_fixtop";
    protected MotionLayout mMainMotionLayout;
    protected MaterialAppBarLayout mBarLayout;
    private boolean isExpand = true;
    private boolean isFixTop = false;
    private volatile boolean needReloadLayoutDescription;


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void main(Bundle save) {
        super.main(save);
        mMainMotionLayout = findViewById(R.id.motion_layout);
        mBarLayout = findViewById(R.id.appBarLayout);
        mMainMotionLayout.setTransitionListener(mTransitionListener);
        if (save != null) {
            onRestore(save);
        }
    }

    private synchronized void initBar() {
        if (isFixTop){
            mMainMotionLayout.loadLayoutDescription(R.xml.main_scene_fixtop);
        }else {
            mMainMotionLayout.loadLayoutDescription(R.xml.main_scene);
        }
        mBarLayout.setProgress(0);
        needReloadLayoutDescription = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startFragment(FragmentFrame frame) {
        super.startFragment(frame);
    }

    @Override
    public void finish(String tag, TransitionHelper transitionHelper) {
        super.finish(tag, transitionHelper);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BAR_FIXTOP, isFixTop);
        outState.putBoolean(BAR_EXPAND, isExpand);
    }


    private void onRestore(Bundle save) {
        isExpand = save.getBoolean(BAR_EXPAND, true);
        isFixTop = save.getBoolean(BAR_FIXTOP, false);
        initBar();
        mMainMotionLayout.setProgress(isExpand ? 0 : 1);
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
            if (i == R.id.end) {
                mBarLayout.setBarClose(true);
                isExpand = false;
            } else if (i == R.id.start) {
                mBarLayout.setBarClose(false);
                isExpand = true;
                if (needReloadLayoutDescription){
                    initBar();
                }
            }
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

        }
    };

    @Override
    protected void onTransactionFragment(Fragment fragment) {
        super.onTransactionFragment(fragment);
        if (!(fragment instanceof BarBaseFragment)) {
            throw new RuntimeException("must extends BarBaseFragment: " + fragment.getClass());
        }
        boolean needFixTop = ((BarBaseFragment) fragment).getNeedFixTop();
        isFixTop = needFixTop;
        if (mMainMotionLayout.getCurrentState() == R.id.start){
            initBar();
        }else {
            needReloadLayoutDescription = true;
            mMainMotionLayout.transitionToStart();
        }
        mBarLayout.addTitleBar(((BarBaseFragment) fragment).getTopAppbar(mContext), needFixTop);
    }

    public void expandAll() {
        if (isExpand) {
            return;
        }
        mMainMotionLayout.transitionToStart();
    }

}