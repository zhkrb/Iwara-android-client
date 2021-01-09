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
 * Create by zhkrb on 2019/10/17 22:58
 */

package com.zhkrb.custom;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.zhkrb.iwara.R;

public class FoldTextView extends AppCompatTextView {

    private boolean isFold = true;
    private int mFoldLine;
    private boolean isRotateDrawable;

    private int mFoldHeight;
    private int mUnFoldHeight;

    private int mAnimDuration;
    private ObjectAnimator mAnimator;
    private ValueAnimator mDrawableAnimator;


    public FoldTextView(Context context) {
        this(context,null);
    }

    public FoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FoldTextView);
        mFoldLine = ta.getInt(R.styleable.FoldTextView_foldLine,0);
        isRotateDrawable = ta.getBoolean(R.styleable.FoldTextView_isRotateDrawable,false);
        mAnimDuration = ta.getInt(R.styleable.FoldTextView_mAnimDuration,333);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mUnFoldHeight == 0){
            mUnFoldHeight = getLayout().getLineTop(getLineCount()) + getCompoundPaddingTop() + getCompoundPaddingBottom();
            if (getLineCount() == 1){
                mUnFoldHeight = getMeasuredHeight();
            }
            setMaxLines(mFoldLine);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mFoldHeight = getMeasuredHeight();
            if (mUnFoldHeight == mFoldHeight) {
                setMaxHeight(mUnFoldHeight);
            }else{
                setMaxLines(Integer.MAX_VALUE);
                setMaxHeight(mFoldHeight);
            }
        }
    }

    public void fold(boolean b){
        if (getLineCount() == 1){
            return;
        }
        if (isFold){
            mAnimator = ObjectAnimator.ofInt(this,"MaxHeight",mFoldHeight);
        }else {
            mAnimator = ObjectAnimator.ofInt(this,"MaxHeight",mUnFoldHeight);
        }
        mAnimator.setDuration(mAnimDuration);
        mAnimator.setInterpolator(new FastOutSlowInInterpolator());
        mAnimator.setAutoCancel(true);
//        mAnimator.addUpdateListener(animation -> {
////
////        });
        mAnimator.start();
    }

    public void setFold(boolean fold){
        if (isFold == fold){
            return;
        }
        isFold = fold;
        if((mFoldHeight != 0 && mUnFoldHeight != 0) || (!TextUtils.isEmpty(getText()))) {
            fold(fold);
        }

        if (isRotateDrawable){
            int startValue = isFold ? 10000 : 0;
            if (mDrawableAnimator!=null&&mDrawableAnimator.isRunning()){
                startValue = (int) mDrawableAnimator.getAnimatedValue();
                mDrawableAnimator.cancel();
            }
            mDrawableAnimator = ValueAnimator.ofInt(startValue,isFold ? 0 : 10000);
            mDrawableAnimator.setDuration(mAnimDuration);
            mDrawableAnimator.setInterpolator(new FastOutSlowInInterpolator());
            mDrawableAnimator.addUpdateListener(animation -> {
                Drawable[] drawables = getCompoundDrawables();
                for (Drawable drawable:drawables){
                    if (drawable instanceof RotateDrawable){
                        drawable.setLevel((Integer) animation.getAnimatedValue());
                    }
                }
            });
            mDrawableAnimator.start();
        }
    }

    public boolean isFold() {
        return isFold;
    }

    public void setFoldLine(int foldLine) {
        mFoldLine = foldLine;
    }

    public int getFoldLine() {
        return mFoldLine;
    }

    public void setAnimDuration(int animDuration) {
        mAnimDuration = animDuration;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator!=null && mAnimator.isRunning()){
            mAnimator.cancel();
            mAnimator.removeAllListeners();
        }
        if (mDrawableAnimator!=null && mDrawableAnimator.isRunning()){
            mDrawableAnimator.cancel();
            mDrawableAnimator.removeAllUpdateListeners();
        }
        mAnimator = null;
        mDrawableAnimator = null;
    }
}
