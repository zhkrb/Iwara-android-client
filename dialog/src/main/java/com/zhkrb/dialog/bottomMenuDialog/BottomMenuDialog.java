package com.zhkrb.dialog.bottomMenuDialog;

import android.app.Dialog;
import android.view.View;

import com.zhkrb.dialog.R;
import com.zhkrb.dialog.base.AbsDialog;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/2/13 23:37
 */
public class BottomMenuDialog extends AbsDialog {


    @Override
    protected int getLayoutId() {
        if (mController.getLayoutId() == -1){
            return R.layout.dialog_bottom_menu;
        }
        return super.getLayoutId();
    }

    @Override
    protected void setWindowStyle(Dialog dialog) {

    }

    @Override
    protected void setWindowAttributes(Dialog dialog) {

    }

    @Override
    protected void bindView(View view) {

    }

    @Override
    protected void main() {

    }


}
