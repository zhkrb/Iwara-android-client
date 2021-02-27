package com.zhkrb.dialog.messageDialog;

import android.widget.TextView;

import com.zhkrb.dialog.base.DialogController;

public class MessageDialogController extends DialogController {

    private BtnStyleInterface mBtnConfirmStyle;
    private BtnStyleInterface mBtnCancelStyle;
    private BtnStyleInterface mBtnActionStyle;

    public BtnStyleInterface getBtnConfirmStyle() {
        return mBtnConfirmStyle;
    }

    public void setBtnConfirmStyle(BtnStyleInterface btnConfirmStyle) {
        mBtnConfirmStyle = btnConfirmStyle;
    }

    public BtnStyleInterface getBtnCancelStyle() {
        return mBtnCancelStyle;
    }

    public void setBtnCancelStyle(BtnStyleInterface btnCancelStyle) {
        mBtnCancelStyle = btnCancelStyle;
    }

    public BtnStyleInterface getBtnActionStyle() {
        return mBtnActionStyle;
    }

    public void setBtnActionStyle(BtnStyleInterface btnActionStyle) {
        mBtnActionStyle = btnActionStyle;
    }

    static class MessageDialgParams extends DialogController.Params {

        private BtnStyleInterface mBtnConfirmStyle;
        private BtnStyleInterface mBtnCancelStyle;
        private BtnStyleInterface mBtnActionStyle;

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
            ((MessageDialogController)controller).setBtnActionStyle(mBtnActionStyle);
            ((MessageDialogController)controller).setBtnConfirmStyle(mBtnConfirmStyle);
            ((MessageDialogController)controller).setBtnCancelStyle(mBtnCancelStyle);
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
}
