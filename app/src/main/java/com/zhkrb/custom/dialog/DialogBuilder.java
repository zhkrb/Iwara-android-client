package com.zhkrb.custom.dialog;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

/**
 * @description：dialog 建筑类
 * @author：zhkrb
 * @DATE： 2020/7/6 13:50
 */
public class DialogBuilder {
    private DialogController.Params mController;

    public DialogBuilder(Context context, FragmentManager fragmentManager) {
        mController = new DialogController.Params();
        mController.setContext(context);
        mController.setFragmentManager(fragmentManager);
    }


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

    public DialogBuilder setCornerRadius(boolean cornerRadius){
        mController.setCornerRadius(cornerRadius);
        return this;
    }

    public DialogBuilder setRadius(int radius){
        mController.setRadius(radius);
        return this;
    }

    public DialogBuilder setCustomWindow(boolean customWindow){
        mController.setCustomWindow(customWindow);
        return this;
    }

    public DialogBuilder setCustomClick(boolean customClick){
        mController.setCustomClick(customClick);
        return this;
    }

    public DialogBuilder setGravity(int gravity){
        mController.setGravity(gravity);
        return this;
    }

    public DialogBuilder setWidthRate(float widthRate){
        mController.setWidthRate(widthRate);
        return this;
    }

    public DialogBuilder setBackgroundAlpha(boolean backgroundAlpha){
        mController.setBackgroundAlphaRate(backgroundAlpha);
        return this;
    }

    public DialogBuilder setBackgroundAlphaRate(float rate){
        mController.setBackgroundAlpha(rate);
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

    public DialogBuilder setEditHintText(String editHintText){
        mController.setEditHintText(editHintText);
        return this;
    }

    public DialogBuilder setDialogLifeListener(DialogController.DialogLifeListener lifeListener){
        mController.setLifeListener(lifeListener);
        return this;
    }

    public AbsDialog create(){
        AbsDialog dialog = new AbsDialog();
        mController.apply(dialog.mController);
        if (dialog.mController.getContext() == null){
            return null;
        }
        return dialog;
    }


    public boolean show() {
        AbsDialog dialog = create();
        if (dialog == null){
            return false;
        }
        dialog.showNow(dialog.mController.getFragmentManager(),"dialog");
        return true;
    }
}
