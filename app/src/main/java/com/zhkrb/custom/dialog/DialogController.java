package com.zhkrb.custom.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.zhkrb.iwara.R;

import java.lang.ref.WeakReference;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/7/6 14:27
 */
public class DialogController {

    private WeakReference<Context> mContext;
    private FragmentManager mFragmentManager;
    private Button mConfirmButton;
    private Button mCancelButton;
    private String mTitle;
    private String mContent;
    private String mEditHintText;
    private RecyclerView mSelectView;
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
     * 是否展示圆角
     */
    private boolean mIsCornerRadius;
    /**
     * 圆角角度大小 单位：dp
     */
    private int mRadius;

    /**
     * 是否自定义window
     * 只有在false时 WidthRate backgroundAlpha gravity才会生效
     */
    private boolean mIsCustomWindow;
    /**
     * 宽度比例
     */
    private float mWidthRate;
    /**
     * 显示位置
     */
    private int mGravity;
    /**
     * 是否显示背景为透明
     * 只有为false时BackgroundAlpha才会生效
     */
    private boolean mIsBackgroundAlpha;
    /**
     * 背景半透明灰度 范围 0~1
     */
    private float mBackgroundAlphaRate;

    private BaseDialogInterface mClickInterface;
    private DialogLifeListener mLifeListener;

    private boolean mIsCustomClick;

    public DialogController() {

    }

    public void setConfirmButton(Button confirmButton) {
        mConfirmButton = confirmButton;
    }

    public void setCancelButton(Button cancelButton) {
        mCancelButton = cancelButton;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setEditHintText(String editHintText) {
        mEditHintText = editHintText;
    }

    public void setSelectView(RecyclerView selectView) {
        mSelectView = selectView;
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

    public Button getConfirmButton() {
        return mConfirmButton;
    }

    public Button getCancelButton() {
        return mCancelButton;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getEditHintText() {
        return mEditHintText;
    }

    public RecyclerView getSelectView() {
        return mSelectView;
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

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public boolean isCornerRadius() {
        return mIsCornerRadius;
    }

    public void setCornerRadius(boolean cornerRadius) {
        mIsCornerRadius = cornerRadius;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public float getWidthRate() {
        return mWidthRate;
    }

    public void setWidthRate(float widthRate) {
        mWidthRate = widthRate;
    }

    public boolean isCustomWindow() {
        return mIsCustomWindow;
    }

    public void setCustomWindow(boolean customWindow) {
        mIsCustomWindow = customWindow;
    }

    public boolean getBackgroundAlphaRate() {
        return mIsBackgroundAlpha;
    }

    public void setBackgroundAlphaRate(boolean backgroundAlphaRate) {
        mIsBackgroundAlpha = backgroundAlphaRate;
    }

    public float getBackgroundAlpha() {
        return mBackgroundAlphaRate;
    }

    public void setBackgroundAlpha(float backgroundAlpha) {
        mBackgroundAlphaRate = backgroundAlpha;
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public boolean isCustomClick() {
        return mIsCustomClick;
    }

    public void setCustomClick(boolean customClick) {
        mIsCustomClick = customClick;
    }

    public void setClick(View rootView, AbsDialog absDialog) {
        mLifeListener.setClick(rootView,absDialog);
    }

    public interface DialogLifeListener{

        void setWindowAttributes(Window window);

        void setClick(View view, AbsDialog absDialog);
    }


    public static class Params{
        private WeakReference<Context> mContext;
        private FragmentManager mFragmentManager;
        private String mTitle;
        private String mContent;
        private String mEditHintText;
        private int mLayoutId;
        private int mStyle = R.style.dialog_null;
        private boolean mCancelOnTouchOutside = true;
        private boolean mCancel = true;
        private boolean mIsCornerRadius = true;
        private int mRadius = 3;
        private boolean mIsCustomWindow = false;
        private boolean mIsCustomClick = false;
        private int mGravity = Gravity.CENTER;
        private float mWidthRate = 0.85f;
        private boolean mIsBackgroundAlpha = false;
        private float mBackgroundAlphaRate = 0.3f;
        private BaseDialogInterface mClickInterface;
        private DialogLifeListener mLifeListener;

        public void setTitle(String title) {
            mTitle = title;
        }

        public void setContent(String content) {
            mContent = content;
        }

        public void setEditHintText(String editHintText) {
            mEditHintText = editHintText;
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

        public void setCornerRadius(boolean cornerRadius) {
            mIsCornerRadius = cornerRadius;
        }

        public void setRadius(int radius) {
            mRadius = radius;
        }

        public void setCustomWindow(boolean customWindow) {
            mIsCustomWindow = customWindow;
        }

        public void setCustomClick(boolean customClick){
            mIsCustomClick = customClick;
        }

        public void setWidthRate(float widthRate) {
            mWidthRate = widthRate;
        }

        public void setBackgroundAlphaRate(boolean backgroundAlphaRate) {
            mIsBackgroundAlpha = backgroundAlphaRate;
        }

        public void setBackgroundAlpha(float backgroundAlpha) {
            mBackgroundAlphaRate = backgroundAlpha;
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

        public void setFragmentManager(FragmentManager fragmentManager) {
            mFragmentManager = fragmentManager;
        }

        public void setGravity(int gravity) {
            mGravity = gravity;
        }

        public void apply(DialogController controller) {
            controller.mTitle = mTitle;
            controller.mContent = mContent;
            controller.mEditHintText = mEditHintText;
            controller.mLayoutId = mLayoutId;
            controller.mStyle = mStyle;
            controller.mCancelOnTouchOutside = mCancelOnTouchOutside;
            controller.mCancel = mCancel;
            controller.mIsCornerRadius = mIsCornerRadius;
            controller.mRadius = mRadius;
            controller.mIsCustomWindow = mIsCustomWindow;
            controller.mIsCustomClick = mIsCustomClick;
            controller.mWidthRate = mWidthRate;
            controller.mIsBackgroundAlpha = mIsBackgroundAlpha;
            controller.mBackgroundAlphaRate = mBackgroundAlphaRate;
            controller.mGravity = mGravity;
            controller.mClickInterface = mClickInterface;
            controller.mLifeListener = mLifeListener;
            controller.mContext = mContext;
            controller.mFragmentManager = mFragmentManager;
        }
    }



}
