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
 * Create by zhkrb on 2019/10/2 21:34
 */

package com.zhkrb.dragvideo.contentViewBase;

import android.os.Bundle;

public class ContentFrame {

    private Class<?> Clazz;
    private Bundle args;
    private ContentTransHelper mHelper;
    private AbsContent requestContent;
    private int requestCode;

    public ContentFrame(Class<?> clazz) {
        Clazz = clazz;
    }

    public Class<?> getClazz() {
        return Clazz;
    }

    public Bundle getArgs() {
        return args;
    }

    public ContentFrame setArgs(Bundle args) {
        this.args = args;
        return this;
    }

    public ContentTransHelper getHelper() {
        return mHelper;
    }

    public void setHelper(ContentTransHelper helper) {
        mHelper = helper;
    }

    public AbsContent getRequestFragment() {
        return requestContent;
    }

    public void setRequest (AbsContent requestContent, int requestCode) {
        this.requestContent = requestContent;
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }



}
