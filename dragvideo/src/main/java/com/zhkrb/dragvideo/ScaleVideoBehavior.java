
package com.zhkrb.dragvideo;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior;
import com.zhkrb.dragvideo.R.id;

public class ScaleVideoBehavior extends Behavior<View> {
    private static float scale;
    private float targetWidth;

    public ScaleVideoBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scale = context.getResources().getDisplayMetrics().density;
    }

    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof FrameLayout;
    }

    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        int mParentWidth = parent.getWidth();
        int deltaX = mParentWidth - dependency.getWidth() < 0 ? 0 : mParentWidth - dependency.getWidth();
        View view = child.findViewById(id.btn_close);
        View view1 = child.findViewById(id.btn_play);
        child.setLayerType(2, (Paint)null);
        if (deltaX <= dp2px(50)) {
            view.getLayoutParams().width = deltaX;
        } else if (deltaX <= dp2px(100) && deltaX > dp2px(50)) {
            view1.getLayoutParams().width = deltaX - dp2px(50);
        } else {
            view.getLayoutParams().width = dp2px(50);
            view1.getLayoutParams().width = dp2px(50);
        }

        LayoutParams params = child.getLayoutParams();
        params.width = deltaX;
        child.setAlpha((float)deltaX / ((float)mParentWidth - this.targetWidth));
        parent.requestLayout();
        child.setLayerType(0, (Paint)null);
        return true;
    }

    private static int dp2px(int dpVal) {
        return (int)(scale * (float)dpVal + 0.5F);
    }

    public void setTargetWidth(float targetWidth) {
        this.targetWidth = targetWidth;
    }
}
