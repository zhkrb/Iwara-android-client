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

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.ViewGroup;

import com.shuyu.gsyvideoplayer.render.GSYRenderView;
import com.shuyu.gsyvideoplayer.render.glrender.GSYVideoGLViewBaseRender;
import com.shuyu.gsyvideoplayer.render.view.GSYTextureView;
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView;
import com.shuyu.gsyvideoplayer.render.view.IGSYRenderView;
import com.shuyu.gsyvideoplayer.render.view.listener.IGSYSurfaceListener;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.MeasureHelper;

public class GSYRenderExView extends GSYRenderView {

    public GSYRenderExView() {
    }


    public void addViewEx(Context context, ViewGroup textureViewContainer, int rotate, IGSYSurfaceListener gsySurfaceListener, MeasureHelper.MeasureFormVideoParamsListener videoParamsListener, GSYVideoGLView.ShaderInterface effect, float[] transform, GSYVideoGLViewBaseRender customRender, int mode, SurfaceTexture surface) {
        if (GSYVideoType.getRenderType() == GSYVideoType.TEXTURE){
            mShowView = GSYTextureExView.addTextureView(context, textureViewContainer, rotate, gsySurfaceListener, videoParamsListener,surface);
        }else {
            throw new RuntimeException("Must set Texture Render mode");
        }
    }

    public void reAddView(ViewGroup textureViewContainer, int rotate, GSYTextureExView renderView){
        renderView.setRotation(rotate);
        renderView.setSurfaceTexture(renderView.getSaveSurfaceTexture());
        renderView.getIGSYSurfaceListener().onSurfaceAvailable(renderView.getSurface());
        GSYRenderView.addToParent(textureViewContainer, renderView);
    }

    @Override
    public void releaseAll() {
        mShowView.getIGSYSurfaceListener().onSurfaceDestroyed(((GSYTextureExView)mShowView).getSurface());
        ((GSYTextureExView)mShowView).getSaveSurfaceTexture().release();
        ((GSYTextureExView)mShowView).getSurface().release();
        super.releaseAll();
    }
}
