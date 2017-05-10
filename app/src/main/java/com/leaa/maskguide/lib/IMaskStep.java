package com.leaa.maskguide.lib;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by 杨浩 on 2016/11/17.
 * 遮罩步骤。
 * 遮罩步骤由一个擦除区域和一个提示View组成，支持空擦除区域和空View
 * 只有{@link #getEraseRect()}和{@link #getEraseParam()}都不为空时，才会生成擦除区域
 * <p>
 * 若{@link #getStepViewParam()}为空，则提示View默认位于MaskView的左上角
 * 若{@link #getStepViewParam()}不为空，则提示View使用参考锚点的方式来定位：
 * 1.若{@link #getEraseRect()}不为空，则提示View的位置参考于擦除区域
 * 2.若{@link #getEraseRect()}为空，则提示View的位置参考于MaskView的显示区域。
 * 见{@link StepViewParam}
 * <p>
 * <p>
 * 步骤分为两种类型
 * {@link #STEP_TYPE_TOGETHER} 表示当前步骤与之前的步骤一起显示
 * {@link #STEP_TYPE_NEXT} 表示当前步骤在之前步骤结束后显示
 * 例如对于步骤 a,b,c,d,e，若设置
 * a--> NEXT
 * b--> NEXT
 * c--> TOGETHER
 * d--> NEXT
 * e--> NEXT
 * 则其出现顺序为：
 * a
 * abc
 * abcd
 * abcde
 * <p>
 * 同一个引导页内，遮罩步骤依次出现时，前面的步骤不会消失
 * 切换引导页时，前一个引导页的所有步骤都会消失
 */
public interface IMaskStep {
    /**
     * 该步骤不与之前的步骤一起显示
     */
    public static final int STEP_TYPE_NEXT = 0;
    /**
     * 当前步骤与之前的步骤一起显示
     */
    public static final int STEP_TYPE_TOGETHER = 1;

    /**
     * 自定义遮罩擦除时的回调函数
     */
    public interface OnEraseDraw {
        void onEraseDraw(EraserParam param, RectF rectF, Canvas canvas);
    }

    /**
     * 遮罩擦除参数
     */
    public static class EraserParam {
        /**
         * 擦除类型：矩形。
         * 矩形范围即是{@link #getEraseRect()}。此时{@link #obj}属性被忽略
         */
        public static final int ERASER_TYPE_RECT = 0;
        /**
         * 擦除类型：椭圆。
         * 椭圆范围即是{@link #getEraseRect()}的内接椭圆。此时{@link #obj}属性被忽略
         */
        public static final int ERASER_TYPE_OVAL = 1;
        /**
         * 擦除类型：圆角矩形。
         * 矩形范围即是 {@link #getEraseRect()}
         * {@link #obj} 指定圆角的半径，接受类型为 {@link Long},{@link Integer},{@link Float} 的参数
         * 若不是以上参数类型，则默认圆角半径为 0
         */
        public static final int ERASER_TYPE_ROUNDED = 2;
        /**
         * 擦除类型：圆形。
         * 圆心即是 {@link #getEraseRect()} 的中心点
         * {@link #obj} 指定圆的半径，接受类型为 {@link Long},{@link Integer},{@link Float} 的参数
         * 若不是以上参数类型，则默认圆的半径为 0
         */
        public static final int ERASER_TYPE_CIRCLE = 3;
        /**
         * 擦除类型：自定义
         * {@link #obj} 需传入 {@link OnEraseDraw} 的实例化对象
         * 若不是以上参数，则不会产生任何效果
         */
        public static final int ERASER_TYPE_CUSTOM = 4;

        int eraserType;
        Object obj;

        /**
         * 构建遮罩的显示参数
         *
         * @param eraserType 遮罩类型
         * @param obj        自定义的遮罩类型
         * @return 遮罩参数类
         */
        public static EraserParam build(int eraserType, Object obj) {
            EraserParam param = new EraserParam();
            param.eraserType = eraserType;
            param.obj = obj;
            return param;
        }
    }

    /**
     * 指示View的位置参数
     * <p>
     * 目标区域：当擦除区域不为空时，目标区域为擦除区域。当擦除区域为空时，目标区域为MaskView的显示区域
     * <p>
     * 目标区域与指示View的位置关系使用锚点(Anchor)来描述，锚点为一个矩形的参考点，x方向的取值为矩形
     * 宽度的百分比，y方向的取值为矩形高度的百分比。例如 {0.5,0.5}即为矩形的中心点，{0, 1}即为矩形左
     * 下角，{1,1}即为矩形右下角，{1.2，0.5}则为超出矩形右侧的一个点
     * <p>
     * 在确定位置时，以目标区域的锚点位置为准，将指示View的锚点与目标区域的锚点进行重合，即确定了方位
     * <p>
     * 例如，设置目标区域的锚点为{0,1}即左下角，设置指示View的锚点为{1,0}即右上角，表示指示View的右
     * 上角与目标区域的左下角重合，即指示View位于目标区域的左下方
     * 又如，设置目标区域的锚点为{0.5,0.5}即中心点，设置指示View的锚点为{0.5,0.5}即中心点，表示指示
     * View与目标区域中心点重合，即指示View覆盖于目标区域之上。
     * <p>
     * 除了锚点之外，还可以设置偏移量来调整两者之间的位置，偏移量是在锚点重合的基础上进行辅助定位的，
     * 其在锚点重合定位完成之后，再根据偏移量来使指示View偏离重合点，调整指示View的位置
     */
    public static class StepViewParam {
        /**
         * 目标区域的锚点。
         */
        PointF targetAnchor;
        /**
         * 自身的描点。即指示View的锚点
         */
        PointF selfAnchor;

        /**
         * x方向的偏移
         */
        int offsetX;
        /**
         * y方向的偏移
         */
        int offsetY;

        public StepViewParam() {
            targetAnchor = new PointF(0, 0);
            selfAnchor = new PointF(0, 0);
        }

        /**
         * @param targetAnchor 目标空间的锚点
         * @param selfAnchor   显示步骤的锚点
         * @param offsetX      显示步骤相对目标控件X坐标的偏移
         * @param offsetY      显示步骤相对目标控件Y坐标的偏移
         * @return 显示步骤显示参数
         */
        public static StepViewParam build(PointF targetAnchor, PointF selfAnchor, int offsetX, int offsetY) {
            StepViewParam param = new StepViewParam();
            param.targetAnchor = targetAnchor;
            param.selfAnchor = selfAnchor;
            param.offsetX = offsetX;
            param.offsetY = offsetY;
            return param;
        }
    }

    /**
     * 获取该步骤的擦除区域，该区域指定的位置的遮罩是透明的
     *
     * @return 将被擦除的区域
     */
    RectF getEraseRect();

    /**
     * 获取擦除参数，见 {@link EraserParam}
     *
     * @return 擦除参数
     */
    EraserParam getEraseParam();

    /**
     * 获取指示用的View。该View会被添加到遮罩层
     * 该View不能是被添加过的View，否则会报"The specified child already has a parent."的错误
     *
     * @return 指示用的View
     */
    View getStepView();

    /**
     * 获取指示View的参数，见 {@link StepViewParam}
     *
     * @return 指示View的参数
     */
    StepViewParam getStepViewParam();

    /**
     * 获取步骤类型。见{@link #STEP_TYPE_NEXT}和{@link #STEP_TYPE_TOGETHER}
     *
     * @return 步骤类型
     */
    int getStepType();

    /**
     * 获取步骤的持续时长。持续时长之后，将会自动进入下一个步骤
     *
     * @return 步骤的持续时长
     */
    int getStepDuration();

    /**
     * 当进入该步骤时的通知。该通知会在指示View被添加到遮罩层上时调用
     *
     * @param step     步骤
     * @param maskView 遮罩层
     */
    void onStepEnter(IMaskStep step, MaskView maskView);

    /**
     * 当该步骤结束时的通知。该通知会在开始下一步之前调用
     *
     * @param step     步骤
     * @param maskView 遮罩层
     */
    void onStepOver(IMaskStep step, MaskView maskView);
}
