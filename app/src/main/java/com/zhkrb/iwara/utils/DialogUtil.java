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
 * Create by zhkrb on 2019/9/11 22:09
 */

package com.zhkrb.iwara.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.zhkrb.iwara.R;

import io.reactivex.annotations.NonNull;

public class DialogUtil {




    public static class builder{

        private Context mContext;
        private boolean mCanCancelable;
        private String mContent;
        private String mCancelString;
        private String mConfrimString;
        private String mTitle;
        private boolean mBackgrouondDimEnable; //黑色半透明背景遮罩
        private boolean isTextInput;    //是否有输入框
        private String mHint;
        private String mErrorHint;
        private int mInputType; //输入类型限制 0 无 1数字 2数字密码 3字符密码
        private int mTextInputLength;
        private ClickCallback mClickCallback;

        public builder(@NonNull Context context) {
            mContext = context;
        }

        public builder setCanCancelable(boolean cancelable){
            mCanCancelable = cancelable;
            return this;
        }

        public builder setContent(String content) {
            mContent = content;
            return this;
        }

        public builder setCancelString(String cancelString) {
            mCancelString = cancelString;
            return this;
        }

        public builder setConfrimString(String confrimString) {
            mConfrimString = confrimString;
            return this;
        }

        public builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public builder setBackgrouondDimEnable(boolean backgrouondDimEnable) {
            mBackgrouondDimEnable = backgrouondDimEnable;
            return this;
        }

        public builder setTextInput(boolean textInput) {
            isTextInput = textInput;
            return this;
        }

        public builder setHint(String hint) {
            mHint = hint;
            return this;
        }

        public builder setErrorHint(String errorHint) {
            mErrorHint = errorHint;
            return this;
        }

        public builder setInputType(int inputType) {
            mInputType = inputType;
            return this;
        }

        public builder setTextInputLength(int textInputLength) {
            mTextInputLength = textInputLength;
            return this;
        }

        public builder setClickCallback(ClickCallback clickCallback) {
            mClickCallback = clickCallback;
            return this;
        }
//        public DialogFragment build(){
//            DialogFragment fragment = new DialogFragment();
//            fragment.setCancelable(mCanCancelable);
//
//
//
//
//
//        }


        public interface ClickCallback{
            void onComfrimClick(DialogFragment dialogFragment,String inContent);
        }

        public interface ClickCallback2 extends ClickCallback{
            void onCancelClick();
        }

    }

}
