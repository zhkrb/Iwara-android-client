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
 * Create by zhkrb on 2019/9/28 15:05
 */

package com.zhkrb.dragvideo.contentViewBase;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;


import com.zhkrb.dragvideo.R;

public abstract class AbsContent extends NestedScrollView {

    private Context mContext;
    private ProgressBar mLoadingProgress;

    protected VideoContentView mParent;
    protected Bundle mArgs;
    protected View mRootView;

    public AbsContent(Context context) {
        this(context,null);
    }

    public AbsContent(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AbsContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    protected abstract void main();

    protected void showLoading() {
        if (mLoadingProgress == null){
            mLoadingProgress = new ProgressBar(mContext);
        }
        mLoadingProgress.setBackgroundResource(R.color.textColor);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mLoadingProgress.setLayoutParams(layoutParams);
        addView(mLoadingProgress);
    }

    protected void hideLoading(){
        if (mLoadingProgress != null && mLoadingProgress.getParent() != null){
            ((ViewGroup)mLoadingProgress.getParent()).removeView(mLoadingProgress);
            mLoadingProgress = null;
        }
    }

    protected abstract int getContentLayoutId();

    public void release() {
        mContext = null;
        mLoadingProgress = null;
        mParent = null;
        mArgs = null;
        mRootView = null;
    }

    protected void reload(Bundle arg) {
    }

    public void setArgs(Bundle args) {
        mArgs = args;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setParent(VideoContentView videoContentView) {
        mParent = videoContentView;
    }

    public void load(){
        mRootView = LayoutInflater.from(mContext).inflate(getContentLayoutId(),this,true);
        main();
    }
}
