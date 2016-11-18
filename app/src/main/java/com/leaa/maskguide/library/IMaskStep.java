package com.leaa.maskguide.library;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by 杨浩 on 2016/11/17.
 * 遮罩步骤
 */
public interface IMaskStep {
    public interface OnEraseDraw{
        void onEraseDraw(EraserParam param, RectF rectF);
    }

    public static class EraserParam {
        public static final int ERASER_TYPE_RECT = 0;
        public static final int ERASER_TYPE_CIRCLE = 1;
        public static final int ERASER_TYPE_ROUNDED = 2;
        public static final int ERASER_TYPE_CUSTOM = 3;

        int eraserType;
        Object obj;

        public static EraserParam build(int eraserType, Object obj) {
            EraserParam param = new EraserParam();
            param.eraserType = eraserType;
            param.obj = obj;
            return param;
        }
    }

    public static class StepViewParam {
        PointF targetAnchor;
        PointF selfAnchor;

        int offsetX;
        int offsetY;

        public static StepViewParam build(PointF targetAnchor, PointF selfAnchor, int offsetX, int offsetY) {
            StepViewParam param = new StepViewParam();
            param.targetAnchor = targetAnchor;
            param.selfAnchor = selfAnchor;
            param.offsetX = offsetX;
            param.offsetY = offsetY;
            return param;
        }
    }

    RectF getEraserRect();

    EraserParam getEraserParam();

    View getStepView();

    StepViewParam getStepViewParam();

    void onStepEnter();

    void onStepExit();
}
