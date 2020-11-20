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
 * Create by zhkrb on 2020/5/14 16:12
 */

package com.zhkrb.dragvideo.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.utils.SeekbarTouchDelegate;

@SuppressLint("AppCompatCustomView")
public class AdvSeekbar extends SeekBar {

    private SeekbarTouchDelegate mDelegate;
    private ValueAnimator mScaleShow;
    private ValueAnimator mScaleHide;
    private int topUp;
    private int bottomDown;

    public AdvSeekbar(Context context) {
        this(context,null);
    }

    public AdvSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AdvSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AdvSeekbar);
        topUp = (int) ta.getDimension(R.styleable.AdvRecyclerView_maxHeight1, 0f);
        bottomDown = (int) ta.getDimension(R.styleable.AdvRecyclerView_maxHeight1, 0f);
        ta.recycle();
        main();
    }

    private void main(){
        mScaleShow = new ValueAnimator().setDuration(333);
        mScaleShow.setFloatValues(0f,1.0f);
        mScaleShow.addUpdateListener(animation -> {
            if (getThumb()!=null){
                getThumb().setAlpha((int) (255*((float)animation.getAnimatedValue())));
            }
        });
        mScaleHide = new ValueAnimator().setDuration(200);
        mScaleHide.setFloatValues(1.0f,0.0f);
        mScaleHide.addUpdateListener(animation -> {
            if (getThumb()!=null){
                getThumb().setAlpha((int) (255*((float)animation.getAnimatedValue())));
            }
        });
        post(setTouchDelegate);
    }

    private Runnable setTouchDelegate = new Runnable() {
        @Override
        public void run() {
            Rect touchRect = new Rect();
            getHitRect(touchRect);
            touchRect.top -= topUp;
            touchRect.bottom += bottomDown;
            mDelegate = new SeekbarTouchDelegate(AdvSeekbar.this,touchRect);
            ((ViewGroup)getParent()).setOnTouchListener((v, event) -> {
                if (mDelegate != null) {
                    if (mDelegate.onTouchEvent(event)) {
                        return true;
                    }
                }
                return false;
            });
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()){
            return;
        }
        if (enabled){
            mScaleShow.start();
            mDelegate.setEnable(true);
        }else {
            mDelegate.setEnable(false);
            mScaleHide.start();
        }
        super.setEnabled(enabled);
    }
}
