package com.zhkrb.iwara.activity;

import com.zhkrb.base.AbsActivity;
import com.zhkrb.base.FragmentFrame;
import com.zhkrb.iwara.R;

public class TextActivity extends AbsActivity {


    @Override
    protected FragmentFrame getLaunchFrame() {

        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getContentView() {
        return R.id.content;
    }
}
