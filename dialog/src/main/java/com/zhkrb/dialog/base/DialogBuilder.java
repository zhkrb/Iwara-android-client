package com.zhkrb.dialog.base;

import android.content.Context;

import com.zhkrb.dialog.dialogManager.DialogShowManager;
import com.zhkrb.dialog.dialogManager.DialogWrapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @description：dialog 建筑类
 * @author：zhkrb
 * @DATE： 2020/7/6 13:50
 */
public abstract class DialogBuilder {
    protected final DialogController.Params mControllerParams;

    public DialogBuilder(Context context) {
        mControllerParams = createParams();
        mControllerParams.setContext(context);
    }

    /**
     * 创建params
     * @return
     */
    protected abstract DialogController.Params createParams();

    /**
     * 创建dialog
     * @return
     */
    protected abstract AbsDialog createDialog();


    public DialogBuilder setLayoutId(int layoutId) {
        mControllerParams.setLayoutId(layoutId);
        return this;
    }

    public DialogBuilder setStyle(int style) {
        mControllerParams.setStyle(style);
        return this;
    }

    public DialogBuilder setCancelOnTouchOutside(boolean cancelOnTouchOutside) {
        mControllerParams.setCancelOnTouchOutside(cancelOnTouchOutside);
        return this;
    }

    public DialogBuilder setCancel(boolean cancel) {
        mControllerParams.setCancel(cancel);
        return this;
    }

    public DialogBuilder setGravity(int gravity){
        mControllerParams.setGravity(gravity);
        return this;
    }

    public DialogBuilder setClickInterface(BaseDialogInterface clickInterface) {
        mControllerParams.setClickInterface(clickInterface);
        return this;
    }

    public DialogBuilder setTitle(String title){
        mControllerParams.setTitle(title);
        return this;
    }

    public DialogBuilder setContent(String content){
        mControllerParams.setContent(content);
        return this;
    }


    public DialogBuilder setDialogLifeListener(DialogController.DialogLifeListener lifeListener){
        mControllerParams.setLifeListener(lifeListener);
        return this;
    }

    public AbsDialog create(){
        AbsDialog dialog = createDialog();
        mControllerParams.apply(dialog.getController());
        if (dialog.getController().getContext() == null){
            return null;
        }
        return dialog;
    }

    public void showNow(){
        DialogShowManager.getInstance().requestShow(new DialogWrapper(this));
    }


    public boolean show() {
        AbsDialog dialog = create();
        if (dialog == null){
            return false;
        }
        Context context = dialog.getController().getContext();
        FragmentManager manager;
        if (context instanceof AppCompatActivity){
            manager = ((AppCompatActivity) context).getSupportFragmentManager();
        }else {
            throw new RuntimeException("context not instance of AppCompatActivity");
        }
        dialog.showNow(manager,"dialog");
        return true;
    }
}
