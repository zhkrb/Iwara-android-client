package com.zhkrb.dialog.bottomMenuDialog;

import com.zhkrb.dialog.base.DialogController;

import java.util.List;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/2/13 23:37
 */
public class BottomMenuDialogController extends DialogController {

    private MenuBaseAdapter mCustomAdapter;

    private MenuBaseAdapter.MenuSelectInterface mSelectInterface;

    private List<MenuBean> mMenuBeanList;

    public MenuBaseAdapter getCustomAdapter() {
        return mCustomAdapter;
    }

    public void setCustomAdapter(MenuBaseAdapter customAdapter) {
        mCustomAdapter = customAdapter;
    }

    public MenuBaseAdapter.MenuSelectInterface getSelectInterface() {
        return mSelectInterface;
    }

    public void setSelectInterface(MenuBaseAdapter.MenuSelectInterface selectInterface) {
        mSelectInterface = selectInterface;
    }

    public List<MenuBean> getMenuBeanList() {
        return mMenuBeanList;
    }

    public void setMenuBeanList(List<MenuBean> menuBeanList) {
        mMenuBeanList = menuBeanList;
    }

    public static class BottomMenuParams extends Params{

        private MenuBaseAdapter mCustomAdapter = null;
        private MenuBaseAdapter.MenuSelectInterface mSelectInterface = null;
        private List<MenuBean> mMenuBeanList = null;

        public void setCustomAdapter(MenuBaseAdapter customAdapter) {
            mCustomAdapter = customAdapter;
        }

        public void setSelectInterface(MenuBaseAdapter.MenuSelectInterface selectInterface) {
            mSelectInterface = selectInterface;
        }

        public void setMenuBeanList(List<MenuBean> menuBeanList) {
            mMenuBeanList = menuBeanList;
        }

        @Override
        public void apply(DialogController controller) {
            super.apply(controller);
            ((BottomMenuDialogController)controller) .setCustomAdapter(mCustomAdapter);
            ((BottomMenuDialogController)controller) .setSelectInterface(mSelectInterface);
            ((BottomMenuDialogController)controller) .setMenuBeanList(mMenuBeanList);
        }


    }

}
