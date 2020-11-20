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
 * Create by zhkrb on 2019/9/8 15:24
 */

package com.zhkrb.dragvideo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.zhkrb.dragvideo.R;

public class AdvRecyclerView extends RecyclerView {

    private float mMaxHeight;

    public AdvRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public AdvRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AdvRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AdvRecyclerView);
        mMaxHeight = ta.getDimension(R.styleable.AdvRecyclerView_maxHeight1, 0f);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (mMaxHeight>0){
            heightSpec = MeasureSpec.makeMeasureSpec((int) mMaxHeight,heightSpec);
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
