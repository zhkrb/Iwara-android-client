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
 * Create by zhkrb on 2020/3/3 0:13
 */

package com.zhkrb.iwara.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhkrb.dialog.bottomMenuDialog.BottomMenuDialogBuilder;
import com.zhkrb.dragvideo.widget.AdvSeekbar;
import com.zhkrb.iwara.R;

public class TestFragment extends BarBaseFragment implements View.OnClickListener {

    private AdvSeekbar mSeekBar;
    private Button mButton1;
    private Button mButton2;
    private TextView mTextView;


    @Override
    public View getTopAppbar(Context context) {
        return null;
    }

    @Override
    public boolean getNeedFixTop() {
        return false;
    }

    @Override
    protected void main(Bundle savedInstanceState) {
        mButton1 = mRootView.findViewById(R.id.button);
        mButton2 = mRootView.findViewById(R.id.button1);
        mTextView = mRootView.findViewById(R.id.text);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);

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
    public void onDestroyView() {
        mSeekBar = null;
        mButton1 = null;
        mButton2 = null;
        mTextView = null;
        super.onDestroyView();
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

    }

    @Override
    public void realDestroy() {

    }

    private void setSeekBarDelegate() {
//        Rect touchRect = new Rect();
//        mSeekBar.getHitRect(touchRect);
//        touchRect.top -= DpUtil.dp2px(20);
//        touchRect.bottom += DpUtil.dp2px(40);
//        TouchDelegate mSeekTouchDelegate = new TouchDelegate(touchRect,mSeekBar);
//        ((ViewGroup)mSeekBar.getParent()).setTouchDelegate(mSeekTouchDelegate);
        mSeekBar.setEnabled(true);
    }

    private void removeDelegate(){
//        ((ViewGroup)mSeekBar.getParent()).setTouchDelegate(null);
        mSeekBar.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
//                setSeekBarDelegate();
                new BottomMenuDialogBuilder(mContext)
                        .setMenu(new String[]{"1123","2234","4432","2324"},2)
                        .showNow();

                break;
            case R.id.button1:
                removeDelegate();
                break;
            default:
        }
    }
}
