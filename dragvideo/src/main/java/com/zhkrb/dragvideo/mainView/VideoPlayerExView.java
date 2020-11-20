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
 * Create by zhkrb on 2020/4/19 17:08
 */

package com.zhkrb.dragvideo.mainView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhkrb.dragvideo.R;

public class VideoPlayerExView extends ConstraintLayout {

    private View mRootView;

    public VideoPlayerExView(Context context) {
        this(context,null);
    }

    public VideoPlayerExView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoPlayerExView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.view_video_play,this,true);

    }



}
