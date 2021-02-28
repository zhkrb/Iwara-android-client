package com.zhkrb.dialog.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.DialogFragment;


/**
 * @description：建造dialog
 * @author：zhkrb
 * @DATE： 2020/7/6 13:55
 */
public abstract class AbsDialog extends BaseDialog {

    protected DialogController mController;
    private DestroyListener mDestroyListener;

    public AbsDialog() {
        mController = createController();
    }

    /**
     * 创建controller
     *
     * @return
     */
    protected abstract DialogController createController();

    private boolean isWaitAddFocusFlag = false;

    @Override
    protected void setWindowStyle(Dialog dialog) {
        setStyle(DialogFragment.STYLE_NORMAL, mController.getStyle());
        isWaitAddFocusFlag = false;
        Window dialogWindow = dialog.getWindow();
        if (isShowNavBar(getActivity())) {
            if (dialogWindow != null) {
                dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                isWaitAddFocusFlag = true;
            }
        }
        setOnShowEvent(dialog);
    }

    private boolean isShowNavBar(Context context) {
        if ((((Activity) context).getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.SYSTEM_UI_FLAG_FULLSCREEN) {
            return true;
        }
        return false;
    }

    protected void setOnShowEvent(Dialog dialog) {
        if (dialog != null) {
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    Dialog dialog = getDialog();
                    if (dialog != null) {
                        Window dialogWindow = dialog.getWindow();
                        if (dialogWindow != null) {
                            if (isWaitAddFocusFlag) {
                                dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                                dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                                dialogWindow.getDecorView().setSystemUiVisibility(uiOptions);
                            }
                        }
                    }
                }
            });
        }
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
        if (mDestroyListener != null) {
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
    public interface DestroyListener {

        /**
         * 销毁
         */
        void onDestroy();

    }


}
