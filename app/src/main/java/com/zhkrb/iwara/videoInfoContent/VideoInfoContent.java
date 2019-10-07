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
 * Create by zhkrb on 2019/10/7 15:04
 */

package com.zhkrb.iwara.videoInfoContent;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.zhkrb.dragvideo.contentViewBase.AbsContent;
import com.zhkrb.iwara.R;

public class VideoInfoContent extends AbsContent {

    public VideoInfoContent(Context context) {
        super(context);
    }

    public VideoInfoContent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoInfoContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void main() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.content_video_info;
    }

}
