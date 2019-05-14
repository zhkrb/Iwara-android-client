package com.zhkrb.iwara.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MaterialAppBarLayout extends LinearLayout {

    public MaterialAppBarLayout(Context context) {
        this(context,null);
    }

    public MaterialAppBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaterialAppBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
