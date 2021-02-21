package com.zhkrb.dialog.bottomMenuDialog;

import android.content.Context;

import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.DialogBuilder;
import com.zhkrb.dialog.base.DialogController;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/2/13 23:36
 */
public class BottomMenuDialogBuilder extends DialogBuilder {

    public BottomMenuDialogBuilder(Context context) {
        super(context);
    }

    @Override
    protected DialogController.Params createParams() {
        return new BottomMenuDialogController.BottomMenuParams();
    }

    @Override
    protected AbsDialog createDialog() {
        return new BottomMenuDialog();
    }

    public BottomMenuDialogBuilder setSelectInterface(MenuBaseAdapter.MenuSelectInterface selectInterface) {
        ((BottomMenuDialogController.BottomMenuParams)mControllerParams).setSelectInterface(selectInterface);
        return this;
    }

    public BottomMenuDialogBuilder setCustomAdapter(MenuBaseAdapter customAdapter) {
        ((BottomMenuDialogController.BottomMenuParams)mControllerParams).setCustomAdapter(customAdapter);
        return this;
    }

    /**
     * 设置选择项
     * @param list
     * @return
     */
    public BottomMenuDialogBuilder setMenu(String[] list){
        return setMenu(list,-1);
    }

    /**
     * 设置选择项
     * @param list
     * @param currentSelct
     * @return
     */
    public BottomMenuDialogBuilder setMenu(String[] list,int currentSelct){
        List<MenuBean> beans = new ArrayList<>();
        int i = 0;
        for (String str : list){
            MenuBean bean = new MenuBean();
            bean.setTitle(str);
            if (i == currentSelct){
                bean.setSelect(true);
            }
            i++;
        }
        ((BottomMenuDialogController.BottomMenuParams)mControllerParams).setMenuBeanList(beans);
        return this;
    }

    /**
     * 设置选择项
     * @param beanList
     * @return
     */
    public BottomMenuDialogBuilder setMenu(List<MenuBean> beanList){
        ((BottomMenuDialogController.BottomMenuParams)mControllerParams).setMenuBeanList(beanList);
        return this;
    }



}
