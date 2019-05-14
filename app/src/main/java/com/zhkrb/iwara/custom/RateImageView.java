package com.zhkrb.iwara.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhkrb.iwara.R;

import androidx.annotation.Nullable;

public class RateImageView extends ImageView {

    private float mRate;
    private boolean isWidthRate;

    public RateImageView(Context context) {
        this(context,null);
    }

    public RateImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RateImageView);
        mRate = ta.getFloat(R.styleable.RateImageView_rate, 1);
        isWidthRate = ta.getBoolean(R.styleable.RateImageView_isWidthRate,true);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isWidthRate){
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * mRate), MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (heightSize * mRate), MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
