package com.zhkrb.dragvideo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zhkrb.dragvideo.R.layout;

public class VideoPlayView extends FrameLayout {
    private Context mContext;

    public VideoPlayView(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public VideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        View view = inflater.inflate(layout.view_video_play, this, false);
        this.addView(view);
    }
}
