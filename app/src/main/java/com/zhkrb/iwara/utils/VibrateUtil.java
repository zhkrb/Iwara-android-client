package com.zhkrb.iwara.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.zhkrb.iwara.AppContext;

public class VibrateUtil {

    private static Vibrator vibrator;
    private static VibrationEffect vibrationEffect;

    static {
        vibrator = (Vibrator) AppContext.sInstance.getSystemService(Context.VIBRATOR_SERVICE);
    }


    public static void Short(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(100,100);
            vibrator.vibrate(vibrationEffect);
        }else {
            vibrator.vibrate(100);
        }
    }

    public static void Long(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(500,100);
            vibrator.vibrate(vibrationEffect);
        }else {
            vibrator.vibrate(500);
        }
    }


}
