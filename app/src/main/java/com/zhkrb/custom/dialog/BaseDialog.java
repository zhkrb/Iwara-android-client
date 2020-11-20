package com.zhkrb.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @description：dialogFragment基类
 * @author：zhkrb
 * @DATE： 2020/7/6 11:25
 */
public abstract class BaseDialog extends DialogFragment {

    protected View mRootView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivityContext(),getDialogStyle());
        mRootView = LayoutInflater.from(getActivityContext()).inflate(getLayoutId(),null);
        dialog.setContentView(mRootView);
        dialog.setCancelable(isCanCancel());
        dialog.setCanceledOnTouchOutside(isCanCancelOnTouchOutside());
        setWindowAttributes(dialog.getWindow());
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main();
    }

    /**
     * 主逻辑块
     */
    protected abstract void main();

    /**
     * 设置dialog展示参数
     * @param window dialog window
     */
    protected abstract void setWindowAttributes(Window window);

    /**
     * 是否设置点击dialog区域外返回
     * @return boolean
     */
    protected abstract boolean isCanCancelOnTouchOutside();

    /**
     *是否设置点击返回键返回
     * @return boolean
     */
    protected abstract boolean isCanCancel();

    /**
     * dialog布局
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     *自定义style属性
     * @return int
     */
    protected abstract int getDialogStyle();


    protected abstract Context getActivityContext();


}
