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
 * Create by zhkrb on 2021/1/8 18:03
 */

package com.zhkrb.dialog.dialogManager;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/1/8 18:03
 */
public class DialogShowManager {


    private volatile static DialogShowManager mShowManager;
    private static ConcurrentLinkedQueue<DialogWrapper> mQueue;
    private volatile boolean isShow = false;


    public DialogShowManager() {
        mQueue = new ConcurrentLinkedQueue<>();
    }

    public static DialogShowManager getInstance(){
        if (mShowManager == null){
            synchronized (DialogShowManager.class){
                if (mShowManager == null){
                    mShowManager = new DialogShowManager();
                }
            }
        }
        return mShowManager;
    }


    public synchronized boolean requestShow(DialogWrapper wrapper){
        boolean isAdd = mQueue.offer(wrapper);
        check();
        return isAdd;
    }

    private synchronized void check() {
        if (!isShow){
            showNext();
        }
    }

    private synchronized void showNext() {
        DialogWrapper wrapper = mQueue.poll();
        if (wrapper == null || wrapper.getBuilder() == null){
            return;
        }
        isShow = true;
        if (!wrapper.getBuilder().show()){
            showOff();
        }
    }

    public synchronized void showOff(){
        isShow = false;
        check();
    }

}
