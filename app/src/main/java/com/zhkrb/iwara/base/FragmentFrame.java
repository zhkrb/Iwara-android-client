package com.zhkrb.iwara.base;

import android.bluetooth.BluetoothHeadset;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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
