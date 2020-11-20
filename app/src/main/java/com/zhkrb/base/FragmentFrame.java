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

import android.os.Bundle;

public final class FragmentFrame  {

    private Class<?> Clazz;
    private Bundle args;
    private TransitionHelper mHelper;
    private AbsFragment requestFragment;
    private int requestCode;

    public FragmentFrame(Class<?> clazz) {
        Clazz = clazz;
    }

    public Class<?> getClazz() {
        return Clazz;
    }

    public Bundle getArgs() {
        return args;
    }

    public FragmentFrame setArgs(Bundle args) {
        this.args = args;
        return this;
    }

    public TransitionHelper getHelper() {
        return mHelper;
    }

    public void setHelper(TransitionHelper helper) {
        mHelper = helper;
    }

    public AbsFragment getRequestFragment() {
        return requestFragment;
    }

    public void setRequest (AbsFragment requestFragment, int requestCode) {
        this.requestFragment = requestFragment;
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }


}
