package com.zhkrb.iwara.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.zhkrb.iwara.AppContext;
import com.zhkrb.iwara.R;

/**
 * Created by cxf on 2017/8/3.
 */

public class ToastUtil {

    private static Toast sToast;

    static {
        sToast = Toast.makeText(AppContext.sInstance, "", Toast.LENGTH_SHORT);
    }

    public static void show(int res){
        show(WordUtil.getString(res));
    }

    public static void show(String s) {
        sToast.setText(s);
        sToast.show();
    }

}
