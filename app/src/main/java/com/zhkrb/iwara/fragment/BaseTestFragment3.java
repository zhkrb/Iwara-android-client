package com.zhkrb.iwara.fragment;

import android.os.Bundle;
import android.view.View;

import com.zhkrb.base.AbsFragment;
import com.zhkrb.base.FragmentFrame;
import com.zhkrb.iwara.R;

public class BaseTestFragment3 extends AbsFragment {
    @Override
    protected void main(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void onNewArguments(Bundle args) {

    }

    @Override
    protected void destroyView() {

    }

    @Override
    protected void bindView() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(new FragmentFrame(BaseChildFragment.class));
            }
        });
    }

    @Override
    public void realDestroy() {

    }
}
