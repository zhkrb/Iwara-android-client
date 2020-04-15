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
 * Create by zhkrb on 2020/3/3 0:13
 */

package com.zhkrb.iwara.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.motion.widget.TransitionBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.base.FragmentFrame;
import com.zhkrb.iwara.custom.MaterialAppBarLayout;
import com.zhkrb.iwara.custom.refreshView.loading.LoadingDrawable;
import com.zhkrb.iwara.custom.refreshView.loading.LoadingView;
import com.zhkrb.iwara.utils.DpUtil;

public class TestFragment extends BarBaseFragment {

//    private MaterialAppBarLayout mView;
    private Button mButton;
    private MotionLayout mConstraintLayout;

    @Override
    public View getTopAppbar(Context context) {
        return null;
    }

    @Override
    public boolean getNeedFixTop() {
        return false;
    }

    @Override
    protected void main(Bundle savedInstanceState) {
//        mView = mRootView.findViewById(R.id.view);
        mButton = mRootView.findViewById(R.id.button);
        mConstraintLayout = mRootView.findViewById(R.id.motion_layout);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFrame frame = new FragmentFrame(TestFragment.class);
                startFragment(frame);
            }
        });
        mRootView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mView.setBarClose(false);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void onNewArguments(Bundle args) {

    }
}
