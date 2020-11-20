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
 * Create by zhkrb on 2020/5/7 17:04
 */

package com.zhkrb.dragvideo.utils;

import android.view.View;
import android.view.ViewConfiguration;

public class DragSettleUtil {

    private View mParentView;
    private int mMinVelocity;
    private int mMaxVelocity;


    private static final int BASE_SETTLE_DURATION = 256; // ms
    private static final int MAX_SETTLE_DURATION = 600; // ms

    public DragSettleUtil(ViewConfiguration vc,View parent) {
        mParentView = parent;
        mMaxVelocity = vc.getScaledMaximumFlingVelocity();
        mMinVelocity = vc.getScaledMinimumFlingVelocity();
    }

    public int computeSettleDuration(int dy, int yvel,int range) {
        float i = clampMag(yvel,mMinVelocity,mMaxVelocity);
        yvel = (int) i;

        final int absDy = Math.abs(dy);
        final int absYVel = Math.abs(yvel);

        final float yweight = yvel != 0 ? (float) absYVel / absYVel :
                (float) absDy / absDy;

        int yduration = computeAxisDuration(dy, yvel, range);

        return (int) (yduration * yweight);
    }

    private int clampMag(int value, int absMin, int absMax) {
        final int absValue = Math.abs(value);
        if (absValue < absMin) return 0;
        if (absValue > absMax) return value > 0 ? absMax : -absMax;
        return value;
    }

    private int computeAxisDuration(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return 0;
        }

        final int width = mParentView.getWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, (float) Math.abs(delta) / width);
        final float distance = halfWidth + halfWidth
                * distanceInfluenceForSnapDuration(distanceRatio);

        int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float range = (float) Math.abs(delta) / motionRange;
            duration = (int) ((range + 1) * BASE_SETTLE_DURATION);
        }
        return Math.min(duration, MAX_SETTLE_DURATION);
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * (float) Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    public static boolean isViewHit(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        return x >= view.getLeft()
                && x < view.getRight()
                && y >= view.getTop()
                && y < view.getBottom();
    }


}
