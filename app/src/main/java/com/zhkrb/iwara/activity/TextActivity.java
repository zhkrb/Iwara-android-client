package com.zhkrb.iwara.activity;

import android.util.ArrayMap;

import com.google.android.material.tabs.TabLayout;
import com.zhkrb.base.AbsActivity;
import com.zhkrb.base.AbsFragment;
import com.zhkrb.base.FragmentFrame;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.fragment.BaseChildFragment;
import com.zhkrb.iwara.fragment.BaseTestFragment1;
import com.zhkrb.iwara.fragment.BaseTestFragment2;
import com.zhkrb.iwara.fragment.BaseTestFragment3;

import java.util.Map;
import java.util.Set;

public class TextActivity extends AbsActivity {

    private TabLayout mTabLayout;

    private static Map<Class<?>,Integer> mBaseMap = new ArrayMap<>();

    static {
        setLaunchMode(BaseTestFragment1.class, AbsFragment.LAUNCH_MODE_BASE);
        setLaunchMode(BaseTestFragment2.class, AbsFragment.LAUNCH_MODE_BASE);
        setLaunchMode(BaseTestFragment3.class, AbsFragment.LAUNCH_MODE_BASE);
        setLaunchMode(BaseChildFragment.class, AbsFragment.LAUNCH_MODE_PAGE);
        mBaseMap.put(BaseTestFragment1.class,0);
        mBaseMap.put(BaseTestFragment2.class,1);
        mBaseMap.put(BaseTestFragment3.class,2);
    }

    @Override
    protected FragmentFrame getLaunchFrame() {

        return new FragmentFrame(BaseTestFragment1.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getContentView() {
        return R.id.content;
    }

    @Override
    protected void main() {
        super.main();
        mTabLayout = findViewById(R.id.tab_parent);
        mTabLayout.addOnTabSelectedListener(mListener);
    }

    private TabLayout.OnTabSelectedListener mListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int pos = tab.getPosition();
            if (pos == 0){
                startFragment(new FragmentFrame(BaseTestFragment1.class));
            }else if (pos == 1){
                startFragment(new FragmentFrame(BaseTestFragment2.class));
            }else if (pos == 2){
                startFragment(new FragmentFrame(BaseTestFragment3.class));
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    protected void onTransactionBaseFragment(Class latestBaseFragment) {
        super.onTransactionBaseFragment(latestBaseFragment);
        Integer a = null;
        for (Class<?> cls : mBaseMap.keySet()){
            if (cls.equals(latestBaseFragment)) {
                a = mBaseMap.get(cls);
                break;
            }
        }
        if (a != null){
            mTabLayout.removeOnTabSelectedListener(mListener);
            mTabLayout.selectTab(mTabLayout.getTabAt(a));
            mTabLayout.addOnTabSelectedListener(mListener);
        }
    }
}
