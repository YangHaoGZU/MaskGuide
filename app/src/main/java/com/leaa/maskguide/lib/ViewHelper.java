package com.leaa.maskguide.lib;

import android.app.Activity;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewManager;

/**
 * Created by yanghao on 2017/5/10.
 */

public class ViewHelper {

    /**
     * @param view 目标显示控件
     * @return
     */
    public static RectF getViewBounds(View view) {
        int[] loc = new int[2];
        view.getLocationInWindow(loc);
        RectF rect = new RectF();
        rect.set(loc[0], loc[1], loc[0] + view.getMeasuredWidth(), loc[1] + view.getMeasuredHeight());

        if (view.getContext() instanceof Activity) {
            View content = ((Activity) view.getContext()).findViewById(android.R.id.content);
            content.getLocationInWindow(loc);
            rect.offset(0, -loc[1]);
        }
        return rect;
    }

    /**
     * 从View的父布局中移除它
     *
     * @param view
     * @return 是否移除成功
     */
    public static boolean removeViewFromParent(View view) {
        if (view == null) {
            return false;
        }
        if (view.getParent() == null) {
            return true;
        }
        if (view.getParent() instanceof ViewManager) {
            try {
                ((ViewManager) view.getParent()).removeView(view);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
