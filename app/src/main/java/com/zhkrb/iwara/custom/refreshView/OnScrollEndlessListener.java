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

package com.zhkrb.iwara.custom.refreshView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zhkrb.iwara.utils.ImgLoader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class OnScrollEndlessListener extends RecyclerView.OnScrollListener {

    private static final int Linearlayout = 1;
    private static final int Graidlayout = 2;
    private static final int StaggeredGridLayout = 3;

    @IntDef({
            Linearlayout,
            Graidlayout,
            StaggeredGridLayout
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface LayoutManagerType{}

    private  @LayoutManagerType int mLayoutManagerType;    //layout manager 类型
    private int[] mLastVisiblePosses;                       //最后一个位置
    private int mLastVisibleItemPos;                        //最后一个可见位置

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager==null){
            return;
        }
        int visibleCount = manager.getChildCount();
        int totleCount = manager.getItemCount();
        if (totleCount>0 && newState == RecyclerView.SCROLL_STATE_IDLE){
//            ImgLoader.resume();
        }
        if (visibleCount>0 && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPos==totleCount-1){
            onLoadMore();
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager==null){
            return;
        }
        if (mLayoutManagerType == 0){
            if (manager instanceof LinearLayoutManager){
                mLayoutManagerType = Linearlayout;
            }else if (manager instanceof GridLayoutManager){
                mLayoutManagerType = Graidlayout;
            }else if (manager instanceof StaggeredGridLayoutManager){
                mLayoutManagerType = StaggeredGridLayout;
            }else {
                throw new RuntimeException("unsupported layout manager");
            }
        }

        switch (mLayoutManagerType){
            case Linearlayout:
                mLastVisibleItemPos = ((LinearLayoutManager)manager).findLastVisibleItemPosition();
                break;
            case Graidlayout:
                mLastVisibleItemPos = ((GridLayoutManager)manager).findLastVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
                if (mLastVisiblePosses == null){
                    mLastVisiblePosses = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(mLastVisiblePosses);
                mLastVisibleItemPos = findMaxPos(mLastVisiblePosses);
                break;
        }
    }

    private static int findMaxPos(int[] lastVisiblePosses) {
        int max = lastVisiblePosses[0];
        for (int count:lastVisiblePosses){
            if (count > max){
                max = count;
            }
        }
        return max;
    }

    public abstract void onLoadMore();
}
