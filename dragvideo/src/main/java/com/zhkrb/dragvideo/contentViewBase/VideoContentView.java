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
 * Create by zhkrb on 2019/10/2 21:21
 */

package com.zhkrb.dragvideo.contentViewBase;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.zhkrb.dragvideo.R;
import com.zhkrb.dragvideo.contentViewBase.AbsContent;
import com.zhkrb.dragvideo.contentViewBase.ContentFrame;
import com.zhkrb.dragvideo.contentViewBase.ContentTransHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class VideoContentView extends NestedScrollView {

    private ArrayList<String> mViweStack = new ArrayList<>(0);
    private HashMap<String,AbsContent> mViewPool = new HashMap<>(0);
    private AtomicInteger mInteger = new AtomicInteger(0);
    private Context mContext;

    public VideoContentView(@NonNull Context context) {
        this(context,null);
    }

    public VideoContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void loadRootContent(ContentFrame frame){
        if (getChildCount() > 0){
            removeAllContent();
        }
        AbsContent rootContent = createContent(frame.getClazz(),mContext);
        rootContent.setArgs(frame.getArgs());
        rootContent.setContext(mContext);
        rootContent.setParent(this);
        ContentTransHelper helper = frame.getHelper();
        String tag = String.valueOf(mInteger.getAndIncrement());
        if (helper == null || !helper.onTransition(mContext,true,rootContent,null)){
            rootContent.setAnimation(null);
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rootContent.setLayoutParams(layoutParams);
        addView(rootContent);
        mViweStack.add(tag);
        mViewPool.put(tag,rootContent);
        rootContent.load();
    }

    public void loadNewContent(ContentFrame frame){
        AbsContent content = createContent(frame.getClazz(),mContext);
        content.setArgs(frame.getArgs());
        content.setContext(mContext);
        content.setParent(this);
        ContentTransHelper helper = frame.getHelper();

        AbsContent currentContent = null;
        if (mViweStack.size() > 0){
            String id = mViweStack.get(mViweStack.size() - 1);
            currentContent = mViewPool.get(id);
        }
        String tag = String.valueOf(mInteger.getAndIncrement());
        if (helper == null || !helper.onTransition(mContext,true,currentContent,content)){
            content.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.top_to_bottom_enter));
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        content.setLayoutParams(layoutParams);
        addView(content);
        mViweStack.add(tag);
        mViewPool.put(tag,content);
        if (currentContent!=null && currentContent.getVisibility() != GONE){
            currentContent.setVisibility(GONE);
        }
        content.load();
    }

    public void finishContent(ContentTransHelper helper){
        if (mViweStack.size() > 1){
            String currentId = mViweStack.get(mViweStack.size() - 1);
            AbsContent currentContent = mViewPool.get(currentId);
            if (currentContent == null){
                return;
            }

            String nextId = mViweStack.get(mViweStack.size() - 2);
            AbsContent nextContent = mViewPool.get(nextId);

            if (helper == null || !helper.onTransition(mContext,true,currentContent,nextContent)){
                currentContent.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.top_to_bottom_exit));
            }
            if (nextContent != null){
                nextContent.setVisibility(VISIBLE);
            }
            removeView(currentContent);
            currentContent.release();
            mViewPool.remove(currentId);
            mViweStack.remove(mViweStack.size()-1);
        }
    }

    private void removeAllContent() {
        if (mViweStack.size() > 0){
            for (String id :mViweStack){
                AbsContent content = mViewPool.get(id);
                if (content == null){
                    Log.e("videoContent","Can't find View: "+id);
                    continue;
                }
                removeView(content);
                content.release();
                mViewPool.remove(id);
                mViweStack.remove(mViweStack.size()-1);
            }
        }
        if (getChildCount() > 0){
            removeAllViews();
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    private AbsContent createContent(Class<?> clazz,Context context) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(Context.class);
            constructor.setAccessible(true);
            return (AbsContent) constructor.newInstance(context);
        } catch (InstantiationException e) {
            throw new IllegalStateException("Can't instance " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("The constructor of " +
                    clazz.getName() + " is not visible", e);
        } catch (ClassCastException e) {
            throw new IllegalStateException(clazz.getName() + " can not cast to scene", e);
        } catch (NoSuchMethodException|InvocationTargetException e) {
            throw new RuntimeException("method value error");
        }
    }

    public boolean canBackUp(){
        if (mViweStack.size() > 1){
            finishContent(null);
            return true;
        }
        return false;
    }



}
