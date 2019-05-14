package com.zhkrb.iwara.base;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public interface TransitionHelper {
    boolean onTransition(Context context, FragmentTransaction fragmentTransaction, Fragment enterFragment, Fragment exitFragment);
}
