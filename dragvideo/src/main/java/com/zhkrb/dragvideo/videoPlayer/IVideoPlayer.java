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

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Map;

public interface IVideoPlayer {

    int VIEW_STATE_NOM = 0x00010;   //正常
    int VIEW_STATE_SMILL= 0x00011;  //拖动
    int VIEW_STATE_DOWN = 0x00012;  //下滑隐藏


    /**
     * 释放
     */
    void releasePlayer();

    /**
     * 加载
     */
    void load(String shareUrl,String title,String thumb);

    /**
     * 播放 带开始时间
     */
    void playUrl(String url,long startTime);



    /**
     * 播放/暂停以及按钮 监听
     */
    void setPlayerStateListener(PlayerStateListener listener);

    /**
     *  设置控件拖动状态
     */
    void setViewState(int state);

    /**
     * 暂停
     */
    void pause();

    /**
     * 恢复暂停
     */
    void resume();

    void setSeekbar(SeekBar seekbar);

    int getViewState();

    float getPlaySpeed();

    void setPlaySpeed(float speed);

    long getPlayingPos();

    void setHeader(Map<String,String> header);

    boolean isFullScreen();

    void exitFullScreen();
}
