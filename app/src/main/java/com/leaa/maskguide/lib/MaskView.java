package com.leaa.maskguide.lib;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨浩 on 2016/11/29.
 * 遮罩。顺序显示引导页。
 * 参数信息见 {@link Guides}
 */
public class MaskView extends FrameLayout {

    private List<GuidePage> mGuidePages = new ArrayList<>();
    private Runnable mPendingNextSteps = new Runnable() {
        @Override
        public void run() {
            showNextStep();
        }
    };
    private int mCurrentIndex = 0;
    private GuidePage mCurrentGuidePage = null;

    private boolean mAutoStart = true;
    private boolean mAutoDismiss = false;
    private boolean mAutoNext = true;
    private boolean mClickToNext = true;
    private OnClickListener mClickListener = null;
    private int mMaskColor;
    private MaskView.OnMaskListener mMaskListener = null;

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
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
                if (mCurrentGuidePage != null && mCurrentGuidePage.getClickListener() != null) {
                    mCurrentGuidePage.getClickListener().onClick(v);
                }
                if (mClickToNext) {
                    showNextStep();
                }
            }
        });
    }

    public void setAutoStart(boolean autoStart) {
        mAutoStart = autoStart;
    }

    public void setAutoDismiss(boolean autoDismiss) {
        mAutoDismiss = autoDismiss;
    }

    public void setAutoNext(boolean autoNext) {
        mAutoNext = autoNext;
    }

    public void setClickToNext(boolean clickToNext) {
        mClickToNext = clickToNext;
    }

    public void setOnMaskClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setMaskColor(int maskColor) {
        mMaskColor = maskColor;
    }

    public void setOnMaskListener(MaskView.OnMaskListener maskListener) {
        mMaskListener = maskListener;
    }

    public void setGuidePages(List<GuidePage> guidePages) {
        mGuidePages.clear();
        mGuidePages.addAll(guidePages);
    }

    /**
     * 显示当前引导页。如果当前引导页不存在，则会触发引导页关闭
     */
    public void showGuide() {
        //移除当前步骤
        for (int i = 0; i < getChildCount(); ) {
            View view = getChildAt(i);
            if (view.getLayoutParams() instanceof MaskView.LayoutParams) {
                removeViewAt(i);
            } else {
                i++;
            }
        }
        if (mCurrentIndex >= mGuidePages.size()) {
            if (mAutoDismiss) {
                ViewHelper.removeViewFromParent(this);
            }
        } else {
            mCurrentGuidePage = mGuidePages.get(mCurrentIndex);
            List<View> extraViews = mCurrentGuidePage.getExtraViews();
            for (View extraView : extraViews) {
                addView(extraView, generateGuideExtraViewLayoutParams());
            }
            showStep();
        }
    }

    /**
     * 切换到下一个引导页
     */
    public void showNextGuide() {
        mCurrentIndex++;
        showGuide();
    }

    /**
     * 显示当前步骤。如果当前步骤为空，会切换到下一个引导页
     */
    public void showStep() {
        List<IMaskStep> steps = null;
        if (mCurrentGuidePage != null) {
            steps = mCurrentGuidePage.getCurrentMaskSteps();
        }
        if (steps == null || steps.isEmpty()) {
            showNextGuide();
        } else {
            showStepsView(steps);
        }
    }

    /**
     * 显示下一个步骤。如果当前引导页没有下一步了，则会切换到下一个引导页
     */
    public void showNextStep() {
        List<IMaskStep> steps = null;
        if (mCurrentGuidePage != null) {
            //通知当前步骤，步骤结束了
            List<IMaskStep> currentSteps = mCurrentGuidePage.getCurrentMaskSteps();
            for (IMaskStep currentStep : currentSteps) {
                currentStep.onStepOver(currentStep, this);
            }
            //获取到下一批步骤
            steps = mCurrentGuidePage.getNextMaskSteps();
        }
        if (steps == null || steps.isEmpty()) {
            showNextGuide();
        } else {
            showStepsView(steps);
        }
    }

    private void showStepsView(List<IMaskStep> steps) {
        removeCallbacks(mPendingNextSteps);

        int stepDuration = 0;
        for (final IMaskStep step : steps) {
            if (step.getStepDuration() > stepDuration) {
                stepDuration = step.getStepDuration();
            }

            final View view = step.getStepView();//获取显示提示信息的控件
            if (view == null) {
                step.onStepEnter(step, MaskView.this);
            } else {
                ViewGroup.LayoutParams rawParams = view.getLayoutParams();
                MaskView.LayoutParams layoutParams;
                if (rawParams instanceof LayoutParams) {
                    layoutParams = (LayoutParams) rawParams;
                } else if (rawParams != null) {
                    layoutParams = new LayoutParams(rawParams);
                } else {
                    layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                layoutParams.maskStep = step;
                view.setLayoutParams(layoutParams);
                view.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        step.onStepEnter(step, MaskView.this);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                    }
                });

                addView(view);//将提示信息的控件添加到当前的显示背景中
            }
        }
        if (mAutoNext && stepDuration > 0) {
            postDelayed(mPendingNextSteps, stepDuration);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mMaskListener != null) {
            mMaskListener.onMaskShow(this);
        }
        if (mAutoStart) {
            showGuide();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mPendingNextSteps);
        if (mMaskListener != null) {
            mMaskListener.onMaskHide(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /*
         * 1、设置显示View的背景 -- 即蒙层的效果
         */
        EraserDrawable layer;
        if (getBackground() == null || !(getBackground() instanceof EraserDrawable)) {
            layer = new EraserDrawable(mMaskColor);
            setBackground(layer);
        } else {
            layer = (EraserDrawable) getBackground();
        }
        layer.reset();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE
                    || !(child.getLayoutParams() instanceof MaskView.LayoutParams)) {
                continue;
            }
            MaskView.LayoutParams params = (MaskView.LayoutParams) child.getLayoutParams();
            IMaskStep maskStep = params.maskStep;
            if (maskStep == null) {
                continue;
            }
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            //添加擦除区域
            if (maskStep.getEraseRect() != null && maskStep.getEraseParam() != null) {
                layer.addEraserRect(maskStep.getEraseRect(), maskStep.getEraseParam());
            }
            //布局步骤的View
            if (maskStep.getStepViewParam() != null) {
                if (maskStep.getEraseRect() != null) {
                    //如果擦除区域不为空，则相对擦除区域来进行定位
                    RectF targetRectF = maskStep.getEraseRect();
                    IMaskStep.StepViewParam viewParam = maskStep.getStepViewParam();

                    int viewLeft = (int) (targetRectF.left + targetRectF.width() * viewParam.targetAnchor.x
                            - width * viewParam.selfAnchor.x + viewParam.offsetX);
                    int viewTop = (int) (targetRectF.top + targetRectF.height() * viewParam.targetAnchor.y
                            - height * viewParam.selfAnchor.y + viewParam.offsetY);

                    child.layout(viewLeft, viewTop, viewLeft + width, viewTop + height);
                } else {
                    //如果擦除区域为空，则相对整个MaskView来进行定位
                    IMaskStep.StepViewParam viewParam = maskStep.getStepViewParam();

                    int viewLeft = (int) (getWidth() * viewParam.targetAnchor.x
                            - width * viewParam.selfAnchor.x + viewParam.offsetX);
                    int viewTop = (int) (getHeight() * viewParam.targetAnchor.y
                            - height * viewParam.selfAnchor.y + viewParam.offsetY);

                    child.layout(viewLeft, viewTop, viewLeft + width, viewTop + height);
                }
            } else {
                child.layout(0, 0, width, height);
            }
        }
    }

    @Override
    protected FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams generateExtraViewLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams generateGuideExtraViewLayoutParams() {
        return new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        private IMaskStep maskStep = null;

        public LayoutParams(ViewGroup.LayoutParams params) {
            super(params);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public interface OnMaskListener {
        void onMaskShow(MaskView view);

        void onMaskHide(MaskView view);
    }
}
