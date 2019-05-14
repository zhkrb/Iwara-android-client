package com.zhkrb.iwara.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapePath;
import com.google.android.material.shape.ShapePathModel;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.utils.DpUtil;

public class CutCardView extends MaterialCardView {

    private ShapeAppearanceModel mShapeAppearanceModel;
    private MaterialShapeDrawable mShapeDrawable;

    private boolean isAllCut;
    private boolean isTopLeftCut;
    private boolean isTopRightCut;
    private boolean isBottomLeftCut;
    private boolean isBottomRightCut;
    private ColorStateList backgoundColor;
    private float CutElevation;
    private float mAllCutRadius;
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;
    private Drawable mForegoundDrawable;

    public CutCardView(Context context) {
        this(context,null);
    }

    public CutCardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialCardViewStyle);
    }

    public CutCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CutCardView);
        isAllCut = ta.getBoolean(R.styleable.CutCardView_AllCut, false);
        isTopLeftCut = ta.getBoolean(R.styleable.CutCardView_TopLeftCut, false);
        isTopRightCut = ta.getBoolean(R.styleable.CutCardView_TopRightCut, false);
        isBottomLeftCut = ta.getBoolean(R.styleable.CutCardView_BottomLeftCut, false);
        isBottomRightCut = ta.getBoolean(R.styleable.CutCardView_BottomRightCut, false);
        CutElevation = ta.getDimension(R.styleable.CutCardView_CutElevation,0);
        mAllCutRadius = ta.getDimension(R.styleable.CutCardView_AllCutRadius,0);
        mTopLeftRadius = ta.getDimension(R.styleable.CutCardView_TopLeftCutRadius,0);
        mTopRightRadius = ta.getDimension(R.styleable.CutCardView_TopRightCutRadius,0);
        mBottomLeftRadius = ta.getDimension(R.styleable.CutCardView_BottomLeftRadius,0);
        mBottomRightRadius = ta.getDimension(R.styleable.CutCardView_BottomRightRadius,0);
        mForegoundDrawable = ta.getDrawable(R.styleable.CutCardView_CutForeground);
        backgoundColor = getCardBackgroundColor();
        ta.recycle();
        init();
    }

    private void init() {
        if (isAllCut || isTopLeftCut || isTopRightCut || isBottomLeftCut || isBottomRightCut){
            mShapeAppearanceModel = new ShapeAppearanceModel();
//            mShapeAppearanceModel.setCornerRadius(0);
            if (isTopLeftCut){
                mShapeAppearanceModel.setTopLeftCorner(new CutCornerTreatment(mTopLeftRadius));
            }
            if (isTopRightCut){
                mShapeAppearanceModel.setTopRightCorner(new CutCornerTreatment(mTopRightRadius));
            }
            if (isBottomLeftCut){
                mShapeAppearanceModel.setBottomLeftCorner(new CutCornerTreatment(mBottomLeftRadius));
            }
            if (isBottomRightCut){
                mShapeAppearanceModel.setBottomRightCorner(new CutCornerTreatment(mBottomRightRadius));
            }
            if (isAllCut){
                mShapeAppearanceModel.setAllCorners(new CutCornerTreatment(mAllCutRadius));
            }
            setBackGround();
        }
    }

    private void setBackGround(){
        if (mShapeAppearanceModel == null){
            return;
        }
        mShapeDrawable = new MaterialShapeDrawable(mShapeAppearanceModel);
        mShapeDrawable.setPaintStyle(Paint.Style.FILL);
        mShapeDrawable.setFillColor(backgoundColor);
        mShapeDrawable.setUseTintColorForShadow(true);
        mShapeDrawable.setElevation(CutElevation);
        mShapeDrawable.setShadowCompatibilityMode(MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS);
        setBackground(mShapeDrawable);
        if (mForegoundDrawable!=null){
            setForeground(mForegoundDrawable);
        }
    }


}
