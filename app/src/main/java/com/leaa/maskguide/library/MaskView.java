package com.leaa.maskguide.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨浩 on 2016/11/17.
 */
public class MaskView extends FrameLayout {

    private List<IMaskStep> mMaskSteps = new ArrayList<>();

    public MaskView(Context context) {
        super(context);
    }

    public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addMaskStep(IMaskStep maskStep) {
        mMaskSteps.add(maskStep);
    }

    public void setMaskSteps(List<IMaskStep> steps) {
        mMaskSteps.addAll(steps);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            initMaskView();
        }
    }

    private void initMaskView() {
        EraserDrawable layer;
        if (getBackground() == null || !(getBackground() instanceof EraserDrawable)) {
            layer = new EraserDrawable(Color.BLACK);
            layer.setAlpha(150);
            setBackground(layer);
        } else {
            layer = (EraserDrawable) getBackground();
        }
        layer.reset();
        removeAllViews();
        for (IMaskStep step : mMaskSteps) {
            layer.addEraserRect(step.getEraserRect(), step.getEraserParam());

            RectF targetRectF = step.getEraserRect();
            IMaskStep.StepViewParam viewParam = step.getStepViewParam();

            View view = step.getStepView();
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = (int) (targetRectF.top + targetRectF.height() * viewParam.targetAnchor.y
                    - view.getMeasuredHeight() * viewParam.selfAnchor.y + viewParam.offsetY);
            layoutParams.leftMargin = (int) (targetRectF.left + targetRectF.width() * viewParam.targetAnchor.x
                    - view.getMeasuredWidth() * viewParam.selfAnchor.x + viewParam.offsetX);

            addView(view, layoutParams);
        }
    }
}
