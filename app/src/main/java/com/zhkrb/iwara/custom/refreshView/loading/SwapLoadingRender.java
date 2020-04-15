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
 * Create by zhkrb on 2020/3/18 23:05
 */

package com.zhkrb.iwara.custom.refreshView.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.zhkrb.iwara.utils.DpUtil;

import java.util.ArrayList;
import java.util.List;

public class SwapLoadingRender extends LoadingRender {

    private static final Interpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_BALLCOUNT = 5;

    private static final float DEFAULT_STROKE_WIDTH = 2f;
    private static final float DEFAULT_BALL_RADIUS = 3f;
    private static final float DEFAULT_BALL_PADDING = 5f;
    private static final float DEFAULT_WIDTH = DEFAULT_BALL_RADIUS * 2 * 5 + DEFAULT_BALL_PADDING * 4;
    private static final float DEFAULT_HEIGHT = DEFAULT_BALL_RADIUS * 4 + DEFAULT_BALL_PADDING;

    private static final long ANIMATION_DURATION = 500;
    private static final long END_ANIMATION_DURATION = 200;


    private List<Float> mEndAnimValues = new ArrayList<>();
    private ValueAnimator mEndAnimator = null;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ArrayList<Integer> mColorList = new ArrayList<Integer>();

    private int mColor;
    private int mBallCount;
    private int mBallIndex;
    private int mColorIndex;
    private boolean mColorCountDown = false;
    private boolean mBallCountDown = false;
    private boolean mBallReverse = false;

    private float mBallRadius;
    private float mBallPaintWidth;
    private float mBallPadding;
    private float mBallCenterY;
    private float mBallComputeX;
    private float mBallComputeY;
    private float mBallOffsetX;
    private LoadingDrawable.EndAnimListener mEndAnimListener;


    public SwapLoadingRender() {
        init();
        setupPaint();
    }



