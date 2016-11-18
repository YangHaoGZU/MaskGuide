package com.leaa.maskguide.library;

import android.app.Activity;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by 杨浩 on 2016/11/17.
 */
public class ViewTargetMaskStep implements IMaskStep {

    private View mTargetView;
    private EraserParam mEraserParam;
    private View mStepView;
    private StepViewParam mStepViewParam;

    public void setTargetView(View view) {
        mTargetView = view;
    }

    public void setEraserParam(EraserParam eraserParam) {
        mEraserParam = eraserParam;
    }

    public void setStepView(View stepView) {
        mStepView = stepView;
    }

    public void setStepViewParam(StepViewParam stepViewParam) {
        mStepViewParam = stepViewParam;
    }

    @Override
    public RectF getEraserRect() {
        if (mTargetView != null) {
            return getViewBounds(mTargetView);
        }
        return null;
    }

    @Override
    public EraserParam getEraserParam() {
        return mEraserParam;
    }

    @Override
    public View getStepView() {
        return mStepView;
    }

    @Override
    public StepViewParam getStepViewParam() {
        return mStepViewParam;
    }

    @Override
    public void onStepEnter() {

    }

    @Override
    public void onStepExit() {

    }

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
