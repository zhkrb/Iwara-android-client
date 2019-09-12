package com.zhkrb.iwara.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;



import io.reactivex.annotations.NonNull;


public class SystemUtil {

    public static void openUrl(@NonNull Activity activity, String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        activity.startActivity(intent);
    }


}
