package com.zhkrb.dialog.messageDialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zhkrb.dialog.R;
import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.BaseDialog;
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
        if (mController.getShowAnimation() == -1) {
            return R.style.MessageAnim;
        }
        return super.getShowAnim();
    }

    @Override
    protected int getLayoutId() {
        if (mController.getLayoutId() == -1) {
            return R.layout.dialog_message;
        }
        return mController.getLayoutId();
    }

    @Override
    protected void bindView(View view) {
        mGroupTitle = view.findViewById(R.id.group_title);
        mTextTitle = view.findViewById(R.id.text_title);
        mTextContent = view.findViewById(R.id.text_content);
        mBtnConfirm = view.findViewById(R.id.btn_confirm);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnAction = view.findViewById(R.id.btn_action);

        if (TextUtils.isEmpty(mController.getTitle())) {
            mGroupTitle.setVisibility(View.GONE);
        } else {
            mGroupTitle.setVisibility(View.VISIBLE);
            mTextTitle.setText(mController.getTitle());
        }
        mTextContent.setText(mController.getContent());

        mBtnConfirm.setOnClickListener(mBtnClickListener);
        mBtnCancel.setOnClickListener(mBtnClickListener);
        mBtnAction.setOnClickListener(mBtnClickListener);

        mBtnCancel.setVisibility(TextUtils.isEmpty(((MessageDialogController) mController).getCancelText()) ? View.GONE : View.VISIBLE);
        mBtnAction.setVisibility(TextUtils.isEmpty(((MessageDialogController) mController).getActionText()) ? View.GONE : View.VISIBLE);
        mBtnConfirm.setText(TextUtils.isEmpty(((MessageDialogController) mController).getConfirmText())
                ? getString(R.string.confirm) :
                ((MessageDialogController) mController).getConfirmText());
        mBtnCancel.setText(((MessageDialogController) mController).getCancelText());
        mBtnAction.setText(((MessageDialogController) mController).getActionText());

        if (((MessageDialogController) mController).getBtnConfirmStyle() != null) {
            ((MessageDialogController) mController).getBtnConfirmStyle().setBtnStyle(mBtnConfirm);
        }
        if (((MessageDialogController) mController).getBtnCancelStyle() != null) {
            ((MessageDialogController) mController).getBtnCancelStyle().setBtnStyle(mBtnCancel);
        }
        if (((MessageDialogController) mController).getBtnActionStyle() != null) {
            ((MessageDialogController) mController).getBtnActionStyle().setBtnStyle(mBtnAction);
        }
    }

    private final View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_confirm) {
                if (((MessageDialogController) mController).getBtnConfirmClickListener() != null) {
                    ((MessageDialogController) mController).getBtnConfirmClickListener().onClick(MessageDialog.this);
                }
            } else if (id == R.id.btn_cancel) {
                ((MessageDialogController) mController).getBtnCancelClickListener().onClick(MessageDialog.this);
            } else if (id == R.id.btn_action) {
                ((MessageDialogController) mController).getBtnActionClickListener().onClick(MessageDialog.this);
            }

            dismiss();
        }
    };

    @Override
    protected void main() {

    }

    @Override
    protected void setWindowAttributes(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout((int) BaseDialog.dp2px(mController.getContext(), 280), WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) BaseDialog.dp2px(mController.getContext(), 280);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = mController.getGravity();
        window.setAttributes(lp);
    }
}
