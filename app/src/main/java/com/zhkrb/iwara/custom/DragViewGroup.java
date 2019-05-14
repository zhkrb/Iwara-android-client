package com.zhkrb.iwara.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.utils.DpUtil;
import com.zhkrb.iwara.utils.L;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class DragViewGroup extends CoordinatorLayout {

    private ViewDragHelper mDragHelper;
    private ViewGroup mHeaderView;           //第一个视图
    private ViewGroup mDescView;             //第二个视图
    private int mTop;                   //第一个视图距离顶部的距离
    private int mViewDragRangeTop;      //全屏模式时的滑动范围
    private int mViewDragRangeBottom;   //小控件模式时的滑动范围
    private float originalWidth;        //原始宽度
    private float originalFirstHeight;  //第一个视图原始高度
    private float originalSecHeight;    //第二个视图原始高度
    private float mDragOffset;

    private static final int TYPE_FULL = 0x000000001;
    private static final int TYPE_SMILL = 0x000000002;
    private static final int TYPE_HIDE = 0x000000003;
    private static final int SCROLL_POS_UP = 0x00001001;
    private static final int SCROLL_POS_DOWN = 0x00001002;
    private int mScrollType = TYPE_FULL;
    private int mScrollDirt = 0;    //view滚动方向

    private int mSmillTypeBottomHeight = 20;    //临时 小窗口时控件到底部的高度
    private int mSmillTypeHeight = 70;          //临时 小窗口时控件高度
    private int mSillTypePaddingLeftRight = 50; //临时 小窗口时两侧padding
    private int mViewLeft = 0;                  //view距离左侧宽度，下面同理
    private int mViewRight = 0;

    private boolean isFirstLayout = true;
    private int mHeaderOrderHeight;             //第一个视图实时高度
    private int mDescOriderHeight;              //第二个视图实时高度

    private Paint testPaint;
    private float mDownMotionY;
    private float mDownMotionX;


    public DragViewGroup(Context context) {
        this(context,null);
    }

    public DragViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount()>2){
            throw new RuntimeException("make sure only 2 Child view in your layout");
        }
        init();
    }

    private void init() {
//        mHeaderView = findViewById(R.id.header_view);
//        mDescView = findViewById(R.id.desc_view);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());

        testPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        testPaint.setColor(Color.GREEN);
        testPaint.setStrokeWidth(15.0f);

    }

    public void goMax(){
        if (mScrollType == TYPE_FULL){
            return;
        }else if (mScrollType == TYPE_HIDE) {
            goSmill();
//            while (mDragHelper.getViewDragState()==ViewDragHelper.STATE_IDLE){
            while (mDragHelper.continueSettling(false)){
                mScrollType = TYPE_SMILL;
                mScrollDirt = SCROLL_POS_UP;
            }
//            }
        }

        mDragHelper.smoothSlideViewTo(mHeaderView,0,0);
        invalidate();
    }

    public void goSmill(){
        if (mScrollType == TYPE_SMILL){
            return;
        }
        int top = mViewDragRangeTop;
        int left = DpUtil.dp2px(mSillTypePaddingLeftRight)/2;
        mDragHelper.smoothSlideViewTo(mHeaderView,left,top);
        invalidate();
    }

    public void goHide(){
        if (mScrollType == TYPE_HIDE){
            return;
        }else if (mScrollType == TYPE_FULL){
            goSmill();
//            while (mDragHelper.getViewDragState()==ViewDragHelper.STATE_IDLE){
                mScrollType = TYPE_SMILL;
//            }
        }
        int top = (int) (originalFirstHeight+originalSecHeight);
        int left = DpUtil.dp2px(mSillTypePaddingLeftRight)/2;
        mDragHelper.smoothSlideViewTo(mHeaderView,left,top);
        invalidate();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth,widthMeasureSpec,0),
                                resolveSizeAndState(maxHeight,heightMeasureSpec,0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isFirstLayout){
//            mViewDragRangeTop = getHeight() - mHeaderView.getHeight()-DpUtil.dp2px(mSmillTypeBottomHeight);
            mViewDragRangeTop = getHeight()
                    - DpUtil.dp2px(mSmillTypeBottomHeight) - DpUtil.dp2px(mSmillTypeHeight);
//            mViewDragRangeBottom = DpUtil.dp2px(mSmillTypeHeight)+DpUtil.dp2px(mSmillTypeBottomHeight);
            mViewDragRangeBottom = getHeight();
            mHeaderView.layout(mViewLeft,mTop,r,mTop+mHeaderView.getMeasuredHeight());
            mDescView.layout(mViewLeft,mTop+mHeaderView.getMeasuredHeight(),r,mTop+b);
            originalWidth = mHeaderView.getWidth();
            originalFirstHeight = mHeaderView.getHeight();
            originalSecHeight = mDescView.getHeight();
        }else {
            mHeaderView.layout(mViewLeft,mTop,mViewRight,mTop+mHeaderOrderHeight);
            mDescView.layout(mViewLeft,mTop+mHeaderOrderHeight,mViewRight,mTop+mHeaderOrderHeight+mDescOriderHeight);
        }
        isFirstLayout = false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawLine(0,mViewDragRangeTop,originalWidth,mViewDragRangeTop,testPaint);
