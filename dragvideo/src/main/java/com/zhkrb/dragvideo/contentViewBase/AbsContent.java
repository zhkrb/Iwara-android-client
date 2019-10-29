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

    protected Context mContext;
    private View mLoadingView;

    protected VideoContentView mParent;
    protected Bundle mArgs;
    protected View mRootView;
    protected boolean isAnim = false;

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
        if (mLoadingView == null){
            mLoadingView = LayoutInflater.from(mContext).inflate(R.layout.view_content_loading,this,false);
        }
        addView(mLoadingView);
    }

    protected void hideLoading(){
        if (mLoadingView != null){
            removeView(mLoadingView);
            mLoadingView = null;
        }
    }

    protected abstract int getContentLayoutId();

    public void release() {
        mContext = null;
        mLoadingView = null;
        mParent = null;
        mArgs = null;
        mRootView = null;
    }

    protected void reload(Bundle arg) {
        if (arg != null){
            mArgs = arg;
        }
        removeAllViews();
        load();
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

        ((VideoContentView)getParent()).setNeedReload(false);
        showLoading();
        prepareLoad();
    }

    protected abstract void prepareLoad();

    public void loadComplete(){
        hideLoading();
        loadRootView();
    }

    public void loadFail(){
        ((VideoContentView)getParent()).setNeedReload(true);
        hideLoading();
        if (getChildCount() > 0){
            removeAllViews();
        }
        LayoutInflater.from(mContext).inflate(R.layout.view_content_loadfail,this,true);
        findViewById(R.id.btn_reload).setOnClickListener(v -> {
            ((VideoContentView)getParent()).getReloadListener().needReload();
        });

    }

    public void loadRootView(){
        mRootView = LayoutInflater.from(mContext).inflate(getContentLayoutId(),this,true);
        main();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isAnim){
            return;
        }
        super.onLayout(changed, l, t, r, b);
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }
}
