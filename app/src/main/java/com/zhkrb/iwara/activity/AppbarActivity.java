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

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.base.AbsActivity;

@SuppressLint("Registered")
public class AppbarActivity extends AbsActivity {

    protected MotionLayout mMainMotionLayout;

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
        mMainMotionLayout.setTransitionListener(mTransitionListener);
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

        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

        }
    };


}
