package com.zhkrb.iwara.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.utils.SpUtil;
import com.zhkrb.iwara.utils.ToastUtil;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private boolean mSafeMode;
    private Handler mHandler;
    private int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mContext = this;
        findViewById(R.id.btn_launcher_1).setOnClickListener(this);
        mSafeMode = SpUtil.getInstance().getBooleanValue(SpUtil.SAFEMODE);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                forward();
            }
        },1000);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_launcher_1:
                if (mSafeMode){
                    return;
                }
                i++;
                if (i==2){
                    ToastUtil.show(R.string.safe_mode_off);
                }
                break;
        }
    }

    private void forward(){
        mContext.startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }

}
