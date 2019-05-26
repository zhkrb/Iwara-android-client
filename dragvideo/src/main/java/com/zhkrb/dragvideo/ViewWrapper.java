package com.zhkrb.dragvideo;

import android.view.View;
import android.view.ViewGroup;

public class ViewWrapper {

    private View mView;
    private ViewGroup.LayoutParams mLayoutParams;

    public ViewWrapper(View view) {
        mView = view;
        mLayoutParams = mView.getLayoutParams();
    }


    public void setWidth(int width) {
        mLayoutParams.width = width;
    }

    public int getWidth(){
        return mLayoutParams.width < 0 ? 0 : mLayoutParams.width;
    }

    public void setHeight(int height){
        mLayoutParams.height = height;
    }

    public int getHeight(){
        return mLayoutParams.height < 0 ? 0 : mLayoutParams.height;
    }

    public View getView() {
        return mView;
    }
}
