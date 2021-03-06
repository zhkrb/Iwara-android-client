package com.zhkrb.iwara.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhkrb.base.AbsFragment;
import com.zhkrb.dragvideo.widget.AdvSeekbar;
import com.zhkrb.iwara.R;
import com.zhkrb.netowrk.jsoup.BaseJsoupCallback;
import com.zhkrb.netowrk.jsoup.JsoupUtil;
import com.zhkrb.utils.L;

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
        mRootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupUtil.getVideoInfo("/videos/wqlwatgmvhqg40kg", "tag", new BaseJsoupCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(int code, String msg, Object info) {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                });
            }
        });
    }

    @Override
    public void realDestroy() {

    }
}
