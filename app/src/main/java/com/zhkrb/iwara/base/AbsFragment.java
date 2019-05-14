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
import androidx.fragment.app.FragmentActivity;

public abstract class AbsFragment extends Fragment {

    public static final int LAUNCH_MODE_STANDARD = 0;
    public static final int LAUNCH_MODE_SINGLE_TOP = 1;
    public static final int LAUNCH_MODE_SINGLE_TASK = 2;

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
        mRootView = inflater.inflate(getLayoutId(),container,false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main();
    }

    protected abstract void main();

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

    public void startFragment(FragmentFrame frame,@LaunchMode int mode){
        ((AbsActivity)mContext).startFragment(frame,mode);
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
        }
    }

    @IntDef({
            LAUNCH_MODE_STANDARD,
            LAUNCH_MODE_SINGLE_TOP,
            LAUNCH_MODE_SINGLE_TASK
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LaunchMode{}





}
