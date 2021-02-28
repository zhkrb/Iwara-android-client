package com.zhkrb.dialog.messageDialog;

import android.widget.TextView;

import com.zhkrb.dialog.R;
import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.DialogController;

import androidx.annotation.NonNull;

public class MessageDialogController extends DialogController {

    private BtnStyleInterface mBtnConfirmStyle;
    private CharSequence mConfirmText;
    private BtnClickListener mBtnConfirmClickListener;
    private BtnStyleInterface mBtnCancelStyle;
    private CharSequence mCancelText;
    private BtnClickListener mBtnCancelClickListener;
    private BtnStyleInterface mBtnActionStyle;
    private CharSequence mActionText;
    private BtnClickListener mBtnActionClickListener;

    public BtnStyleInterface getBtnConfirmStyle() {
        return mBtnConfirmStyle;
    }

    public CharSequence getConfirmText() {
        return mConfirmText;
    }

    public BtnStyleInterface getBtnCancelStyle() {
        return mBtnCancelStyle;
    }

    public CharSequence getCancelText() {
        return mCancelText;
    }


    public BtnStyleInterface getBtnActionStyle() {
        return mBtnActionStyle;
    }

    public void setBtnConfirm(CharSequence text, @NonNull BtnClickListener clickListener) {
        mBtnConfirmClickListener = clickListener;
        mConfirmText = text;
    }

    public void setBtnCancel(CharSequence text, @NonNull BtnClickListener clickListener) {
        mBtnCancelClickListener = clickListener;
        mCancelText = text;
    }

    public void setBtnAction(CharSequence text, @NonNull BtnClickListener clickListener) {
        mBtnActionClickListener = clickListener;
        mActionText = text;
    }

    public void setBtnConfirmStyle(BtnStyleInterface btnConfirmStyle) {
        mBtnConfirmStyle = btnConfirmStyle;
    }

    public void setBtnCancelStyle(BtnStyleInterface btnCancelStyle) {
        mBtnCancelStyle = btnCancelStyle;
    }

    public void setBtnActionStyle(BtnStyleInterface btnActionStyle) {
        mBtnActionStyle = btnActionStyle;
    }

    public CharSequence getActionText() {
        return mActionText;
    }

    public BtnClickListener getBtnConfirmClickListener() {
        return mBtnConfirmClickListener;
    }

    public BtnClickListener getBtnCancelClickListener() {
        return mBtnCancelClickListener;
    }

    public BtnClickListener getBtnActionClickListener() {
        return mBtnActionClickListener;
    }

    static class MessageDialgParams extends DialogController.Params {

        private BtnStyleInterface mBtnConfirmStyle;
        private CharSequence mConfirmText = "";
        private BtnClickListener mBtnConfirmClickListener;
        private BtnStyleInterface mBtnCancelStyle;
        private CharSequence mCancelText = "";
        private BtnClickListener mBtnCancelClickListener;
        private BtnStyleInterface mBtnActionStyle;
        private CharSequence mActionText = "";
        private BtnClickListener mBtnActionClickListener;

        public void setBtnConfirm(CharSequence text, @NonNull BtnClickListener clickListener) {
            mBtnConfirmClickListener = clickListener;
            mConfirmText = text;
        }

        public void setBtnCancel(CharSequence text, @NonNull BtnClickListener clickListener) {
            mBtnCancelClickListener = clickListener;
            mCancelText = text;
        }

        public void setBtnAction(CharSequence text, @NonNull BtnClickListener clickListener) {
            mBtnActionClickListener = clickListener;
            mActionText = text;
        }

        public void setBtnConfirmStyle(BtnStyleInterface btnConfirmStyle) {
            mBtnConfirmStyle = btnConfirmStyle;
        }

        public void setBtnCancelStyle(BtnStyleInterface btnCancelStyle) {
            mBtnCancelStyle = btnCancelStyle;
        }

        public void setBtnActionStyle(BtnStyleInterface btnActionStyle) {
            mBtnActionStyle = btnActionStyle;
        }

        @Override
        public void apply(DialogController controller) {
            super.apply(controller);
            ((MessageDialogController) controller).setBtnConfirm(mConfirmText, mBtnConfirmClickListener);
            ((MessageDialogController) controller).setBtnCancel(mCancelText, mBtnCancelClickListener);
            ((MessageDialogController) controller).setBtnAction(mActionText, mBtnActionClickListener);
            ((MessageDialogController) controller).setBtnConfirmStyle(mBtnConfirmStyle);
            ((MessageDialogController) controller).setBtnCancelStyle(mBtnCancelStyle);
            ((MessageDialogController) controller).setBtnActionStyle(mBtnActionStyle);
        }
    }

    public interface BtnStyleInterface {

        /**
         * 设置textView样式
         *
         * @param textView
         */
        void setBtnStyle(TextView textView);
    }

    public interface BtnClickListener{

        /**
         * 点击事件
         * @param dialog
         */
        void onClick(AbsDialog dialog);
    }
}
