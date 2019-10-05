///*
// * Copyright zhkrb
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// * Create by zhkrb on 2019/10/2 21:21
// */
//
//package com.zhkrb.dragvideo.widget;
//
//import android.content.Context;
//import android.os.Parcelable;
//import android.util.AttributeSet;
//import android.view.animation.AlphaAnimation;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.widget.NestedScrollView;
//
//import com.zhkrb.dragvideo.contentView.AbsContent;
//import com.zhkrb.dragvideo.contentView.ContentFrame;
//import com.zhkrb.dragvideo.contentView.ContentTransHelper;
//import com.zhkrb.dragvideo.contentView.IContent;
//
//public class VideoContentView extends NestedScrollView {
//
//    private int ViewCount = 0;
//    private Context mContext;
//
//    public VideoContentView(@NonNull Context context) {
//        this(context,null);
//    }
//
//    public VideoContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs,0);
//    }
//
//    public VideoContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mContext = context;
//    }
//
//    public void loadRootContent(ContentFrame frame){
//        if (getChildCount() > 0){
//            removeAllViews();
//        }
//        AbsContent rootContent = createContent(frame.getClazz());
//        if (rootContent == null){
//            return;
//        }
//        rootContent.setArgs(frame.getArgs());
//        rootContent.setContext(mContext);
//        ContentTransHelper helper = frame.getHelper();
//        if (helper == null || !helper.onTransition(mContext,true,rootContent)){
//            addView(rootContent);
//        }
//
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        super.onRestoreInstanceState(state);
//    }
//
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        return super.onSaveInstanceState();
//    }
//
//    private AbsContent createContent(Class<?> clazz) {
//        try {
//            return (AbsContent) clazz.newInstance();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }catch (ClassCastException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public boolean canBackUp(){
//
//    }
//
//
//
//}
