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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
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
import com.zhkrb.iwara.utils.ToastUtil;

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
    protected ArrayList<String> mFragmentStack;
    private AtomicInteger mInteger = new AtomicInteger(0);
    private static final HashMap<Class<?>,Integer> mFragmentMode = new HashMap<>(0);

    private long mLastClickBackTime;

    private static final String FRAGMENT_STACK = "fragment_stack";

    public int getLaunchMode(Class<?> clazz) {
        Integer integer = mFragmentMode.get(clazz);
        if (integer == null) {
            throw new RuntimeException("Not register " + clazz.getName());
        } else {
            return integer;
        }
    }

    public static void setLaunchMode(Class<?> clazz, @AbsFragment.LaunchMode int mode){
        mFragmentMode.put(clazz,mode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        mFragmentStack = new ArrayList<>();
        if (savedInstanceState != null){
            ArrayList<String> list = savedInstanceState.getStringArrayList(FRAGMENT_STACK);
            if (list != null){
                mFragmentStack.addAll(list);
            }
        }

        main(savedInstanceState);

        Intent intent = getIntent();
        if (savedInstanceState == null){
            if (intent != null){
                String action = intent.getAction();
                if (Intent.ACTION_MAIN.equals(action)){
                    FragmentFrame frame = getLaunchFrame();
                    if (frame != null) {
                        startFragment(frame);
                        return;
                    }
                }
            }
        }
    }

    protected abstract FragmentFrame getLaunchFrame();

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(FRAGMENT_STACK,mFragmentStack);
    }

    protected abstract int getLayoutId();

    protected void main(Bundle savedInstanceState) {
        main();
    }

    protected void main() {
    }


    protected void reLoadRootFragment(FragmentFrame frame){
        Class<?> clazz = frame.getClazz();
        Bundle args = frame.getArgs();
        FragmentManager fragmentManager = getSupportFragmentManager();
        int launchMode = getLaunchMode(clazz);
        boolean forceNewScene = launchMode == AbsFragment.LAUNCH_MODE_STANDARD;
        boolean createNewScene = true;
        boolean findScene = false;
        AbsFragment fragment = null;

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Set default animation
        transaction.setCustomAnimations(R.anim.bottomtop_enter, R.anim.bottomtop_exit);

        String findSceneTag = null;
        for (int i = 0, n = mFragmentStack.size(); i < n; i++) {
            String tag = mFragmentStack.get(i);
            Fragment currentFragment = fragmentManager.findFragmentByTag(tag);
            if (currentFragment == null) {
                L.e("AbsActivity", "Can't find fragment with tag: " + tag);
                continue;
            }

            // Clear shared element
            currentFragment.setSharedElementEnterTransition(null);
            currentFragment.setSharedElementReturnTransition(null);
            currentFragment.setEnterTransition(null);
            currentFragment.setExitTransition(null);

            // Check is target scene
            if (!forceNewScene && !findScene && clazz.isInstance(currentFragment) &&
                    (launchMode == AbsFragment.LAUNCH_MODE_SINGLE_TASK || !currentFragment.isDetached())) {
                fragment = (AbsFragment) currentFragment;
                findScene = true;
                createNewScene = false;
                findSceneTag = tag;
                if (currentFragment.isDetached()) {
                    transaction.attach(currentFragment);
                }
            } else {
                // Remove it
                transaction.remove(currentFragment);
            }
        }

        // Handle tag list
        mFragmentStack.clear();
        if (null != findSceneTag) {
            mFragmentStack.add(findSceneTag);
        }

        if (createNewScene) {
            fragment = createFragment(clazz);
            fragment.setArguments(args);

            // Create scene tag
            String tag = Integer.toString(mInteger.getAndIncrement());

            // Add tag to list
            mFragmentStack.add(tag);

            // Add scene
            transaction.add(getContentView(), fragment, tag);
        }

        // Commit
        transaction.commitAllowingStateLoss();

        if (!createNewScene && args != null) {
            fragment.onNewArguments(args);
        }
    }

    public void startFragment(FragmentFrame frame){
        FragmentManager manager = getSupportFragmentManager();
        Class clazz = frame.getClazz();
        Bundle args = frame.getArgs();
        int launchMode = getLaunchMode(frame.getClazz());
        TransitionHelper helper = frame.getHelper();
        int stackSize = mFragmentStack.size();

        if (launchMode==AbsFragment.LAUNCH_MODE_SINGLE_TASK && stackSize>1){
            for (int i = 0; i<stackSize;i++){
                String tag = mFragmentStack.get(i);
                Fragment fragment = manager.findFragmentByTag(tag);
                if (fragment == null){
                    L.e("can't find tag: "+tag);
                    continue;
                }
                if (clazz.isInstance(fragment)){
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.bottomtop_enter,R.anim.bottomtop_exit);

                    for (int j = i+1; j<stackSize;j++){
                        String topTag = mFragmentStack.get(j);
                        Fragment topFragment = manager.findFragmentByTag(topTag);
                        if (topFragment == null){
                            L.e("can't find tag: "+tag);
                            continue;
                        }
                        topFragment.setSharedElementEnterTransition(null);
                        topFragment.setSharedElementReturnTransition(null);
                        topFragment.setEnterTransition(null);
                        topFragment.setExitTransition(null);
                        fragmentTransaction.remove(topFragment);
                    }

                    mFragmentStack.subList(i + 1, stackSize).clear();

                    if (fragment.isDetached()) {
                        fragmentTransaction.attach(fragment);
                    }

                    fragmentTransaction.commitAllowingStateLoss();
                    if (args != null) {
                        ((AbsFragment)fragment).onNewArguments(args);
                    }
                    return;
                }
            }
        }

        AbsFragment currentFragment = null;
        if (stackSize > 0){
            String tag = mFragmentStack.get(stackSize-1);
            Fragment fragment = manager.findFragmentByTag(tag);
            if (fragment != null && AbsFragment.class.isInstance(fragment)){
                currentFragment = (AbsFragment) fragment;
            }
        }

        if (launchMode==AbsFragment.LAUNCH_MODE_SINGLE_TOP && clazz.isInstance(currentFragment)){
            if (args!=null){
                ((AbsFragment)currentFragment).onNewArguments(args);
            }
            return;
        }


        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        AbsFragment fragment = createFragment(clazz);

        if (args!=null){
            fragment.setArguments(args);
        }


        if (currentFragment!=null){
            if (helper==null||helper.onTransition(mContext,fragmentTransaction,fragment,currentFragment)){
                fragment.setEnterTransition(null);
                fragment.setExitTransition(null);
                fragment.setSharedElementReturnTransition(null);
                fragment.setSharedElementEnterTransition(null);
                currentFragment.setEnterTransition(null);
                currentFragment.setExitTransition(null);
                currentFragment.setSharedElementReturnTransition(null);
                currentFragment.setSharedElementEnterTransition(null);

                fragmentTransaction.setCustomAnimations(R.anim.bottomtop_enter,R.anim.bottomtop_exit);
            }
            if (!currentFragment.isDetached()){
                fragmentTransaction.detach(currentFragment);
            }
        }
        String tag = String.valueOf(mInteger.getAndIncrement());
        fragmentTransaction.add(getContentView(),fragment,tag);
        fragmentTransaction.commitAllowingStateLoss();

        mFragmentStack.add(tag);

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
        } catch (InstantiationException e) {
            throw new IllegalStateException("Can't instance " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("The constructor of " +
                    clazz.getName() + " is not visible", e);
        } catch (ClassCastException e) {
            throw new IllegalStateException(clazz.getName() + " can not cast to scene", e);
        }
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
            long curTime = System.currentTimeMillis();
            if (curTime - mLastClickBackTime > 2000) {
                mLastClickBackTime = curTime;
                ToastUtil.show(R.string.main_click_next_exit);
                return;
            }
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
