package com.foodBasket.util.animation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.foodBasket.util.dimen.DimenUtil;

public final class BezierAnimation {

    public static void addCart(Activity delegate, View start, View end,
                               ImageView target, BezierUtil.AnimationListener animationListener) {
        /* 起点 */
        final int[] startXY = new int[2];
        start.getLocationInWindow(startXY);
        startXY[0] += start.getWidth() / 2;
        final int fx = startXY[0];
        final int fy = startXY[1];

        final ViewGroup anim_mask_layout = BezierUtil.createAnimLayout(delegate);
        anim_mask_layout.addView(target);
        final View v = BezierUtil.addViewToAnimLayout(delegate, target, startXY, true);
        if (v == null) {
            return;
        }
        /* 终点 */
        final int[] endXY = new int[2];
        end.getLocationInWindow(endXY);
        final int tx = endXY[0] + end.getWidth() / 2 - 48;
        final int ty = endXY[1] + end.getHeight() / 2;
        /* 中点 */
        final int mx = (tx + fx) / 2;
        final int my = DimenUtil.getScreenHeight() / 10;
        BezierUtil.startAnimationForJd(v, 0, 0, fx, fy, mx, my, tx, ty, animationListener);
    }

}
