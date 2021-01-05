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
 * Create by zhkrb on 2021/1/5 16:37
 */

package com.zhkrb.netowrk.retrofit;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;

/**
 * @description：
 * @author：zhkrb
 * @DATE： 2021/1/5 16:37
 */
public final class ParameterizedTypeImpl implements ParameterizedType {


    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return new Type[0];
    }

    @NonNull
    @Override
    public Type getRawType() {
        return null;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}