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
 * Create by zhkrb on 2019/10/12 0:00
 */

package com.zhkrb.iwara.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhkrb.dragvideo.ViewWrapper;
import com.zhkrb.iwara.R;

public class SwitchButton extends ConstraintLayout {


    private final float mInterval;
    private boolean isButtonSelect;
    private final int mSelectImageId;
    private final String mSelectText;
    private final int mSelectBackgroundColor;
    private final int mUnSelectImageId;
    private final String mUnSelectText;
    private final int mUnSelectBackgroundColor;
    private final int mSelectTextColor;
    private final int mUnSelectTextColor;
    private final float mImageSize;
    private final float mTextSize;


    private ImageView mImageView;
    private TextView mTextView;

    public SwitchButton(Context context) {
        this(context,null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     *         <attr name="select" format="boolean"/>
     *         <attr name="select_image" format="reference"/>
     *         <attr name="select_text" format="string"/>
     *         <attr name="unselect_image" format="reference"/>
     *         <attr name="unselect_text" format="string"/>
     *         <attr name="select_background_color" format="color"/>
     *         <attr name="unselect_background_color" format="color"/>
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        isButtonSelect = ta.getBoolean(R.styleable.SwitchButton_select, false);
        mSelectImageId = ta.getResourceId(R.styleable.SwitchButton_select_image,0);
        mSelectText = ta.getString(R.styleable.SwitchButton_select_text);
        mSelectBackgroundColor = ta.getColor(R.styleable.SwitchButton_select_background_color,0);
        mUnSelectImageId = ta.getResourceId(R.styleable.SwitchButton_unselect_image,0);
        mUnSelectText = ta.getString(R.styleable.SwitchButton_unselect_text);
        mUnSelectBackgroundColor = ta.getColor(R.styleable.SwitchButton_unselect_background_color,0);
        mSelectTextColor = ta.getColor(R.styleable.SwitchButton_select_text_color,0);
        mUnSelectTextColor = ta.getColor(R.styleable.SwitchButton_unselect_text_color,0);
        mImageSize = ta.getDimension(R.styleable.SwitchButton_image_size,0);
        mTextSize = ta.getInt(R.styleable.SwitchButton_text_size,0);
        mInterval = ta.getDimension(R.styleable.SwitchButton_interval,0);
        ta.recycle();
        initButton();
    }

    private void initButton() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_switchbutton,this,true);
        mImageView = view.findViewById(R.id.btn_img);
        mTextView = view.findViewById(R.id.btn_text);
        ViewWrapper imgWrapper = new ViewWrapper(mImageView);
        imgWrapper.setHeight((int) mImageSize);
        imgWrapper.setWidth((int) mImageSize);
        imgWrapper.setRightMargin((int) mInterval);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,mTextSize);
        mImageView.requestLayout();
        mTextView.requestLayout();
        setButtonSelect(isButtonSelect);
    }

    public boolean isButtonSelect() {
        return isButtonSelect;
    }

    public void setButtonSelect(boolean buttonSelect) {
        isButtonSelect = buttonSelect;
        setBackgroundColor(isButtonSelect ? mSelectBackgroundColor : mUnSelectBackgroundColor);
        mTextView.setTextColor(isButtonSelect ? mSelectTextColor : mUnSelectTextColor);
        mTextView.setText(isButtonSelect ? mSelectText : mUnSelectText);
        mImageView.setImageResource(isButtonSelect ? mSelectImageId : mUnSelectImageId);
        mImageView.setImageTintList(ColorStateList.valueOf(isButtonSelect ? mSelectTextColor : mUnSelectTextColor));
    }

}
