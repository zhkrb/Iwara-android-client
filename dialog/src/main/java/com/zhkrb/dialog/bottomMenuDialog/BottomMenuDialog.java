package com.zhkrb.dialog.bottomMenuDialog;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhkrb.dialog.R;
import com.zhkrb.dialog.base.AbsDialog;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/2/13 23:37
 */
public class BottomMenuDialog extends AbsDialog {

    private RecyclerView mRecyclerView;
    private TextView mTextTitle;


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
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mTextTitle = view.findViewById(R.id.text_title);

    }

    @Override
    protected void main() {
        if (!TextUtils.isEmpty(mController.getTitle())){
            mTextTitle.setVisibility(View.VISIBLE);
            mTextTitle.setText(mController.getTitle());
        }else {
            mTextTitle.setVisibility(View.GONE);
        }




    }


}