    private void init(){
        adjustParams();
        mBallRadius = DpUtil.dp2px(DEFAULT_BALL_RADIUS);
        mBallPadding = DpUtil.dp2px(DEFAULT_BALL_PADDING);
        mColor = DEFAULT_COLOR;
        mBallPaintWidth = DEFAULT_STROKE_WIDTH;
        mBallCount = DEFAULT_BALLCOUNT;
        mBallIndex = 0;

        mDuration = ANIMATION_DURATION;
        mRenderAnimator.setDuration(mDuration);
        mRenderAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startEndAnimator();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mBallReverse = !mBallReverse;
                if (mBallCountDown){
                    mBallIndex -= 1;
                }else {
                    mBallIndex += 1;
                }
                if (mBallIndex == 0 || mBallIndex == mBallCount -1){
                    mBallCountDown = !mBallCountDown;
                    onDirectionChange();
                }
            }
        });
    }

    private void onDirectionChange() {
        if (mColorList.size() == 0){
            return;
        }
        if (mColorCountDown){
            mColorIndex -= 1;
        }else {
            mColorIndex += 1;
        }
        mPaint.setColor(mColorList.get(mColorIndex));
        if (mColorIndex == 0 || mColorIndex == mColorList.size() -1){
            mColorCountDown = !mColorCountDown;
        }
    }

    @Override
    public void setColorSchemeColors(int... color) {
        for (int colors :color){
            mColorList.add(colors);
        }
        mPaint.setColor(mColorList.get(0));
    }

    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        adjustParams();
    }

    private void adjustParams(){
        mWidth = mBounds.right == 0 ? DpUtil.dp2px(DEFAULT_HEIGHT) : mBounds.right;
        mHeight = mBounds.bottom == 0 ? DpUtil.dp2px(DEFAULT_WIDTH) : mBounds.bottom;
        mBallCenterY = mHeight / 2;
        mBallOffsetX = (mWidth - DpUtil.dp2px(DEFAULT_WIDTH)) / 2;
    }

    private void setupPaint() {
        mPaint.setStrokeWidth(mBallPaintWidth);
        mPaint.setColor(mColor);
    }

    @Override
    public void computeRender(float renderProgress) {
        mBallComputeX = ACCELERATE_DECELERATE_INTERPOLATOR.getInterpolation(renderProgress) * (mBallRadius * 2 + mBallPadding);
        mBallComputeY = (float) Math.sqrt(Math.pow((mBallRadius * 2 + mBallPadding)/2,2) - Math.pow(mBallComputeX - (mBallRadius * 2 + mBallPadding)/2,2));
    }

    @Override
    protected void draw(Canvas canvas) {
        int count = canvas.save();
        if (!isEndAnim){
            for (int i = 0;i < mBallCount; i++){
                if (i == mBallIndex){
                    mPaint.setStyle(Paint.Style.STROKE);
                    float x = mBallCountDown ? mBallOffsetX + mBallRadius + i * mBallPadding + 2 * i * mBallRadius - mBallComputeX :
                            mBallOffsetX + mBallRadius + i * mBallPadding + 2 * i * mBallRadius + mBallComputeX;
                    float y = mBallReverse ? mBallCenterY - mBallComputeY : mBallCenterY + mBallComputeY;
                    canvas.drawCircle(x,y, mBallRadius,mPaint);
                }else if ((!mBallCountDown && i == mBallIndex + 1) || (mBallCountDown && i == mBallIndex -1)){
                    mPaint.setStyle(Paint.Style.FILL);
                    float x = mBallCountDown ? mBallOffsetX + mBallRadius + i * mBallPadding + 2 * i * mBallRadius + mBallComputeX :
                            mBallOffsetX + mBallRadius + i * mBallPadding + 2 * i * mBallRadius - mBallComputeX;
                    float y = mBallReverse ? mBallCenterY + mBallComputeY : mBallCenterY - mBallComputeY;
                    canvas.drawCircle(x,y,mBallRadius,mPaint);
                } else {
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(mBallOffsetX + mBallRadius + i * mBallPadding + 2 * i * mBallRadius,
                            mBallCenterY,mBallRadius,mPaint);
                }
            }
        }else {
            for (int i = 0;i < mBallCount; i++){
                mPaint.setStyle(i == mBallIndex ? Paint.Style.STROKE : Paint.Style.FILL);
                canvas.drawCircle(mBallOffsetX + mBallRadius + i * mBallPadding + 2 * i * mBallRadius,
                        mBallCenterY + (mHeight) * mEndAnimValues.get(i) ,mBallRadius,mPaint);
            }
        }
        canvas.restoreToCount(count);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public void reset() {
        if (mEndAnimValues.size() > 0){
            mEndAnimValues.clear();
        }
        for (int i =0;i<mBallCount;i++){
            mEndAnimValues.add(0f);
        }
        if (isEndAnim){
            mBallIndex = 0;
            mBallReverse = false;
            mBallCountDown = false;
        }
    }

    @Override
    public void endAnim(LoadingDrawable.EndAnimListener endAnimListener) {
        mEndAnimListener = endAnimListener;
        mRenderAnimator.end();
    }

    @Override
    protected void stop() {
        super.stop();
        if (mEndAnimator != null){
            mEndAnimator.cancel();
            mEndAnimator = null;
        }
    }

    private void startEndAnimator() {
        isEndAnim = true;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(END_ANIMATION_DURATION);
        valueAnimator.setInterpolator(new AccelerateInterpolator());

        for (int i = 0;i < mBallCount;i++){
            ValueAnimator valueAnimator1 = valueAnimator.clone();
            int finalI = i;
            valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mEndAnimValues.set(finalI, (Float) animation.getAnimatedValue());
                    invalidateSelf();
                }
            });
            if (i == mBallCount - 1){
                mEndAnimator = valueAnimator1;
                valueAnimator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mEndAnimListener != null){
                            mEndAnimListener.isEnd();
                        }
                    }
                });
            }
            valueAnimator1.setStartDelay(i * 50);
            valueAnimator1.start();
        }
    }
}
