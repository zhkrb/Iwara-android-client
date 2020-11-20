package com.zhkrb.custom.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.qiyou.id.R;
import com.qiyou.id.custom.dialog.dialogManager.DialogShowManager;
import com.qiyou.id.utils.ClickUtils;
import com.qiyou.id.utils.DpUtils;

import androidx.cardview.widget.CardView;

/**
 * @description：建造dialog
 * @author：zhkrb
 * @DATE： 2020/7/6 13:55
 */
public class AbsDialog extends BaseDialog implements View.OnClickListener {

    public DialogController mController;
    private EditText mEditText;
    private DestroyListener mDestroyListener;

    public AbsDialog() {
        mController = new DialogController();
    }

    @Override
    protected void main() {
        setCancelable(mController.isCancel());
        TextView confirmBtn = mRootView.findViewById(R.id.btn_confirm);
        TextView cancelBtn = mRootView.findViewById(R.id.btn_cancel);
        TextView title = mRootView.findViewById(R.id.text_title);
        TextView content = mRootView.findViewById(R.id.text_content);
        CardView cardView = mRootView.findViewById(R.id.parent_card);
        mEditText = mRootView.findViewById(R.id.edit_text);
        if (confirmBtn != null){
            confirmBtn.setOnClickListener(this);
        }
        if (cancelBtn != null){
            cancelBtn.setOnClickListener(this);
        }
        if (title != null){
            title.setText(mController.getTitle());
        }
        if (content != null){
            content.setText(mController.getContent());
        }
        if (mEditText != null){
            mEditText.setHint(mController.getEditHintText());
        }
        if (cardView != null){
            cardView.setRadius(mController.isCornerRadius()
                    ? DpUtils.dp2px(mController.getRadius())
                    : 0);
        }
        if (mController.isCustomClick()){
            mController.setClick(mRootView,this);
        }
    }

    @Override
    protected void setWindowAttributes(Window window) {
        if (mController.isCustomWindow()){
            mController.setWindowAttributes(window);
        }else {
            WindowManager.LayoutParams params = window.getAttributes();

            int screenWidth = DpUtils.getScreenWidth();
            params.width = (int) (screenWidth * mController.getWidthRate());
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            if (mController.getBackgroundAlphaRate()){
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }else {
                ColorDrawable drawable = new ColorDrawable(Color.GRAY);
                drawable.setAlpha((int) (255 * mController.getBackgroundAlpha()));
                window.setBackgroundDrawable(drawable);
            }
            window.setAttributes(params);
        }

    }

    @Override
    protected boolean isCanCancelOnTouchOutside() {
        return mController.isCancelOnTouchOutside();
    }

    @Override
    protected boolean isCanCancel() {
        return mController.isCancel();
    }

    @Override
    protected int getLayoutId() {
        return mController.getLayoutId();
    }

    @Override
    protected int getDialogStyle() {
        return mController.getStyle();
    }

    @Override
    protected Context getActivityContext() {
        return mController.getContext();
    }


    @Override
    public void onClick(View v) {
        if (!ClickUtils.canClick()){
            return;
        }
        switch (v.getId()){
            case R.id.btn_confirm:
                if (mController.getClickInterface() != null){
                    if (mEditText != null && mController.getClickInterface() instanceof InputDialogInterface){
                        String str = TextUtils.isEmpty(mEditText.getText()) ? null : mEditText.getText().toString();
                        ((InputDialogInterface) mController.getClickInterface()).confirmClick(str);
                        dismiss();
                        return;
                    }
                    mController.getClickInterface().confirmClick();
                }
                dismiss();
                break;
            case R.id.btn_cancel :
                if (mController.getClickInterface() != null){
                    mController.getClickInterface().cancelClick();
                }
                dismiss();
                break;
            default:

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEditText = null;
        mController = null;
        if (mDestroyListener != null){
            mDestroyListener.onDestroy();
        }
        DialogShowManager.getInstance().showOff();
    }

    public void setDestroyListener(DestroyListener destroyListener) {
        mDestroyListener = destroyListener;
    }

    /**
     * dialog销毁监听
     */
    public interface DestroyListener{

        /**
         * 销毁
         */
        void onDestroy();

    }


}
