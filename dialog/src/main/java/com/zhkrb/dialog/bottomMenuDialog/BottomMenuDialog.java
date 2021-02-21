package com.zhkrb.dialog.bottomMenuDialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhkrb.dialog.R;
import com.zhkrb.dialog.base.AbsDialog;
import com.zhkrb.dialog.base.BaseDialog;

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

    private ConstraintLayout mParentLayout;
    private RecyclerView mRecyclerView;
    private TextView mTextTitle;

    private MenuBaseAdapter mAdapter;


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
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.anim.bottom_to_top_enter;
        window.setAttributes(lp);

    }

    @Override
    protected void bindView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mTextTitle = view.findViewById(R.id.text_title);
        mParentLayout = view.findViewById(R.id.dialog_parent);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mParentLayout.setY(BaseDialog.getScreenHeight(getActivityContext()));
        mParentLayout.post(new Runnable() {
            @Override
            public void run() {
                mParentLayout.animate().translationY(0).setDuration(333);
            }
        });


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

        if (((BottomMenuDialogController) mController).getSelectInterface() != null) {
            mAdapter.setSelectInterface(((BottomMenuDialogController) mController).getSelectInterface());
        }

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
            return new MenuBaseViewHolder(inflater.inflate(R.layout.dialog_bottom_menu, parent, false));
        }

        @Override
        protected void convert(MenuBaseViewHolder holder, int position) {
            MenuBean bean = mList.get(position);

            if (bean.getIcon() == -1) {
                holder.getRootView().setSelected(false);
                if (bean.isSelect()) {
                    ((ImageView) holder.findView(R.id.img_icon)).setImageResource(R.drawable.ic_icon_item_select);
                } else {
                    ((ImageView) holder.findView(R.id.img_icon)).setImageResource(0);
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
