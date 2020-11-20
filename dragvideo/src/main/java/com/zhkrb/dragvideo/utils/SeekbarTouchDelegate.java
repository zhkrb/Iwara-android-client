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
 * Create by zhkrb on 2020/5/9 16:00
 */

package com.zhkrb.dragvideo.utils;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;


public class SeekbarTouchDelegate {

    private SeekBar mSeekBar;
    private Rect mRect;
    private boolean mDelegateTargeted;
    private boolean mEnable = true;

    public SeekbarTouchDelegate(@NonNull SeekBar seekBar, @NonNull Rect rect) {
        mSeekBar = seekBar;
        mRect = rect;
    }

    public boolean onTouchEvent(MotionEvent event){
        if (!mSeekBar.isShown() ||!mSeekBar.isEnabled() ||!isEnable()){
            return false;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean sendToDelegate = false;

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mDelegateTargeted = mRect.contains(x,y);
                sendToDelegate = mDelegateTargeted;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                sendToDelegate = mDelegateTargeted;
                break;
            case MotionEvent.ACTION_CANCEL:
                sendToDelegate = mDelegateTargeted;
                mDelegateTargeted = false;
                break;
        }
        if (sendToDelegate){
            float newX = event.getX() - mRect.left;
            float newY = mSeekBar.getHeight() / 2;

            MotionEvent barEvent = MotionEvent.obtain(event.getDownTime(),
                    event.getEventTime(), event.getAction(),
                    newX,
                    newY,
                    event.getMetaState());
            return mSeekBar.onTouchEvent(barEvent);
        }
        return false;
    }


    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public boolean isEnable() {
        return mEnable;
    }
}
