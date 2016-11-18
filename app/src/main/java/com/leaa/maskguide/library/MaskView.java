package com.leaa.maskguide.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
    private int mCurrentIndex = 0;
    private boolean mHasShown = false;

    public MaskView(Context context) {
        super(context);
        init();
    }

    public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    public void addMaskStep(IMaskStep maskStep) {
        mMaskSteps.add(maskStep);
    }

    public void setMaskSteps(List<IMaskStep> steps) {
        mMaskSteps.addAll(steps);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && !mHasShown) {
            mHasShown = false;
            next();
        }
    }

    private void showStep(List<IMaskStep> steps) {
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
        for (final IMaskStep step : steps) {
            layer.addEraserRect(step.getEraseRect(), step.getEraseParam());

            RectF targetRectF = step.getEraseRect();
            IMaskStep.StepViewParam viewParam = step.getStepViewParam();

            final View view = step.getStepView();
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = (int) (targetRectF.top + targetRectF.height() * viewParam.targetAnchor.y
                    - view.getMeasuredHeight() * viewParam.selfAnchor.y + viewParam.offsetY);
            layoutParams.leftMargin = (int) (targetRectF.left + targetRectF.width() * viewParam.targetAnchor.x
                    - view.getMeasuredWidth() * viewParam.selfAnchor.x + viewParam.offsetX);

            view.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    step.onStepEnter(step);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    step.onStepExit(step);
                }
            });
            addView(view, layoutParams);
        }
    }

    private void next() {
        List<IMaskStep> steps = new ArrayList<>();
        int index = mCurrentIndex;
        for (; index < mMaskSteps.size(); index++) {
            if (index == mCurrentIndex || mMaskSteps.get(index).getStepType() == IMaskStep.STEP_TYPE_TOGETHER) {
                steps.add(mMaskSteps.get(index));
            } else {
                break;
            }
        }
        mCurrentIndex = index;
        showStep(steps);
        if (steps.isEmpty()) {
            setVisibility(GONE);
        }
    }
}
