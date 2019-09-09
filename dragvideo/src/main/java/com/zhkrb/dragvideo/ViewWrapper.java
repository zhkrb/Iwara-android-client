//Copyright zhkrb
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

//Create by zhkrb on 2019/9/7 22:12

package com.zhkrb.dragvideo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
        return mLayoutParams.width < 0 ? mView.getWidth() : mLayoutParams.width;
    }

    public void setHeight(int height){
        mLayoutParams.height = height;
    }

    public int getHeight(){
        return mLayoutParams.height < 0 ? mView.getHeight() : mLayoutParams.height;
    }

    public View getView() {
        return mView;
    }

    public void offsetLeftandRight(int offX){
        mView.offsetLeftAndRight(offX);
    }

    public void setTopMargin(int top){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            ((ViewGroup.MarginLayoutParams) mLayoutParams).topMargin = top;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public int getTopMargin(){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            return ((ViewGroup.MarginLayoutParams) mLayoutParams).topMargin;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public void setLeftMargin(int left){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            ((ViewGroup.MarginLayoutParams) mLayoutParams).leftMargin = left;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public int getLeftMargin(){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            return ((ViewGroup.MarginLayoutParams) mLayoutParams).leftMargin;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public void setRightMargin(int right){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            ((ViewGroup.MarginLayoutParams) mLayoutParams).rightMargin = right;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public int getRightMargin(){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            return ((ViewGroup.MarginLayoutParams) mLayoutParams).rightMargin;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public void setBottomMargin(int bottomMargin){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            ((ViewGroup.MarginLayoutParams) mLayoutParams).bottomMargin = bottomMargin;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public int getBottomMargin(){
        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams){
            return ((ViewGroup.MarginLayoutParams) mLayoutParams).bottomMargin;
        }else {
            throw new RuntimeException(mView.getClass().getName()+"Can't cast to MarginLayoutParams");
        }
    }

    public void setVisibile(int visibile){
        mView.setVisibility(visibile);
    }

    public int getVisibile(){
        return mView.getVisibility();
    }

}
