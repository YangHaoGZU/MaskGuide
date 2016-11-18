package com.leaa.maskguide.library;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by 杨浩 on 2016/11/18.
 */
public class MaskStepWrapper implements IMaskStep {
    private RectF mEraseRect;
    private EraserParam mEraserParam;
    private View mStepView;
    private StepViewParam mStepViewParam;
    private int mStepType;
    private OnStepListener mOnStepListener;

    public MaskStepWrapper() {

    }

    public MaskStepWrapper(Builder builder) {
        mEraseRect = builder.eraseRect != null ? builder.eraseRect : new RectF();
        mEraserParam = builder.eraserParam != null ? builder.eraserParam
                : EraserParam.build(EraserParam.ERASER_TYPE_RECT, null);
        mStepView = builder.stepView;
        mStepViewParam = builder.stepViewParam != null ? builder.stepViewParam
                : StepViewParam.build(new PointF(), new PointF(), 0, 0);
        mStepType = builder.stepType;
        mOnStepListener = builder.onStepListener;
    }

    public void setEraseRect(RectF eraseRect) {
        mEraseRect = eraseRect;
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

    public void setStepType(int stepType) {
        mStepType = stepType;
    }

    public void setOnStepListener(OnStepListener onStepListener) {
        mOnStepListener = onStepListener;
    }

    @Override
    public RectF getEraseRect() {
        return mEraseRect;
    }

    @Override
    public EraserParam getEraseParam() {
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
    public int getStepType() {
        return mStepType;
    }

    @Override
    public void onStepEnter(IMaskStep step) {
        if (mOnStepListener != null) {
            mOnStepListener.onStepEnter(step);
        }
    }

    @Override
    public void onStepExit(IMaskStep step) {
        if (mOnStepListener != null) {
            mOnStepListener.onStepEnter(step);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        RectF eraseRect;
        EraserParam eraserParam;
        View stepView;
        StepViewParam stepViewParam;
        int stepType;
        OnStepListener onStepListener;

        public Builder setEraseRect(RectF eraseRect) {
            this.eraseRect = eraseRect;
            return this;
        }

        public Builder setEraserParam(int eraserType, Object obj) {
            this.eraserParam = EraserParam.build(eraserType, obj);
            return this;
        }

        public Builder setEraserParam(EraserParam eraserParam) {
            this.eraserParam = eraserParam;
            return this;
        }

        public Builder setStepView(View stepView) {
            this.stepView = stepView;
            return this;
        }

        public Builder setStepView(Context context, @LayoutRes int layoutID) {
            this.stepView = LayoutInflater.from(context).inflate(layoutID, null);
            return this;
        }

        public Builder setStepViewParam(PointF targetAnchor, PointF selfAnchor, int offsetX, int offsetY) {
            this.stepViewParam = StepViewParam.build(targetAnchor, selfAnchor, offsetX, offsetY);
            return this;
        }

        public Builder setStepViewParam(StepViewParam stepViewParam) {
            this.stepViewParam = stepViewParam;
            return this;
        }

        public Builder setStepType(int stepType) {
            this.stepType = stepType;
            return this;
        }

        public Builder setOnStepListener(OnStepListener onStepListener) {
            this.onStepListener = onStepListener;
            return this;
        }

        final public <T extends Builder> T convertTo(Class<T> clazz) throws ClassCastException {
            return (T) this;
        }

        public IMaskStep build() {
            return new MaskStepWrapper(this);
        }
    }

    public interface OnStepListener {

        void onStepEnter(IMaskStep step);

        void onStepExit(IMaskStep step);
    }
}
