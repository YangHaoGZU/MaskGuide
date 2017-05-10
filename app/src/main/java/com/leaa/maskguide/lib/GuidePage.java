package com.leaa.maskguide.lib;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨浩 on 2016/11/25.
 * 一个引导页。
 * 一个引导页包含若干个步骤，所有步骤都共存于一个遮罩页面内。步骤可以依次出现也可以一起出现，
 * 但已出现的步骤不会消失，直至切换到下一个引导页，所有步骤才会消失
 * 引导页可以添加额外的View。额外的View会一直存在，直至切换到下一个引导页
 */
public class GuidePage {
    /**
     * 点击遮罩时的点击事件
     */
    private View.OnClickListener mClickListener = null;
    private List<IMaskStep> mMaskSteps = new ArrayList<>();
    private List<View> mExtraViews = new ArrayList<>();
    private int mCurrentIndex = 0;

    public GuidePage onMaskClick(View.OnClickListener clickListener) {
        mClickListener = clickListener;
        return this;
    }

    public GuidePage addMaskStep(List<IMaskStep> maskSteps) {
        mMaskSteps.addAll(maskSteps);
        return this;
    }

    public GuidePage addMaskStep(IMaskStep maskStep) {
        mMaskSteps.add(maskStep);
        return this;
    }

    public GuidePage addExtraView(List<View> extraViews) {
        mExtraViews.addAll(extraViews);
        return this;
    }

    public GuidePage addExtraView(View extraView) {
        mExtraViews.add(extraView);
        return this;
    }

    public View.OnClickListener getClickListener() {
        return mClickListener;
    }

    public List<IMaskStep> getMaskSteps() {
        return mMaskSteps;
    }

    public List<View> getExtraViews() {
        return mExtraViews;
    }

    /**
     * 获取当前步骤
     * @return
     */
    public List<IMaskStep> getCurrentMaskSteps() {
        List<IMaskStep> steps = new ArrayList<>();
        int index = mCurrentIndex;
        for (; index < mMaskSteps.size(); index++) {
            /*
             * 控制当前显示步骤中显示的控件数量
             */
            if (index == mCurrentIndex || mMaskSteps.get(index).getStepType() == IMaskStep.STEP_TYPE_TOGETHER) {
                steps.add(mMaskSteps.get(index));
            } else {
                break;
            }
        }
        return steps;
    }

    /**
     * 获取下一步骤
     * @return
     */
    public List<IMaskStep> getNextMaskSteps() {
        int index = mCurrentIndex;
        for (; index < mMaskSteps.size(); index++) {
            if (index != mCurrentIndex && mMaskSteps.get(index).getStepType() == IMaskStep.STEP_TYPE_NEXT) {
                break;
            }
        }
        mCurrentIndex = index;
        return getCurrentMaskSteps();
    }

    public static GuidePage newGuide() {
        return new GuidePage();
    }

    public static GuidePage newGuide(IMaskStep... steps) {
        GuidePage guidePage = new GuidePage();
        for (IMaskStep step : steps) {
            guidePage.addMaskStep(step);
        }
        return guidePage;
    }
}
