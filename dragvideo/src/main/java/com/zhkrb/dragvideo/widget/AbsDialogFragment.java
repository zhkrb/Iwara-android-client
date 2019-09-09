//Copyright zhkrb
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

//Create by zhkrb on 2019/9/7 22:12

package com.zhkrb.dragvideo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public abstract class AbsDialogFragment extends DialogFragment {

    protected Context mContext;
    protected View mRootView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        Dialog dialog = new Dialog(mContext,getDialogStyle());
        mRootView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        dialog.setContentView(mRootView);
        dialog.setCancelable(canCancel());
        dialog.setCanceledOnTouchOutside(canCancel());
        setWindowAttributes(dialog.getWindow());
        return dialog;
    }

    protected abstract void setWindowAttributes(Window window);

    protected abstract boolean canCancel();

    protected abstract int getDialogStyle();

    protected abstract int getLayoutId();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main();
    }

    protected abstract void main();
}
