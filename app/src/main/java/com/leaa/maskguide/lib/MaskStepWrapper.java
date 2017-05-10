package com.leaa.maskguide.lib;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by 杨浩 on 2016/11/18.
 * 使用builder来简化MaskStep的创建
 */
public class MaskStepWrapper implements IMaskStep {
    private RectF mEraseRect;
    private EraserParam mEraserParam;
    private View mStepView;
    private StepViewParam mStepViewParam;
    private int mStepType;
    private int mStepDuration;
    private OnStepListener mOnStepListener;

    public MaskStepWrapper() {

    }

    public MaskStepWrapper(Builder builder) {
        mEraseRect = builder.eraseRect;
        mEraserParam = builder.eraserParam;
        mStepView = builder.stepView;
        mStepViewParam = builder.stepViewParam;
        mStepType = builder.stepType;
        mStepDuration = builder.stepDuration;
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

    public void setStepDuration(int stepDuration) {
        mStepDuration = stepDuration;
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

    public int getStepDuration() {
        return mStepDuration;
    }

    @Override
    public void onStepEnter(IMaskStep step, MaskView maskView) {
        if (mOnStepListener != null) {
            mOnStepListener.onStepEnter(step, maskView);
        }
    }

    @Override
    public void onStepOver(IMaskStep step, MaskView maskView) {
        if (mOnStepListener != null) {
            mOnStepListener.onStepOver(step, maskView);
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
        int stepDuration;
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

        public Builder setStepDuration(int duration) {
            this.stepDuration = duration;
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

        void onStepEnter(IMaskStep step, MaskView maskView);

        void onStepOver(IMaskStep step, MaskView maskView);
    }
}
