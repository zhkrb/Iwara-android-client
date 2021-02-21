package com.zhkrb.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import com.zhkrb.dialog.base.BaseDialog;
import com.zhkrb.dialog.base.DialogController;
import com.zhkrb.dialog.dialogManager.DialogShowManager;

import androidx.cardview.widget.CardView;


/**
 * @description：建造dialog
 * @author：zhkrb
 * @DATE： 2020/7/6 13:55
 */
public abstract class AbsDialog extends BaseDialog {

    protected DialogController mController;
    private DestroyListener mDestroyListener;

    public AbsDialog() {
        mController = new DialogController();
    }

//    @Override
//    protected void setWindowAttributes(Window window) {
//        if (mController.isCustomWindow()){
//            mController.setWindowAttributes(window);
//        }else {
//            WindowManager.LayoutParams params = window.getAttributes();
//
//            int screenWidth = getScreenWidth(getActivityContext());
//            params.width = (int) (screenWidth * mController.getWidthRate());
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            params.gravity = Gravity.CENTER;
//            if (mController.getBackgroundAlphaRate()){
//                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            }else {
//                ColorDrawable drawable = new ColorDrawable(Color.GRAY);
//                drawable.setAlpha((int) (255 * mController.getBackgroundAlpha()));
//                window.setBackgroundDrawable(drawable);
//            }
//            window.setAttributes(params);
//        }
//    }


    @Override
    protected void setWindowStyle(Dialog dialog) {
        setStyle(STYLE_NO_FRAME,mController.getStyle());
    }

    @Override
    protected int getShowAnim() {
        return mController.getShowAnimation();
    }

    @Override
    protected boolean isCanCancelOnTouchOutside() {
        return mController.isCancelOnTouchOutside();
    }

    @Override
    protected boolean isCanCancel() {
        return mController.isCancel();
    }

    @Override
    protected int getLayoutId() {
        return mController.getLayoutId();
    }

    @Override
    protected int getDialogStyle() {
        return mController.getStyle();
    }

    @Override
    protected Context getActivityContext() {
        return mController.getContext();
    }


    public DialogController getController() {
        return mController;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController = null;
        if (mDestroyListener != null){
            mDestroyListener.onDestroy();
        }

        DialogShowManager.getInstance().showOff();
    }

    public void setDestroyListener(DestroyListener destroyListener) {
        mDestroyListener = destroyListener;
    }

    /**
     * dialog销毁监听
     */
    public interface DestroyListener{

        /**
         * 销毁
         */
        void onDestroy();

    }


}
