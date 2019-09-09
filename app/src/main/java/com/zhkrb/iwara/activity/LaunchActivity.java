/*
 * Copyright zhkrb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Create by zhkrb on 2019/9/7 22:12
 */

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
