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
    private final DialogController.Params mController;

    public DialogBuilder(Context context, FragmentManager fragmentManager) {
        mController = createParams();
        mController.setContext(context);
    }

    /**
     * 创建params
     * @return
     */
    public abstract DialogController.Params createParams();

    /**
     * 创建dialog
     * @return
     */
    public abstract AbsDialog createDialog();


    public DialogBuilder setLayoutId(int layoutId) {
        mController.setLayoutId(layoutId);
        return this;
    }

    public DialogBuilder setStyle(int style) {
        mController.setStyle(style);
        return this;
    }

    public DialogBuilder setCancelOnTouchOutside(boolean cancelOnTouchOutside) {
        mController.setCancelOnTouchOutside(cancelOnTouchOutside);
        return this;
    }

    public DialogBuilder setCancel(boolean cancel) {
        mController.setCancel(cancel);
        return this;
    }

    public DialogBuilder setGravity(int gravity){
        mController.setGravity(gravity);
        return this;
    }

    public DialogBuilder setClickInterface(BaseDialogInterface clickInterface) {
        mController.setClickInterface(clickInterface);
        return this;
    }

    public DialogBuilder setTitle(String title){
        mController.setTitle(title);
        return this;
    }

    public DialogBuilder setContent(String content){
        mController.setContent(content);
        return this;
    }


    public DialogBuilder setDialogLifeListener(DialogController.DialogLifeListener lifeListener){
        mController.setLifeListener(lifeListener);
        return this;
    }

    public AbsDialog create(){
        AbsDialog dialog = createDialog();
        mController.apply(dialog.getController());
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
