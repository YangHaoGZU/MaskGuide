package com.leaa.maskguide.lib;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨浩 on 2016/11/29.
 * 遮罩引导。该类是创建并显示遮罩的工具类。
 * 一个遮罩可以顺序的显示多个引导页。
 * 一个引导页可以包括多个步骤
 */
public class Guides {

    /**
     * 是否自动开始引导。为true，则当遮罩出现时，会自动显示第一个引导页
     */
    private boolean mAutoStart = true;
    /**
     * 是否自动结束引导，为true，则当引导页播放完毕时，会自动隐藏遮罩
     */
    private boolean mAutoDismiss = false;
    /**
     * 是否自动开始下一步，为true，则在当前步骤达到持续时长后，自动切换到下一步
     * 注意如果当前步骤的{@link IMaskStep#getStepDuration()}为0，则不会触发该属性
     */
    private boolean mAutoNext = true;
    /**
     * 是否点击后开始下一步，为true，则当点击遮罩时，切换下一步
     */
    private boolean mClickToNext = true;
    /**
     * 点击遮罩时的点击事件
     */
    private View.OnClickListener mClickListener = null;
    /**
     * 遮罩颜色
     */
    private int mMaskColor = Color.parseColor("#D8000000");
    /**
     * 遮罩显示和隐藏的事件
     */
    private MaskView.OnMaskListener mMaskListener = null;
    /**
     * 引导页列表
     */
    private List<GuidePage> mGuidePageList = new ArrayList<>();
    /**
     * 额外的View。该部分View会被添加到遮罩上，直至遮罩消失
     */
    private List<View> mExtraViews = new ArrayList<>();

    public Guides autoStart(boolean value) {
        mAutoStart = value;
        return this;
    }

    public Guides autoDismiss(boolean value) {
        mAutoDismiss = value;
        return this;
    }

    public Guides autoNext(boolean value) {
        mAutoNext = value;
        return this;
    }

    public Guides clickToNext(boolean value) {
        mClickToNext = value;
        return this;
    }

    public Guides maskColor(int color) {
        mMaskColor = color;
        return this;
    }

    public Guides onMaskListener(MaskView.OnMaskListener listener) {
        mMaskListener = listener;
        return this;
    }

    public Guides onMaskClick(View.OnClickListener clickListener) {
        mClickListener = clickListener;
        return this;
    }

    public Guides addGuidePages(List<GuidePage> guidePages) {
        mGuidePageList.addAll(guidePages);
        return this;
    }

    public Guides addGuidePage(GuidePage guidePage) {
        mGuidePageList.add(guidePage);
        return this;
    }

    public Guides addExtraView(List<View> extraViews) {
        mExtraViews.addAll(extraViews);
        return this;
    }

    public Guides addExtraView(View extraView) {
        mExtraViews.add(extraView);
        return this;
    }

    public static Guides newGuides() {
        return new Guides();
    }

    public MaskView show(Activity activity) {
        MaskView maskView = new MaskView(activity);
        maskView.setAutoStart(mAutoStart);
        maskView.setAutoDismiss(mAutoDismiss);
        maskView.setAutoNext(mAutoNext);
        maskView.setClickToNext(mClickToNext);
        maskView.setMaskColor(mMaskColor);
        maskView.setOnMaskClickListener(mClickListener);
        maskView.setOnMaskListener(mMaskListener);
        maskView.setGuidePages(mGuidePageList);
        for (View view : mExtraViews) {
            maskView.addView(view);
        }

        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        content.addView(maskView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//当前显示基础上覆盖一层View

        return maskView;
    }

    public static void dismiss(Activity activity) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0; i < content.getChildCount(); ) {
            View view = content.getChildAt(i);
            if (view instanceof MaskView) {
                content.removeViewAt(i);
            } else {
                i++;
            }
        }
    }
}
