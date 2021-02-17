package com.zhkrb.dialog;

import com.zhkrb.dialog.base.BaseDialogInterface;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/7/6 16:16
 */
public abstract class InputDialogInterface implements BaseDialogInterface {

    @Override
    public boolean confirmClick(){
        return false;
    }

    /**
     * 点击确认 获取输入字符
     * @param str 字符串
     */
    abstract boolean confirmClick(String str);

}
