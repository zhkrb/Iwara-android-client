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
 * Create by zhkrb on 2020/3/22 10:55
 */

package com.zhkrb.iwara.custom.refreshView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhkrb.iwara.R;

public class ErrorView extends ConstraintLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public ErrorView(Context context) {
        this(context,null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_refresh_error,this,true);
        mImageView = view.findViewById(R.id.no_data_img);
        mTextView = view.findViewById(R.id.hint);
    }


    public void setError(String errHint, int errDrawable) {
        if (errDrawable != 0){
            mImageView.setImageResource(errDrawable);
        }
        mTextView.setText(errHint);
    }
}
