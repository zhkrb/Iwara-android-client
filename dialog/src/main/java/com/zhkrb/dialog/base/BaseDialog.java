package com.zhkrb.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setWindowStyle(dialog);
        if (getShowAnim() != -1){
            dialog.getWindow().setWindowAnimations(getShowAnim());
        }
        dialog.setCancelable(isCanCancel());
        dialog.setCanceledOnTouchOutside(isCanCancelOnTouchOutside());
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() == -1){
            throw new RuntimeException("must set layout");
        }
        mRootView = inflater.inflate(getLayoutId(),null,false);
        bindView(mRootView);
        setWindowAttributes(getDialog());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main();
    }

    /**
     * 设置dialog样式
     * @param dialog
     */
    protected abstract void setWindowStyle(Dialog dialog);

    /**
     * 设置显示动画
     * @return
     */
    protected abstract int getShowAnim();

    /**
     * 绑定view
     */
    protected abstract void bindView(View view);

    /**
     * 主逻辑块
     */
    protected abstract void main();

    /**
     * 设置dialog展示参数
     * @param dialog
     */
    protected abstract void setWindowAttributes(Dialog dialog);

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

    /**
     * 点击间隔时间
     */
    public static final int CLICK_DURATION = 100;
    private static long lastClickTime;

    public static boolean canClick(){
        long currentTime = System.currentTimeMillis();
        long timeInterval = currentTime - lastClickTime;
        if (timeInterval > CLICK_DURATION) {
            lastClickTime = currentTime;
            return true;
        }
        return false;
    }

    /**
     * dp转px
     * @param dpVal
     * @return
     */
    public static float dp2px(Context context,float dpVal) {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 返回屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }


}