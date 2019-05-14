package com.zhkrb.iwara.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.WindowManager;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.utils.ContextLocalWrapper;
import com.zhkrb.iwara.utils.L;
import com.zhkrb.iwara.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class AbsActivity extends AppCompatActivity {

    protected Context mContext;
    private List<String> mFragmentStack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        mFragmentStack = new ArrayList<>();
        main(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    protected abstract int getLayoutId();

    protected void main(Bundle savedInstanceState) {
        main();
    }

    protected void main() {
    }

    protected void loadRootFragment(FragmentFrame frame){
        loadRootFragment(frame,AbsFragment.LAUNCH_MODE_STANDARD);
    }

    protected void loadRootFragment(FragmentFrame frame, @AbsFragment.LaunchMode int launchMode){
        FragmentManager manager = getSupportFragmentManager();
        Class clazz = frame.getClazz();
        Bundle args = frame.getArgs();

        if (manager.getBackStackEntryCount()>0){
            manager.popBackStack(null,1);
        }
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Fragment fragment = createFragment(clazz);
        if (fragment==null){
            return;
        }
        if (args!=null){
            fragment.setArguments(args);
        }
        fragmentTransaction.replace(getContentView(),fragment,clazz.getName());
        mFragmentStack.add(clazz.getName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void startFragment(FragmentFrame frame, @AbsFragment.LaunchMode int launchMode){
        FragmentManager manager = getSupportFragmentManager();
        Class clazz = frame.getClazz();
        Bundle args = frame.getArgs();
        TransitionHelper helper = frame.getHelper();
        int stackSize = mFragmentStack.size();

        if (launchMode==AbsFragment.LAUNCH_MODE_SINGLE_TOP&&stackSize>1){
            Fragment topFragment = manager.findFragmentByTag(mFragmentStack.get(stackSize-1));
            if (topFragment!=null&&topFragment.getClass().getName().equals(clazz.getName())){
                ((AbsFragment)topFragment).onNewArguments(args);
                return;
            }
        }

        if (launchMode==AbsFragment.LAUNCH_MODE_SINGLE_TASK&&stackSize>1){
            manager.popBackStack(mFragmentStack.get(0),1);
            mFragmentStack.subList(0,stackSize-1).clear();
            stackSize = 1;
        }

        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        AbsFragment fragment = createFragment(clazz);
        if (fragment==null){
            return;
        }
        if (args!=null){
            fragment.setArguments(args);
        }
        Fragment topFrag = manager.findFragmentByTag(mFragmentStack.get(stackSize-1));
        if (topFrag!=null){
            if (helper==null||helper.onTransition(mContext,fragmentTransaction,fragment,topFrag)){
                fragment.setEnterTransition(null);
                fragment.setExitTransition(null);
                fragment.setSharedElementReturnTransition(null);
                fragment.setSharedElementEnterTransition(null);
                topFrag.setEnterTransition(null);
                topFrag.setExitTransition(null);
                topFrag.setSharedElementReturnTransition(null);
                topFrag.setSharedElementEnterTransition(null);

                fragmentTransaction.setCustomAnimations(R.anim.bottomtop_enter,R.anim.bottomtop_exit);
            }
            fragmentTransaction.hide(topFrag);
        }
        fragmentTransaction.add(getContentView(),fragment,clazz.getName());
        fragmentTransaction.addToBackStack(clazz.getName());
        fragmentTransaction.commitAllowingStateLoss();

        mFragmentStack.add(clazz.getName());

        if (frame.getRequestFragment()!=null){
            fragment.addRequest(frame.getRequestFragment().getClass().getName(),frame.getRequestCode());
        }
    }


    @Override
    public void onBackPressed() {
        int size = mFragmentStack.size();
        if (size==0){
            super.onBackPressed();
            return;
        }

        String tag = mFragmentStack.get(mFragmentStack.size()-1);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment==null){
            L.e("can't find finish fragment tag: "+ tag);
            return;
        }
        if (!(fragment instanceof AbsFragment)){
            L.e("can't cash to AbsFragment: ");
            return;
        }
        ((AbsFragment) fragment).onBackPressed();
    }

    protected abstract int getContentView();

    private AbsFragment createFragment(Class<?> clazz) {
        try {
            return (AbsFragment) clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SpUtil.getInstance().getBooleanValue(SpUtil.SECURITY_ENABLE)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale locale = null;
        String lang = SpUtil.getInstance().getStringValue(SpUtil.LANGUAGE);
        if (!TextUtils.isEmpty(lang)&&!lang.equals("default")){
            locale = new Locale(lang);
        }
        if (locale!=null){
            newBase = ContextLocalWrapper.wrap(newBase,locale);
        }
        super.attachBaseContext(newBase);
    }

    public void finish(AbsFragment fragment, TransitionHelper helper){
        finish(fragment.getTag(),helper);
    }

    public void finish(String tag, TransitionHelper transitionHelper) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment==null){
            L.e("can't find fragment :"+tag);
            return;
        }

        int fragmentIndex = mFragmentStack.indexOf(tag);
        if (fragmentIndex<0){
            L.e("can't find fragment tag :"+tag);
            return;
        }

        if (mFragmentStack.size()==1){
            L.i("finish activity");
            finish();
            return;
        }

        Fragment next = null;
        if (fragmentIndex == mFragmentStack.size() - 1) {
            // It is first fragment, show the next one
            next = fragmentManager.findFragmentByTag(mFragmentStack.get(fragmentIndex - 1));
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (next != null) {
            if (transitionHelper == null || !transitionHelper.onTransition(
                    this, transaction, fragment, next)) {
                // Clear shared item
                fragment.setSharedElementEnterTransition(null);
                fragment.setSharedElementReturnTransition(null);
                fragment.setEnterTransition(null);
                fragment.setExitTransition(null);
                next.setSharedElementEnterTransition(null);
                next.setSharedElementReturnTransition(null);
                next.setEnterTransition(null);
                next.setExitTransition(null);
                // Do not show animate if it is not the first fragment
                transaction.setCustomAnimations(R.anim.bottomtop_enter,
                        R.anim.bottomtop_exit);
            }
            // Attach fragment
            transaction.show(next);
        }
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();

        // Remove tag
        mFragmentStack.remove(fragmentIndex);

        // Return result
        if (fragment instanceof AbsFragment) {
            ((AbsFragment) fragment).returnResult(this);
        }


    }

    public AbsFragment findFragmentbyTag(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            return (AbsFragment) fragment;
        } else {
            return null;
        }
    }
}
