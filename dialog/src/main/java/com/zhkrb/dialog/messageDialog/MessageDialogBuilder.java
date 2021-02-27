package com.zhkrb.dialog.messageDialog;

import android.content.Context;
import android.widget.TextView;

import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.DialogBuilder;
import com.zhkrb.dialog.base.DialogController;

public class MessageDialogBuilder extends DialogBuilder {



    public MessageDialogBuilder(Context context) {
        super(context);
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
