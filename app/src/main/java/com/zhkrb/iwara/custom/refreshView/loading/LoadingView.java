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
 * Create by zhkrb on 2020/3/19 23:25
 */

package com.zhkrb.iwara.custom.refreshView.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.zhkrb.iwara.utils.L;

public class LoadingView extends ImageView {

    private LoadingDrawable mLoadingDrawable;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLoadingRender();
    }


    private void setLoadingRender() {
        mLoadingDrawable = new LoadingDrawable(new SwapLoadingRender());
        setImageDrawable(mLoadingDrawable);
        post(() -> mLoadingDrawable.setBounds(getLeft(),getTop(),getRight(),getBottom()));
    }

    public void setColorSchemeResources(@ColorRes int... colorResIds){
        final Context context = getContext();
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
        }

        mLoadingDrawable.setColorSchemeColors(colorRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        final boolean visible = visibility == VISIBLE && getVisibility() == VISIBLE;
        if (visible) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }


    public void endAnimation(LoadingDrawable.EndAnimListener listener) {
        if (mLoadingDrawable != null) {
            mLoadingDrawable.endAnim(listener);
        }
    }


    private void startAnimation() {
        L.e("start");
        if (mLoadingDrawable != null) {
            mLoadingDrawable.start();
        }
    }

    private void stopAnimation() {
        L.e("stop");
        if (mLoadingDrawable != null) {
            mLoadingDrawable.stop();
        }
    }


}
