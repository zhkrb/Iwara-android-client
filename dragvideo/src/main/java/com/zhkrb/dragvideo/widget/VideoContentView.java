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
//import android.util.Log;
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
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class VideoContentView extends NestedScrollView {
//
//    private ArrayList<String> mViweStack = new ArrayList<>(0);
//    private HashMap<String,AbsContent> mViewPool = new HashMap<>(0);
//    private AtomicInteger mInteger = new AtomicInteger(0);
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
//            removeAllContent();
//        }
//        AbsContent rootContent = createContent(frame.getClazz(),mContext);
//        rootContent.setArgs(frame.getArgs());
//        rootContent.setContext(mContext);
//        rootContent.setParent(this);
//        ContentTransHelper helper = frame.getHelper();
//        if (helper == null || !helper.onTransition(mContext,true,rootContent)){
//            addView(rootContent);
//        }
//
//
//    }
//
//    private void removeAllContent() {
//        if (mViweStack.size() > 0){
//            for (String id :mViweStack){
//                AbsContent content = mViewPool.get(id);
//                if (content == null){
//                    Log.e("videoContent","Can't find View: "+id);
//                    continue;
//                }
//                content.removeToParent(false);
//                mViewPool.remove(id);
//                mViweStack.remove(id);
//            }
//        }
//        if (getChildCount() > 0){
//            removeAllViews();
//        }
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
//    private AbsContent createContent(Class<?> clazz,Context context) {
//        try {
//            Constructor constructor = clazz.getDeclaredConstructor(Context.class);
//            constructor.setAccessible(true);
//            return (AbsContent) constructor.newInstance(context);
//        } catch (InstantiationException e) {
//            throw new IllegalStateException("Can't instance " + clazz.getName(), e);
//        } catch (IllegalAccessException e) {
//            throw new IllegalStateException("The constructor of " +
//                    clazz.getName() + " is not visible", e);
//        } catch (ClassCastException e) {
//            throw new IllegalStateException(clazz.getName() + " can not cast to scene", e);
//        } catch (NoSuchMethodException|InvocationTargetException e) {
//            throw new RuntimeException("method value error");
//        }
//    }
//
//    public boolean canBackUp(){
//
//    }
//
//
//
//}
