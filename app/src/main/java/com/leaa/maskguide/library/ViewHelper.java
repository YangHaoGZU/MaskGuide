package com.leaa.maskguide.library;

import android.app.Activity;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by 杨浩 on 2016/11/18.
 */
public class ViewHelper {
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
}
