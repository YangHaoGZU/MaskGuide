package com.leaa.maskguide.library;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 杨浩 on 2016/11/17.
 */
public class MaskViewHelper {
    public static void show(Activity activity, IMaskStep maskSteps) {
        MaskView maskView = new MaskView(activity);
        maskView.addMaskStep(maskSteps);

        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        content.addView(maskView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static void show(Activity activity, List<IMaskStep> maskSteps) {
        MaskView maskView = new MaskView(activity);
        maskView.setMaskSteps(maskSteps);

        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        content.addView(maskView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
