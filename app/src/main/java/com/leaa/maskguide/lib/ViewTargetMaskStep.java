package com.leaa.maskguide.lib;

import android.graphics.RectF;
import android.view.View;

/**
 * Created by 杨浩 on 2016/11/17.
 * 以一个View为目标区域的引导步骤
 */
public class ViewTargetMaskStep extends MaskStepWrapper {

    private View mTargetView;

    public ViewTargetMaskStep() {

    }

    public ViewTargetMaskStep(Builder builder) {
        super(builder);
        mTargetView = builder.targetView;
    }

    public void setTargetView(View view) {
        mTargetView = view;
    }

    @Override
    public RectF getEraseRect() {
        if (mTargetView != null) {
            return ViewHelper.getViewBounds(mTargetView);
        } else {
            return super.getEraseRect();
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends MaskStepWrapper.Builder {
        View targetView;

        public Builder setTargetView(View targetView) {
            this.targetView = targetView;
            return this;
        }

        @Override
        public ViewTargetMaskStep build() {
            return new ViewTargetMaskStep(this);
        }
    }
}
