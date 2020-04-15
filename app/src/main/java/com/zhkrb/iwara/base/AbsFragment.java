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

package com.zhkrb.iwara.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class AbsFragment extends Fragment {

    public static final int LAUNCH_MODE_BASE = 0;
    public static final int LAUNCH_MODE_PAGE = 1;

    /** Standard scene result: operation canceled. */
    public static final int RESULT_CANCELED  = 0;
    /** Standard scene result: operation succeeded. */
    public static final int RESULT_OK = -1;
    private List<Integer> mRequestCodeList = new ArrayList<>(0);
    private List<String> mRequestTagList = new ArrayList<>(0);

    int resultCode = RESULT_CANCELED;
    Bundle result = null;

    protected Context mContext;
    protected View mRootView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = LayoutInflater.from(getContext()).inflate(getLayoutId(),container,false);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;
    }

    protected abstract void main(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    public abstract void onNewArguments(Bundle args);

    public void addRequest(String tag, int requestCode) {
        mRequestTagList.add(tag);
        mRequestCodeList.add(requestCode);
    }

    public void returnResult(AbsActivity absActivity) {
        for (int i = 0, size = Math.min(mRequestCodeList.size(),mRequestTagList.size());i < size; i++){
            String tag = mRequestTagList.get(i);
            int code = mRequestCodeList.get(i);
            AbsFragment fragment = absActivity.findFragmentbyTag(tag);
            if (fragment!=null){
                fragment.onFragmentResult(code,resultCode,result);
            }
        }
        mRequestCodeList.clear();
        mRequestTagList.clear();
    }

    public void setResult(int resultCode, Bundle result){
        this.resultCode = resultCode;
        this.result = result;
    }

    protected void onFragmentResult(int code, int resultCode, Bundle result) {

    }

    public void startFragment(FragmentFrame frame){
        ((AbsActivity)mContext).startFragment(frame);
    }

    public void onBackPressed(){
        finish();
    }

    protected void finish() {
        finish(null);
    }

    protected void finish(TransitionHelper helper){
        if (mContext instanceof AbsActivity){
            ((AbsActivity) mContext).finish(this,helper);
            if (((AbsActivity) mContext).mFragmentStack.size() > 1){
                mContext = null;
            }
        }
    }



    @IntDef({
            LAUNCH_MODE_BASE,
            LAUNCH_MODE_PAGE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LaunchMode{}





}
