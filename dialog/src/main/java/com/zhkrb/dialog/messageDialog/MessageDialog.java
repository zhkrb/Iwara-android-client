package com.zhkrb.dialog.messageDialog;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.zhkrb.dialog.R;
import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.DialogController;

import androidx.constraintlayout.widget.Group;

public class MessageDialog extends AbsDialog {

    private Group mGroupTitle;
    private TextView mTextTitle;
    private TextView mTextContent;
    private MaterialButton mBtnConfirm;
    private MaterialButton mBtnCancel;
    private MaterialButton mBtnAction;


    @Override
    protected DialogController createController() {
        return new MessageDialogController();
    }

    @Override
    protected int getShowAnim() {
        if (mController.getLayoutId() == -1) {
            return R.layout.dialog_message;
        }
        return super.getLayoutId();
    }

    @Override
    protected void bindView(View view) {
        mGroupTitle = view.findViewById(R.id.group_title);
        mTextTitle = view.findViewById(R.id.text_title);
        mTextContent = view.findViewById(R.id.text_content);
        mBtnConfirm = view.findViewById(R.id.btn_confirm);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnAction = view.findViewById(R.id.btn_action);

        if (TextUtils.isEmpty(mController.getTitle())){
            mGroupTitle.setVisibility(View.GONE);
        }else {
            mGroupTitle.setVisibility(View.VISIBLE);
            mTextTitle.setText(mController.getTitle());
        }
        mTextContent.setText(mController.getContent());

        mBtnConfirm.setOnClickListener();
        mBtnCancel.setOnClickListener();
        mBtnAction.setOnClickListener();

        if (((MessageDialogController)mController).getBtnConfirmStyle() != null){
            ((MessageDialogController) mController).getBtnConfirmStyle().setBtnStyle(mBtnConfirm);
        }
        if (((MessageDialogController)mController).getBtnCancelStyle() != null){
            ((MessageDialogController) mController).getBtnCancelStyle().setBtnStyle(mBtnCancel);
        }
        if (((MessageDialogController)mController).getBtnConfirmStyle() != null){
            ((MessageDialogController) mController).getBtnActionStyle().setBtnStyle(mBtnAction);
        }



    }



    @Override
    protected void main() {

    }

    @Override
    protected void setWindowAttributes(Dialog dialog) {

    }
}
