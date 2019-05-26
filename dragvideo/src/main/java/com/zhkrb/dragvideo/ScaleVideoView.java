

package com.zhkrb.dragvideo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zhkrb.dragvideo.R.id;
import com.zhkrb.dragvideo.R.layout;
import com.zhkrb.dragvideo.R.styleable;

public class ScaleVideoView extends FrameLayout implements OnClickListener {
    private Context mContext;
    private int mLayoutRes;
    private static float scale;
    private float mTargetWidth;
    private float mProgress;
    private FrameLayout mVideoContent;
    private ViewWrapper mVideoWrapper;
    private ViewWrapper mInfoWrapper;
    private ImageView mBtnPlay;
    private ViewWrapper mPlayWrapper;
    private ImageView mBtnClose;
    private ViewWrapper mCloseWrapper;

    public ScaleVideoView(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public ScaleVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mProgress = 0.0F;
        mContext = context;
        scale = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, styleable.ScaleVideoView);
        mLayoutRes = ta.getResourceId(styleable.ScaleVideoView_view_layout, layout.view_scale_video);
        mTargetWidth = ta.getDimension(styleable.ScaleVideoView_target_width, 0.0F);
        ta.recycle();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(mLayoutRes, this, false);
        addView(view);
        mVideoContent = (FrameLayout)view.findViewById(id.video_content);
        mVideoWrapper = new ViewWrapper(mVideoContent);
        mInfoWrapper = new ViewWrapper(view.findViewById(id.info_content));
        mBtnPlay = (ImageView)view.findViewById(id.btn_play);
        mPlayWrapper = new ViewWrapper(mBtnPlay);
        mBtnClose = (ImageView)view.findViewById(id.btn_close);
        mCloseWrapper = new ViewWrapper(mBtnClose);
        mBtnClose.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        init();
    }

    private void init() {
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        this.reLayout();
    }

    public void reLayout() {
        int mParentWidth = this.getWidth();
        mVideoWrapper.setWidth(mParentWidth - (int)(((float)mParentWidth - this.mTargetWidth) * this.mProgress / 100.0F));
        int deltaX = mParentWidth - this.mVideoWrapper.getWidth() < 0 ? 0 : mParentWidth - this.mVideoWrapper.getWidth();
        setLayerType(View.LAYER_TYPE_HARDWARE, (Paint)null);
        if (deltaX <= dp2px(50)) {
            mCloseWrapper.setWidth(deltaX);
        } else if (deltaX <= dp2px(100) && deltaX > dp2px(50)) {
            mPlayWrapper.setWidth(deltaX - dp2px(50));
        } else {
            mCloseWrapper.setWidth(dp2px(50));
            mPlayWrapper.setWidth(dp2px(50));
        }

        mInfoWrapper.setWidth(deltaX);
        mInfoWrapper.getView().setAlpha((float)deltaX / ((float)mParentWidth - this.mTargetWidth));
        requestLayout();
        setLayerType(View.LAYER_TYPE_NONE, (Paint)null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, dp2px(220));
        }

    }

    private static int dp2px(int dpVal) {
        return (int)(scale * (float)dpVal + 0.5F);
    }

    public void onClick(View view) {
    }
}
