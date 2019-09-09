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

package com.zhkrb.dragvideo.contentView;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleObserver;

/**
 * ContentView 接口
 * 承载详情展示之类的页面
 */

public interface IContent extends LifecycleObserver {

    /**
     * View初始化
     */
    void init(Bundle arg);

    /**
     * View 释放
     */
    void release();

    /**
     * View 重载
     */
    void reload(Bundle arg);

    /**
     * 添加到父view
     */
    void addToParent(ViewGroup parent);




}
