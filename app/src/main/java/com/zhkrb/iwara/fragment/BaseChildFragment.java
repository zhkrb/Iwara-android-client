package com.zhkrb.iwara.fragment;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhkrb.base.AbsFragment;
import com.zhkrb.dragvideo.widget.AdvSeekbar;
import com.zhkrb.iwara.R;

public class BaseChildFragment extends AbsFragment {

    private TextView mTextView;
    private AdvSeekbar mSeekBar;

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
        mTextView = null;
        mSeekBar = null;
    }

    @Override
    protected void bindView() {
        mTextView = mRootView.findViewById(R.id.text);
        mSeekBar = mRootView.findViewById(R.id.progress);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void realDestroy() {

    }
}
