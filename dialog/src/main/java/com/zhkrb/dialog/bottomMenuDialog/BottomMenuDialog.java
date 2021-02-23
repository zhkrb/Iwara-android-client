package com.zhkrb.dialog.bottomMenuDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhkrb.dialog.R;
import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.BaseDialog;
import com.zhkrb.dialog.base.DialogController;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/2/13 23:37
 */
public class BottomMenuDialog extends AbsDialog {

    private ViewGroup mParentLayout;
    private RecyclerView mRecyclerView;
    private TextView mTextTitle;

    private MenuBaseAdapter mAdapter;


    @Override
    protected DialogController createController() {
        return new BottomMenuDialogController();
    }

    @Override
    protected int getLayoutId() {
        if (mController.getLayoutId() == -1) {
            return R.layout.dialog_bottom_menu;
        }
        return super.getLayoutId();
    }

    @Override
    protected void setWindowAttributes(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.dialog_null_anim;
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(lp);

    }

    @Override
    protected void bindView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mTextTitle = view.findViewById(R.id.text_title);
        mParentLayout = (ViewGroup) view.findViewById(R.id.dialog_parent);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

//        mParentLayout.setY(BaseDialog.getScreenHeight(getActivityContext()));
//        mParentLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mParentLayout.animate().translationY(0).setDuration(333);
//            }
//        });
        main();
    }

    @Override
    protected void main() {
        if (!TextUtils.isEmpty(mController.getTitle())) {
            mTextTitle.setVisibility(View.VISIBLE);
            mTextTitle.setText(mController.getTitle());
        } else {
            mTextTitle.setVisibility(View.GONE);
        }

        if (((BottomMenuDialogController) mController).getCustomAdapter() != null) {
            mAdapter = ((BottomMenuDialogController) mController).getCustomAdapter();
        } else {
            mAdapter = new MenuAdapter(getActivityContext());
        }
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setSelectInterface(new MenuBaseAdapter.MenuSelectInterface() {
            @Override
            public void onMenuSelect(Object data, int pos) {
                if (((BottomMenuDialogController) mController).getSelectInterface() != null) {
                    ((BottomMenuDialogController) mController).getSelectInterface().onMenuSelect(data,pos);
                }
                dismiss();
            }
        });

        if (((BottomMenuDialogController) mController).getMenuBeanList() != null) {
            mAdapter.setList(((BottomMenuDialogController) mController).getMenuBeanList());
        }

    }


    private static class MenuAdapter extends MenuBaseAdapter<MenuBean> {


        public MenuAdapter(Context context) {
            super(context);
        }

        @Override
        public MenuBaseViewHolder createHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return new MenuBaseViewHolder(inflater.inflate(R.layout.item_setting, parent, false));
        }

        @Override
        protected void convert(MenuBaseViewHolder holder, int position) {
            MenuBean bean = mList.get(position);

            if (bean.getIcon() == -1) {
                holder.getRootView().setSelected(false);
                if (bean.isSelect()) {
                    ((ImageView) holder.findView(R.id.img_icon)).setImageResource(R.drawable.ic_icon_item_select);
                } else {
                    ((ImageView) holder.findView(R.id.img_icon)).setImageDrawable(null);
                }
            } else {
                ((ImageView) holder.findView(R.id.img_icon)).setImageResource(bean.getIcon());
                holder.getRootView().setSelected(bean.isSelect());
            }

            ((TextView) holder.findView(R.id.text_title)).setText(bean.getTitle());

            holder.findView(R.id.text_point).setVisibility(TextUtils.isEmpty(bean.getValue()) ? View.GONE : View.VISIBLE);
            holder.findView(R.id.text_value).setVisibility(TextUtils.isEmpty(bean.getValue()) ? View.GONE : View.VISIBLE);

            ((TextView) holder.findView(R.id.text_value)).setText(bean.getValue());
        }
    }

}
