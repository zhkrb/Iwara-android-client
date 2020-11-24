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
 * Create by zhkrb on 2019/9/11 17:41
 */

package com.zhkrb.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.zhkrb.iwara.AppConfig;
import com.zhkrb.iwara.AppContext;
import com.zhkrb.iwara.R;
import com.zhkrb.base.AbsActivity;
import com.zhkrb.iwara.bean.UpdateInfoBean;
import com.zhkrb.netowrk.retrofit.HttpUtil;
import com.zhkrb.netowrk.retrofit.BaseRetrofitCallback;

import java.lang.ref.WeakReference;

public class UpdateUtil {

    public static void checkUpdate(Context context,boolean show){
        AppConfig.getInstance().setVersionCode(getVersion());
        Runnable runnable = new netRunable(context,show);
        ThreadPoolUtil.submit(runnable);
    }

    public static long getVersion(){
        long version = 0;
        try {
            PackageInfo packageInfo = AppContext.sInstance.getPackageManager().getPackageInfo(
                    AppContext.sInstance.getPackageName(),0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                version = packageInfo.getLongVersionCode();
            }else {
                version = packageInfo.versionCode;
            }
            L.e("getVersion: "+ version);
            L.e("getVersionName: " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            L.e("getVersionErr: "+e.getMessage());
        }
        return version;
    }

    static class netRunable implements Runnable{

        private boolean showFeedback = false;
        private WeakReference<Context> mContext;

        public netRunable(Context context,boolean showFeedback) {
            this.showFeedback = showFeedback;
            mContext = new WeakReference<>(context);
        }

        @Override
        public void run() {
            HttpUtil.getUpdateInfo(new BaseRetrofitCallback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(int code, String msg, String info) {
                    UpdateInfoBean bean = null;
                    try {
                        bean = JSON.parseObject(info,UpdateInfoBean.class);
                        if (AppConfig.getInstance().getVersionCode() >=  bean.getVersion()||
                                AppConfig.getInstance().getIgnoreVersion() == bean.getVersion()){
                            if (showFeedback){
                                ToastUtil.show(R.string.is_new);
                            }
                            return;
                        }
                    }catch (JSONException e){
                        if (showFeedback){
                            ToastUtil.show(R.string.get_update_fail);
                        }
                    }
                    if (bean == null){
                        if (showFeedback){
                            ToastUtil.show(R.string.get_update_fail);
                        }
                        return;
                    }
                    if (mContext.get() == null){
                        L.e("showUpdate fail context may null");
                        return;
                    }
                    showUpdate(mContext.get(),bean);
                }

                @Override
                public void onFinish() {

                }

                @Override
                public void onError(int code, String msg) {
                    if (showFeedback){
                        ToastUtil.show(R.string.get_update_fail);
                    }
                }
            });
        }

        private void showUpdate(Context context,UpdateInfoBean bean) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.update)
                    .setMessage(String.format(context.getResources().getString(R.string.update_info),
                            bean.getVersion_name(),
                            bean.getSize(),
                            bean.getInfo().replace("\\n", "\n")))
                    .setCancelable(true)
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        SystemUtil.openUrl(((AbsActivity)context),bean.getUrl());
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel,(dialog1,which1) ->{
                        dialog1.dismiss();
                    })
                    .setNeutralButton(R.string.ignore,(dialog,which) -> {
                        AppConfig.getInstance().putIgnoreVersion(bean.getVersion());
                        dialog.dismiss();
                    })
                    .show();
        }
    }

}
