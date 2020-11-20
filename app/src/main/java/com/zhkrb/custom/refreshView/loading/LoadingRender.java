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
 * Create by zhkrb on 2020/3/18 21:25
 */

package com.zhkrb.custom.refreshView.loading;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import com.zhkrb.utils.DpUtil;

public abstract class LoadingRender {
    private final int DEFAULT_SIZE = 55;
    private final int DEFAULT_DURATION = 1000;

    private Drawable.Callback mCallback;
    protected ValueAnimator mRenderAnimator;
    protected Rect mBounds = new Rect();
    protected long mDuration;
    protected float mWidth;
    protected float mHeight;

    protected boolean isEndAnim = false;

    public LoadingRender(){
        setDefaultparams();
        setAnimator();
    }

    public abstract void computeRender(float renderProgress);
    public abstract void setAlpha(int alpha);
    public abstract void setColorFilter(ColorFilter cf);
    public abstract void reset();
    public abstract void endAnim(LoadingDrawable.EndAnimListener endAnimListener);

    protected void draw(Canvas canvas){
        draw(canvas,mBounds);
    }

    @Deprecated
    protected void draw(Canvas canvas,Rect bounds){

    }

    public void setCallback(Drawable.Callback callback) {
        mCallback = callback;
    }

    public void start() {
        reset();
        isEndAnim = false;
        mRenderAnimator.start();
    }

    protected void stop() {
        mRenderAnimator.cancel();
    }

    public boolean isRunning() {
        return mRenderAnimator.isRunning();
    }

    public void setBounds(Rect bounds) {
        mBounds.set(bounds);
    }

    public float getHieght() {
        return mHeight;
    }

    public float getWidth(){
        return mWidth;
    }

    private void setAnimator() {
        mRenderAnimator = ValueAnimator.ofFloat(0,1f);
        mRenderAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRenderAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRenderAnimator.setDuration(mDuration);
        mRenderAnimator.setInterpolator(new LinearInterpolator());
        mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);
    }

    private void setDefaultparams() {
        mHeight = DpUtil.dp2px(DEFAULT_SIZE);
        mWidth = DpUtil.dp2px(DEFAULT_SIZE);

        mDuration = DEFAULT_DURATION;
    }

    protected void invalidateSelf() {
        mCallback.invalidateDrawable(null);
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = animation -> {
        computeRender((Float) animation.getAnimatedValue());
        invalidateSelf();
    };

    public abstract void setColorSchemeColors(int... color);
}
