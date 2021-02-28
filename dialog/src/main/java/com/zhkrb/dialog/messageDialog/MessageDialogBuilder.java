package com.zhkrb.dialog.messageDialog;

import android.content.Context;
import android.widget.TextView;

import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.DialogBuilder;
import com.zhkrb.dialog.base.DialogController;

import androidx.annotation.NonNull;

public class MessageDialogBuilder extends DialogBuilder {



    public MessageDialogBuilder(Context context) {
        super(context);
    }

    public MessageDialogBuilder setBtnConfirm(CharSequence text, @NonNull MessageDialogController.BtnClickListener clickListener) {
        ((MessageDialogController.MessageDialgParams)mControllerParams).setBtnConfirm(text,clickListener);
        return this;
    }

    public MessageDialogBuilder setBtnCancel(CharSequence text, @NonNull MessageDialogController.BtnClickListener clickListener) {
        ((MessageDialogController.MessageDialgParams)mControllerParams).setBtnCancel(text,clickListener);
        return this;
    }

    public MessageDialogBuilder setBtnAction(CharSequence text, @NonNull MessageDialogController.BtnClickListener clickListener) {
        ((MessageDialogController.MessageDialgParams)mControllerParams).setBtnAction(text,clickListener);
        return this;
    }

    public MessageDialogBuilder setBtnConfirmStyle(MessageDialogController.BtnStyleInterface btnConfirmStyle) {
        ((MessageDialogController.MessageDialgParams)mControllerParams).setBtnConfirmStyle(btnConfirmStyle);
        return this;
    }

    public MessageDialogBuilder setBtnCancelStyle(MessageDialogController.BtnStyleInterface btnCancelStyle) {
        ((MessageDialogController.MessageDialgParams)mControllerParams).setBtnConfirmStyle(btnCancelStyle);
        return this;
    }

    public MessageDialogBuilder setBtnActionStyle(MessageDialogController.BtnStyleInterface btnActionStyle) {
        ((MessageDialogController.MessageDialgParams)mControllerParams).setBtnConfirmStyle(btnActionStyle);
        return this;
    }

    @Override
    protected DialogController.Params createParams() {
        return new MessageDialogController.MessageDialgParams();
    }

    @Override
    protected AbsDialog createDialog() {
        return new MessageDialog();
    }




}
