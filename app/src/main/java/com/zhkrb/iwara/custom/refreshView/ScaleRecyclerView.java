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
 * Create by zhkrb on 2019/9/7 22:12
 */

package com.zhkrb.iwara.custom.refreshView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ScaleRecyclerView extends RecyclerView implements ScaleGestureDetector.OnScaleGestureListener {

    private onScaleListener mScaleListener;
    private ScaleGestureDetector mDetector;
    private static final float Zoom_out_size = 0.5f;
    private static final float Zoom_in_size = 1.5f;
    private boolean mCanScale = false;

    public ScaleRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public ScaleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScaleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mDetector = new ScaleGestureDetector(this.getContext(),this);
    }

    public void setScaleListener(onScaleListener scaleListener) {
        mScaleListener = scaleListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (getAdapter() == null){
            return super.onTouchEvent(e);
        }
        if (e.getAction() == MotionEvent.ACTION_UP){
                if (mCanScale){
                    mCanScale = false;
                    return mDetector.onTouchEvent(e);
//                    return true;
                }else {
                    return super.onTouchEvent(e);
                }
        }
        if (mCanScale){
            return mDetector.onTouchEvent(e);
        }else {
            return super.onTouchEvent(e);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (getAdapter() == null){
            return super.onInterceptTouchEvent(e);
        }
        final int touchCount = e.getPointerCount();
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            mCanScale = touchCount == 2;
        }
        if (mCanScale){
            return true;
        }else {
            return super.onInterceptTouchEvent(e);
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return mCanScale;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        if (mScaleListener!=null){
            if (scaleGestureDetector.getScaleFactor()  < Zoom_out_size){
                mScaleListener.onZoomOut();
            }else if (scaleGestureDetector.getScaleFactor() > Zoom_in_size){
                mScaleListener.onZoomIn();
            }
        }
    }

    public interface onScaleListener{
        void onZoomIn();
        void onZoomOut();
    }
}
