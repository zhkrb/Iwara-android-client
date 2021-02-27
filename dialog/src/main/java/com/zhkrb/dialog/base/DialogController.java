package com.zhkrb.dialog.base;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.zhkrb.dialog.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import androidx.annotation.IntDef;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/7/6 14:27
 */
public class DialogController {

    private WeakReference<Context> mContext;
    private String mTitle;
    private String mContent;
    private int mLayoutId;
    private int mStyle;
    /**
     * 是否点击区域外取消
     */
    private boolean mCancelOnTouchOutside;
    /**
     * 是否响应返回键
     */
    private boolean mCancel;

    /**
     * 显示位置
     */
    private int mGravity;

    private int mShowAnimation;

    private BaseDialogInterface mClickInterface;
    private DialogLifeListener mLifeListener;

    private static final int FULL = 0x00;
    private static final int BOTTOM = 0x01;
    private static final int CENTER = 0x02;
    private static final int TOP = 0x03;

    @IntDef({
            FULL,
            BOTTOM,
            CENTER,
            TOP
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position{}

    public DialogController() {

    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setLayoutId(int layoutId) {
        mLayoutId = layoutId;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public void setCancelOnTouchOutside(boolean cancelOnTouchOutside) {
        mCancelOnTouchOutside = cancelOnTouchOutside;
    }

    public void setCancel(boolean cancel) {
        mCancel = cancel;
    }

    public void setClickInterface(BaseDialogInterface clickInterface) {
        mClickInterface = clickInterface;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public int getStyle() {
        return mStyle;
    }

    public boolean isCancelOnTouchOutside() {
        return mCancelOnTouchOutside;
    }

    public boolean isCancel() {
        return mCancel;
    }

    public BaseDialogInterface getClickInterface() {
        return mClickInterface;
    }

    public void setWindowAttributes(Window window) {
        mLifeListener.setWindowAttributes(window);
    }

    public DialogLifeListener getLifeListener() {
        return mLifeListener;
    }

    public void setLifeListener(DialogLifeListener lifeListener) {
        mLifeListener = lifeListener;
    }

    public Context getContext() {
        return mContext.get();
    }

    public void setContext(WeakReference<Context> context) {
        mContext = context;
    }


    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public void setClick(View rootView, AbsDialog absDialog) {
        mLifeListener.setClick(rootView, absDialog);
    }

    public int getShowAnimation() {
        return mShowAnimation;
    }

    public void setShowAnimation(int showAnimation) {
        mShowAnimation = showAnimation;
    }

    public interface DialogLifeListener {

        void setWindowAttributes(Window window);

        void setClick(View view, AbsDialog absDialog);
    }


    public static class Params {
        private WeakReference<Context> mContext;
        private String mTitle = "";
        private String mContent = "";
        private int mLayoutId = -1;
        private int mStyle = R.style.DialogDefault;
        private boolean mCancelOnTouchOutside = true;
        private boolean mCancel = true;
        private int mGravity = Gravity.CENTER;
        private int mShowAnimation = -1;
        private BaseDialogInterface mClickInterface;
        private DialogLifeListener mLifeListener;

        public void setTitle(String title) {
            mTitle = title;
        }

        public void setContent(String content) {
            mContent = content;
        }

        public void setLayoutId(int layoutId) {
            mLayoutId = layoutId;
        }

        public void setStyle(int style) {
            mStyle = style;
        }

        public void setCancelOnTouchOutside(boolean cancelOnTouchOutside) {
            mCancelOnTouchOutside = cancelOnTouchOutside;
        }

        public void setCancel(boolean cancel) {
            mCancel = cancel;
        }

        public void setClickInterface(BaseDialogInterface clickInterface) {
            mClickInterface = clickInterface;
        }

        public void setLifeListener(DialogLifeListener lifeListener) {
            mLifeListener = lifeListener;
        }

        public void setContext(Context context) {
            mContext = new WeakReference<>(context);
        }

        public void setGravity(int gravity) {
            mGravity = gravity;
        }

        public void setShowAnimation(int showAnimation) {
            mShowAnimation = showAnimation;
        }

        public void apply(DialogController controller) {
            controller.mTitle = mTitle;
            controller.mContent = mContent;
            controller.mLayoutId = mLayoutId;
            controller.mStyle = mStyle;
            controller.mCancelOnTouchOutside = mCancelOnTouchOutside;
            controller.mCancel = mCancel;
            controller.mGravity = mGravity;
            controller.mClickInterface = mClickInterface;
            controller.mLifeListener = mLifeListener;
            controller.mContext = mContext;
            controller.mShowAnimation = mShowAnimation;
        }
    }


}
