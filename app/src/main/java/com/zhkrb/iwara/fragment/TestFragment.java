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
 * Create by zhkrb on 2019/10/4 10:52
 */

package com.zhkrb.iwara.fragment;

import android.os.Bundle;
import android.view.View;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.base.AbsFragment;

public class TestFragment extends AbsFragment implements View.OnClickListener {



    @Override
    protected void main(Bundle savedInstanceState) {
        mRootView.findViewById(R.id.btn_top).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_stand).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_task).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_main;
    }

    @Override
    public void onNewArguments(Bundle args) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_stand:

                break;
            case R.id.btn_task:

                break;

            case R.id.btn_top:

                break;
        }
    }
}
