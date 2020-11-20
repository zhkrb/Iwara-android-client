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

package com.zhkrb.utils;

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

    public static void smallCase(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(40,30);
            vibrator.vibrate(vibrationEffect);
        }else {
            vibrator.vibrate(40);
        }
    }

    public static void shortCase(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(60,60);
            vibrator.vibrate(vibrationEffect);
        }else {
            vibrator.vibrate(60);
        }
    }

    public static void longCase(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(300,60);
            vibrator.vibrate(vibrationEffect);
        }else {
            vibrator.vibrate(300);
        }
    }


}
