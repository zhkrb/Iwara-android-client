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

package com.zhkrb.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.WindowManager;

import com.google.android.material.transition.MaterialFadeThrough;
import com.zhkrb.iwara.R;
import com.zhkrb.utils.ContextLocalWrapper;
import com.zhkrb.utils.L;
import com.zhkrb.utils.SpUtil;
import com.zhkrb.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class AbsActivity extends AppCompatActivity {

    private static final String TAG = "Base Activity";

    protected Context mContext;
    protected ArrayList<String> mFragmentStack;
    protected ArrayMap<Class<?>, String> mFragmentBaseCache;
    private final AtomicInteger mInteger = new AtomicInteger(0);
    private static final HashMap<Class<?>, Integer> M_FRAGMENT_MODE = new HashMap<>(0);

    private long mLastClickBackTime;

    private static final String FRAGMENT_STACK = "fragment_stack";
    private static final String FRAGMENT_STACK_NEXT_TAG = "fragment_stack_next_tag";

    public static final String ACTION_START_FRAGMENT = "start_fragment";
    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static final String KEY_FRAGMENT_ARGS = "key_fragment_args";

    /**
     * 切换锁
     */
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 获取fragment启动模式
     *
     * @param clazz 类
     * @return 模式
     */
    public int getLaunchMode(Class<?> clazz) {
        Integer integer = M_FRAGMENT_MODE.get(clazz);
        if (integer == null) {
            throw new RuntimeException("Not register " + clazz.getName());
        } else {
            return integer;
        }
    }

    /**
     * 设置fragment启动模式
     *
     * @param clazz 类
     * @param mode  模式
     */
    public static void setLaunchMode(Class<?> clazz, @AbsFragment.LaunchMode int mode) {
        M_FRAGMENT_MODE.put(clazz, mode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        mFragmentStack = new ArrayList<>();
        mFragmentBaseCache = new ArrayMap<>(1);
        if (savedInstanceState != null) {
            ArrayList<String> list = savedInstanceState.getStringArrayList(FRAGMENT_STACK);
            if (list != null) {
                mFragmentStack.addAll(list);
            }
            mInteger.lazySet(savedInstanceState.getInt(FRAGMENT_STACK_NEXT_TAG));
        }

        main(savedInstanceState);

        Intent intent = getIntent();
        if (savedInstanceState == null) {
            if (intent != null) {
                String action = intent.getAction();
                if (Intent.ACTION_MAIN.equals(action)) {
                    FragmentFrame frame = getLaunchFrame();
                    if (frame != null) {
                        reLoadRootFragment(frame);
                        return;
                    }
                } else if (ACTION_START_FRAGMENT.equals(action)) {
                    onStartFragmentForIntent(intent);
                }
            }
        }
    }

    /**
     * 默认fragment
     *
     * @return
     */
    protected abstract FragmentFrame getLaunchFrame();

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(FRAGMENT_STACK, mFragmentStack);
        outState.putInt(FRAGMENT_STACK_NEXT_TAG, mInteger.getAndIncrement());
    }

    protected abstract int getLayoutId();

    protected void main(Bundle savedInstanceState) {
        main();
    }

    protected void main() {
    }

    /**
     * 从intent启动fragment
     *
     * @param intent
     */
    private void onStartFragmentForIntent(Intent intent) {
        String clazzStr = intent.getStringExtra(KEY_FRAGMENT_NAME);
        if (null == clazzStr) {
            return;
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(clazzStr);
        } catch (ClassNotFoundException e) {
            L.e(TAG, "Can't find class " + clazzStr + e);
            return;
        }

        Bundle args = intent.getBundleExtra(KEY_FRAGMENT_ARGS);

        FragmentFrame frame = new FragmentFrame(clazz).setArgs(args);

        startFragment(frame);
    }

    /**
     * 重载根fragment, 移除上层全部
     *
     * @param frame
     */
    protected void reLoadRootFragment(FragmentFrame frame) {
        LOCK.lock();
        try {
            Class<?> clazz = frame.getClazz();
            Bundle args = frame.getArgs();
            FragmentManager fragmentManager = getSupportFragmentManager();
            int launchMode = getLaunchMode(clazz);
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
                boolean isSame = clazz.isInstance(currentFragment) &&
                        (launchMode == AbsFragment.LAUNCH_MODE_BASE || !currentFragment.isDetached());
                if (!findScene && isSame) {
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
                if (launchMode == AbsFragment.LAUNCH_MODE_BASE) {
                    mFragmentBaseCache.put(clazz, tag);
                }

                // Add scene
                transaction.add(getContentView(), fragment, tag);
            }

            // Commit
            transaction.commitAllowingStateLoss();

            if (getLaunchMode(fragment.getClass()) == AbsFragment.LAUNCH_MODE_BASE) {
                onTransactionBaseFragment(fragment.getClass());
            }
            onTransactionFragment(fragment);

            if (!createNewScene && args != null) {
                fragment.onNewArguments(args);
            }
        } finally {
            LOCK.unlock();
        }
    }

    public void startFragment(FragmentFrame frame) {
        LOCK.lock();
        try {
            FragmentManager manager = getSupportFragmentManager();
            Class<?> clazz = frame.getClazz();
            Bundle args = frame.getArgs();
            int launchMode = getLaunchMode(frame.getClazz());
            TransitionHelper helper = frame.getHelper();
            int stackSize = mFragmentStack.size();

            if (launchMode == AbsFragment.LAUNCH_MODE_BASE && (stackSize > 1 || mFragmentBaseCache.size() > 0)) {
                Fragment baseLatestFragment = null;
                String baseLatestTag = "";

                for (int i = 0; i < stackSize; i++) {
                    String tag = mFragmentStack.get(i);
                    Fragment fragment = manager.findFragmentByTag(tag);
                    if (fragment == null) {
                        L.e("can't find tag fragment: " + tag);
                        continue;
                    }
                    if (clazz.isInstance(fragment)) {
                        if (isLatestFragment(tag)) {
                            return;
                        }
                        baseLatestFragment = fragment;
                        baseLatestTag = tag;
                    }
                }

                if (baseLatestFragment == null) {
                    String tag = mFragmentBaseCache.get(clazz);
                    if (!TextUtils.isEmpty(tag)) {
                        baseLatestFragment = manager.findFragmentByTag(tag);
                        baseLatestTag = tag;
                    }

                }

                if (baseLatestFragment != null) {
                    ArrayList<String> tagList = findBaseFragmentChildByTag(baseLatestTag);
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.anim.bottomtop_enter, R.anim.bottomtop_exit);

                    String currTag = mFragmentStack.get(stackSize - 1);
                    AbsFragment currentFragment = findFragmentByTag(currTag);
                    if (currentFragment == null) {
                        throw new RuntimeException("can't find current AbsFragment: " + currTag);
                    }

                    String latestTag = tagList.get(tagList.size() - 1);
                    AbsFragment latestFragment = findFragmentByTag(latestTag);
                    if (latestFragment == null) {
                        latestFragment = (AbsFragment) baseLatestFragment;
                    }

                    if (helper == null || helper.onTransition(mContext, fragmentTransaction, latestFragment, currentFragment)) {
                        MaterialFadeThrough through = new MaterialFadeThrough();
                        through.setDuration(150);
                        latestFragment.setEnterTransition(through);
                    }

                    if (mFragmentStack.get(0).equals(tagList.get(0))) {
                        String temp = mFragmentStack.remove(0);
                        mFragmentStack.removeAll(tagList);
                        mFragmentStack.add(0,temp);
                    }else {
                        mFragmentStack.removeAll(tagList);
                    }

                    mFragmentStack.addAll(tagList);
                    if (!currentFragment.isDetached()) {
                        fragmentTransaction.detach(currentFragment);
                    }
                    if (latestFragment.isDetached()) {
                        fragmentTransaction.attach(latestFragment);
                    }
                    fragmentTransaction.commitAllowingStateLoss();

                    if (getLaunchMode(latestFragment.getClass()) == AbsFragment.LAUNCH_MODE_BASE) {
                        onTransactionBaseFragment(latestFragment.getClass());
                    }
                    onTransactionFragment(latestFragment);
                    return;
                }
            }

            AbsFragment currentFragment = null;
            if (stackSize > 0) {
                String tag = mFragmentStack.get(stackSize - 1);
                currentFragment = findFragmentByTag(tag);
                if (currentFragment == null) {
                    L.e("can't find tag: " + tag);
                }
            }

            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            AbsFragment fragment = createFragment(clazz);

            if (args != null) {
                fragment.setArguments(args);
            }

            if (currentFragment != null) {
                if (helper == null || helper.onTransition(mContext, fragmentTransaction, fragment, currentFragment)) {
                MaterialFadeThrough through = new MaterialFadeThrough();
                through.setDuration(150);
                fragment.setEnterTransition(through);
                }
                if (!currentFragment.isDetached()) {
                    fragmentTransaction.detach(currentFragment);
                }
            }
            String tag = String.valueOf(mInteger.getAndIncrement());
            fragmentTransaction.add(getContentView(), fragment, tag);

            fragmentTransaction.commitAllowingStateLoss();

            mFragmentStack.add(tag);

            if (launchMode == AbsFragment.LAUNCH_MODE_BASE) {
                mFragmentBaseCache.put(clazz, tag);
                onTransactionBaseFragment(clazz);
            }
            onTransactionFragment(fragment);

            if (frame.getRequestFragment() != null) {
                fragment.addRequest(frame.getRequestFragment().getClass().getName(), frame.getRequestCode());
            }
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 是否双击退出
     *
     * @return
     */
    protected boolean isDoubleExit() {
        return true;
    }


    @Override
    public void onBackPressed() {
        int size = mFragmentStack.size();
        if (size == 0) {
            super.onBackPressed();
            return;
        }

        if (isDoubleExit() && mFragmentStack.size() == 1) {
            long curTime = System.currentTimeMillis();
            if (curTime - mLastClickBackTime > 2000) {
                mLastClickBackTime = curTime;
                ToastUtil.show(R.string.main_click_next_exit);
                return;
            }
        }

        String tag = mFragmentStack.get(mFragmentStack.size() - 1);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            L.e("can't find finish fragment tag: " + tag);
            return;
        }
        if (!(fragment instanceof AbsFragment)) {
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
        if (SpUtil.getInstance().getBooleanValue(SpUtil.SECURITY_ENABLE)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        } else {
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
        if (!TextUtils.isEmpty(lang) && !lang.equals("default")) {
            locale = new Locale(lang);
        }
        if (locale != null) {
            newBase = ContextLocalWrapper.wrap(newBase, locale);
        }
        super.attachBaseContext(newBase);
    }

    public void finish(AbsFragment fragment, TransitionHelper helper) {
        finish(fragment.getTag(), helper);
    }

    public void finish(String tag, TransitionHelper transitionHelper) {
        LOCK.lock();
        try {
            L.d("stack",mFragmentStack.toString());
            L.d("stack","" + mFragmentStack.size());

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                L.e("can't find fragment :" + tag);
                return;
            }

            int fragmentIndex = -1;

            for (int i = 0; i < mFragmentStack.size(); i++) {
                if (tag.equals(mFragmentStack.get(i))) {
                    fragmentIndex = i;
                }
            }

            if (fragmentIndex < 0) {
                L.e("can't find fragment tag :" + tag);
                return;
            }

            if (mFragmentStack.size() == 1) {
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

                    MaterialFadeThrough through = new MaterialFadeThrough();
                    through.setDuration(150);
                    next.setEnterTransition(through);
                }
                // Attach fragment
                transaction.attach(next);
            }

            if (getLaunchMode(fragment.getClass()) == AbsFragment.LAUNCH_MODE_BASE) {
                if (!fragment.isDetached()) {
                    transaction.detach(fragment);
                }
            } else {
                transaction.remove(fragment);
            }

            transaction.commitAllowingStateLoss();

            // Remove tag
            mFragmentStack.remove(fragmentIndex);

            if (getLaunchMode(fragment.getClass()) == AbsFragment.LAUNCH_MODE_BASE) {
                onTransactionBaseFragment(findLatestBaseFragment());
            }
            onTransactionFragment(next);

            // Return result
            if (fragment instanceof AbsFragment) {
                ((AbsFragment) fragment).returnResult(this);
            }
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 关闭一个fragment
     *
     * @param baseFragment
     */
    public void finishSelf(AbsFragment baseFragment) {
        LOCK.lock();
        try {
            String tag = baseFragment.getTag();
            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                L.e("can't find fragment :" + tag);
                return;
            }

            int fragmentIndex = mFragmentStack.indexOf(tag);
            if (fragmentIndex < 0) {
                L.e("can't find fragment tag :" + tag);
                return;
            }

            if (fragment instanceof AbsFragment) {
                ((AbsFragment) fragment).realDestroy();
            }

            if (mFragmentStack.size() == 1) {
                L.i("finish activity");
                finish();
                return;
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            fragment.setSharedElementEnterTransition(null);
            fragment.setSharedElementReturnTransition(null);
            fragment.setEnterTransition(null);
            fragment.setExitTransition(null);

            transaction.setCustomAnimations(0,
                    0);

            transaction.remove(fragment);
            transaction.commitAllowingStateLoss();

            // Remove tag
            mFragmentStack.remove(fragmentIndex);

            // Return result
            if (fragment instanceof AbsFragment) {
                ((AbsFragment) fragment).returnResult(this);
            }
        } finally {
            LOCK.unlock();
        }
    }

    protected void onTransactionBaseFragment(Class latestBaseFragment) {

    }

    protected void onTransactionFragment(Fragment fragment) {

    }

    public AbsFragment findFragmentByTag(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment instanceof AbsFragment) {
            return (AbsFragment) fragment;
        } else {
            return null;
        }
    }

    public ArrayList<String> findBaseFragmentChildByTag(String tag) {
        ArrayList<String> list = new ArrayList<>();
        list.add(tag);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = mFragmentStack.indexOf(tag) + 1; i < mFragmentStack.size(); i++) {
            String childTag = mFragmentStack.get(i);
            Fragment fragment = fragmentManager.findFragmentByTag(childTag);
            if (fragment == null) {
                L.e("can't find tag: " + childTag);
                continue;
            }

            if (getLaunchMode(fragment.getClass()) == AbsFragment.LAUNCH_MODE_BASE) {
                break;
            }
            list.add(childTag);
        }

        return list;
    }

    private boolean isLatestFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = mFragmentStack.indexOf(tag) + 1; i < mFragmentStack.size(); i++) {
            String childTag = mFragmentStack.get(i);
            Fragment fragment = fragmentManager.findFragmentByTag(childTag);
            if (fragment == null) {
                L.e("can't find tag: " + childTag);
                continue;
            }

            if (getLaunchMode(fragment.getClass()) == AbsFragment.LAUNCH_MODE_BASE) {
                return false;
            }
        }
        return true;
    }

    private Class<?> findLatestBaseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Class<?> clazz = null;
        for (int i = 0; i < mFragmentStack.size(); i++) {
            String childTag = mFragmentStack.get(i);
            Fragment fragment = fragmentManager.findFragmentByTag(childTag);
            if (fragment == null) {
                L.e("can't find tag: " + childTag);
                continue;
            }

            if (getLaunchMode(fragment.getClass()) == AbsFragment.LAUNCH_MODE_BASE) {
                clazz = fragment.getClass();
            }
        }
        return clazz;
    }

}
