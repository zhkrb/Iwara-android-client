///*
// * Copyright zhkrb
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// * Create by zhkrb on 2019/9/28 15:05
// */
//
//package com.zhkrb.dragvideo.contentView;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ProgressBar;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public  class AbsContent extends FrameLayout implements IContent {
//
//    private Context mContext;
//    private ProgressBar mLoadingProgress;
//
//    protected List<String> mFragmentStack;
//
//    public AbsContent(Context context) {
//        this(context,null);
//    }
//
//    public AbsContent(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs,0);
//    }
//
//    public AbsContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mContext = context;
//    }
//
//    protected  void main(){
//
//    }
//
//    @Override
//    public void init(Bundle arg) {
////        View view = LayoutInflater.from(mContext).inflate(getContentLayoutId(),this,false);
////        addView(view);
////        showLoading();
//
//    }
//
//    protected void loadRootFragment(FragmentFrame frame){
//        loadRootFragment(frame,AbsFragment.LAUNCH_MODE_STANDARD);
//    }
//
//
//
//    private void showLoading() {
//        if (mLoadingProgress == null){
//            mLoadingProgress = new ProgressBar(mContext);
//        }
//        mLoadingProgress.setBackgroundResource(R.color.textColor);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        mLoadingProgress.setLayoutParams(layoutParams);
//        addView(mLoadingProgress);
//    }
//
//    private void hideLoading(){
//        if (mLoadingProgress != null && mLoadingProgress.getParent() != null){
//            ((ViewGroup)mLoadingProgress.getParent()).removeView(mLoadingProgress);
//            mLoadingProgress = null;
//        }
//    }
//
//    protected  int getContentLayoutId(){
//        return 0;
//    }
//
//    @Override
//    public void release() {
//        mContext = null;
//    }
//
//    @Override
//    public void reload(Bundle arg) {
//
//    }
//
//    @Override
//    public void addToParent(ViewGroup parent) {
//        if (getParent() == null){
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            setLayoutParams(params);
//        }else {
//            ((ViewGroup)getParent()).removeView(this);
//        }
//        parent.addView(this);
//        requestLayout();
//    }
//
//    public void setArgs(Bundle args) {
//    }
//
//    public void setContext(Context context) {
//    }
//}
