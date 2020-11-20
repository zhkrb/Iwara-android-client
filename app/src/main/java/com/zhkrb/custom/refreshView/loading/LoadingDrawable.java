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
 * Create by zhkrb on 2020/3/18 21:16
 */

package com.zhkrb.custom.refreshView.loading;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoadingDrawable extends Drawable implements Animatable {

    private LoadingRender mLoadingRender;

    public LoadingDrawable(@NonNull LoadingRender loadingRender) {
        mLoadingRender = loadingRender;
        mLoadingRender.setCallback(mCallback);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (!getBounds().isEmpty()){
            mLoadingRender.draw(canvas);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mLoadingRender.setBounds(bounds);
    }

    @Override
    public void setAlpha(int alpha) {
        mLoadingRender.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mLoadingRender.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    public void start() {
        mLoadingRender.start();
    }

    @Override
    public void stop() {
        mLoadingRender.stop();
    }

    public void endAnim(EndAnimListener listener){
        mLoadingRender.endAnim(listener);
    }


    @Override
    public boolean isRunning() {
        return mLoadingRender.isRunning();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) mLoadingRender.getHieght();
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) mLoadingRender.getWidth();
    }

    public void setColorSchemeColors(int... color) {
        mLoadingRender.setColorSchemeColors(color);
    }

    public interface EndAnimListener {
        void isEnd();
    }

    private final Callback mCallback = new Callback() {
        @Override
        public void invalidateDrawable(@NonNull Drawable who) {
            invalidateSelf();
        }

        @Override
        public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
            scheduleSelf(what,when);
        }

        @Override
        public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
            unscheduleSelf(what);
        }
    };
}