//        testPaint.setColor(Color.YELLOW);
//        canvas.drawLine(0,getContext().getResources().getDisplayMetrics().heightPixels-555,originalWidth-100,getContext().getResources().getDisplayMetrics().heightPixels-555,testPaint);
        testPaint.setColor(Color.BLUE);
        canvas.drawLine(0,mViewDragRangeBottom,originalWidth,mViewDragRangeBottom,testPaint);
        testPaint.setColor(Color.parseColor("#7b1fa2"));
        canvas.drawLine(0,mTop,originalWidth,mTop,testPaint);

//        int[] location = new int[2];
//        mHeaderView.getChildAt(0).getLocationInWindow(location);
//        int left = location[0];
//        int top = location[1];
//        int right = left + mHeaderView.getChildAt(0).getMeasuredWidth();
//        int bottom = top + mHeaderView.getChildAt(0).getMeasuredHeight();
//        canvas.drawLine(left,top,right,top,testPaint);
//        canvas.drawLine(right,top,right,bottom,testPaint);
//        canvas.drawLine(left,bottom,right,bottom,testPaint);
//        canvas.drawLine(left,top,left,bottom,testPaint);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (( action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mDownMotionY = y;
                mDownMotionX = x;
                interceptTap = mDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
                if (interceptTap){
                    if (isChildViewClick(mHeaderView, (int) ev.getRawX(), (int) ev.getRawY())){
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = Math.abs(x - mDownMotionX);
                float deltaY = Math.abs(y - mDownMotionY);
                final int slop = mDragHelper.getTouchSlop();
                if (deltaY > slop && deltaX > deltaY) {
                    mDragHelper.cancel();
                    return false;
                }else {
                    if (y - mDownMotionY>0){
                        mScrollDirt = SCROLL_POS_DOWN;
                    }else {
                        mScrollDirt = SCROLL_POS_UP;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                float deltaX1 = Math.abs(x - mDownMotionX);
                float deltaY1 = Math.abs(y - mDownMotionY);
                final int slop1 = mDragHelper.getTouchSlop();
                if ((deltaY1 > slop1 && deltaX1 > deltaY1) ||(deltaY1<slop1)) {
                    mDragHelper.cancel();
                    return false;
                }
                break;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
         mDragHelper.processTouchEvent(ev);
         final int action = ev.getAction();
         final float x = ev.getX();
         final float y = ev.getY();
         boolean isHeaderViewUnder = mDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
         return isHeaderViewUnder && isViewHit(mHeaderView,(int)x ,(int) y) || isViewHit(mDescView,(int)x ,(int) y);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View[] views = getChildView();
//        if (views!=null&&views.length>0){
//
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private boolean isChildViewClick(ViewGroup headerView, int x, int y){
                View[] views = getChildView(headerView);
        if (views!=null&&views.length>0){
            for (View view:views){
                if (isTouchPointInView(view,x,y)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTouchPointInView(View targetView, int xAxis, int yAxis) {
        if (targetView== null) {
            return false;
        }
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + targetView.getMeasuredWidth();
        int bottom = top + targetView.getMeasuredHeight();
        if (yAxis >= top && yAxis <= bottom && xAxis >= left
                && xAxis <= right) {
            return true;
        }
        return false;
    }

    private View[] getChildView(ViewGroup headerView) {
        View[] views = null;
        views = new View[headerView.getChildCount()];
        for (int i=0;i<headerView.getChildCount();i++){
            View view = headerView.getChildAt(i);
            views[i] = view;
        }
        return views;
    }

    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child==mHeaderView;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            mHeaderView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mDescView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mTop = top;
//            if (top >= mViewDragRangeTop){
            if (mScrollType == TYPE_FULL || (mScrollType == TYPE_SMILL&&top < mViewDragRangeTop)){
                mDragOffset = (float) top / mViewDragRangeTop;
            }else {
                mDragOffset = (float) (top-mViewDragRangeTop) / (mViewDragRangeBottom - mViewDragRangeTop);
            }
            L.e("dragOffset: ", String.valueOf(mDragOffset));
//            L.e(String.valueOf(mTop));
//            if (top >= mViewDragRangeTop){
            if (mScrollType == TYPE_FULL || (mScrollType == TYPE_SMILL&&top < mViewDragRangeTop)){
                mViewLeft = (int) (mDragOffset*(DpUtil.dp2px(mSillTypePaddingLeftRight)/2));
                mViewRight = (int) ((int) originalWidth-(mDragOffset*(DpUtil.dp2px(mSillTypePaddingLeftRight)/2)));
                mHeaderOrderHeight = (int) (originalFirstHeight - mDragOffset*(originalFirstHeight-DpUtil.dp2px(mSmillTypeHeight)));
                float fastOff = (1+0.1f)*mDragOffset;
                if (fastOff>1){
                    fastOff = 1;
                }
                L.e("dragOffset1: ", String.valueOf(fastOff));
                mDescOriderHeight = (int) (originalSecHeight - mDragOffset*(originalSecHeight));
                mDescView.setAlpha(1 - fastOff);
            }else {
                    mHeaderView.setAlpha(1 - mDragOffset);
            }
            requestLayout();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            switch (state){
                case ViewDragHelper.STATE_IDLE:
                    mHeaderView.setLayerType(View.LAYER_TYPE_NONE, null);
                    mDescView.setLayerType(View.LAYER_TYPE_NONE, null);
                    if (mTop == mViewDragRangeTop){
                        mScrollType = TYPE_SMILL;
                    }else if (mTop == originalFirstHeight+originalSecHeight){
                        mScrollType = TYPE_HIDE;
                    }else {
                        mScrollType = TYPE_FULL;
                    }
                    break;
            }
            L.e(String.valueOf(mScrollType));
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int top = 0;
            int left = 0;
            if (mScrollType == TYPE_FULL){
                if (yvel>0 || (yvel==0 && mDragOffset > 0.5f)){
                    top = mViewDragRangeTop;
                    left = DpUtil.dp2px(mSillTypePaddingLeftRight)/2;
                }
            }else {
                top = mViewDragRangeTop;
                left = DpUtil.dp2px(mSillTypePaddingLeftRight)/2;
                if (yvel >0 || (yvel == 0 && mDragOffset>0.3f)){
                    top = (int) (originalFirstHeight+originalSecHeight);
                }else if (yvel <0 ||(yvel == 0 && mDragOffset < 0.7f)){
                    left = 0;
                    top = 0;
                }
            }

            mDragHelper.settleCapturedViewAt(left,top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            if (mScrollType == TYPE_SMILL){
                return mViewDragRangeBottom;
            }else {
                return mViewDragRangeTop;
            }
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int newTop;
            int topBound = getPaddingTop();
            if (mScrollType == TYPE_FULL || (mScrollType == TYPE_SMILL&&top < mViewDragRangeTop)){
                newTop = Math.min(Math.max(top,topBound),mViewDragRangeTop);
            }else {
                topBound = mViewDragRangeTop;

                newTop = Math.min(Math.max(top, topBound), mViewDragRangeBottom);
            }
            return newTop;
        }
    }
}
