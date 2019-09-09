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

package com.zhkrb.dragvideo.videoPlayer;

public interface PlayerStateListener {

    int PLAY = 0x00011;
    int PAUSE = 0x00012;
    int LOADING = 0x00014;

    void onStateChange(int state);

    void onVideoFirstPrepared(int width,int height);

    void onBtnViewDown();

    void onProgressUpdate(int progress,int secProgress);

    void onBtnMore();

    void onClickViewToNom();
}
