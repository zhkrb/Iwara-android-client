package com.zhkrb.dialog.dialogManager;


import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2020/7/6 13:43
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
