package com.zhkrb.dialog.bottomMenuDialog;

import android.content.Context;

import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.DialogBuilder;
import com.zhkrb.dialog.base.DialogController;

import androidx.fragment.app.FragmentManager;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/2/13 23:36
 */
public class BottomMenuDialogBuilder extends DialogBuilder {

    public BottomMenuDialogBuilder(Context context, FragmentManager fragmentManager) {
        super(context, fragmentManager);
    }

    @Override
    public DialogController.Params createParams() {
        return new BottomMenuDialogController.BottomMenuParams();
    }

    @Override
    public AbsDialog createDialog() {
        return null;
    }


}
