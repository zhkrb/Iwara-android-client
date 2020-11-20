package com.zhkrb.custom.dialog.dialogManager;


import com.qiyou.id.custom.dialog.DialogBuilder;

/**
 * @description：builder包装类
 * @author：zhkrb
 * @DATE： 2020/7/6 16:50
 */
public class DialogWrapper {

    private DialogBuilder mBuilder;

    public DialogWrapper(DialogBuilder builder) {
        mBuilder = builder;
    }

    public DialogBuilder getBuilder() {
        return mBuilder;
    }

    public void setBuilder(DialogBuilder builder) {
        mBuilder = builder;
    }
}
