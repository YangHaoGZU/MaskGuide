package com.leaa.maskguide;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import com.leaa.maskguide.lib.GuidePage;
import com.leaa.maskguide.lib.Guides;
import com.leaa.maskguide.lib.IMaskStep;
import com.leaa.maskguide.lib.ViewTargetMaskStep;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IMaskStep step1;
        {
            TextView textView = new TextView(this);
            textView.setTextSize(13);
            textView.setText("就是这里");
            textView.setTextColor(Color.WHITE);

            ViewTargetMaskStep.Builder step = ViewTargetMaskStep.newBuilder().
                    setTargetView(findViewById(R.id.button)).
                    setEraserParam(IMaskStep.EraserParam.build(IMaskStep.EraserParam.ERASER_TYPE_ROUNDED, 10f))
                    .setStepViewParam(IMaskStep.StepViewParam.build(
                            new PointF(1, 1),
                            new PointF(0.2f, 0.2f),
                            0, 0
                    )).setStepView(textView)
                    .convertTo(ViewTargetMaskStep.Builder.class);
            step1 = step.build();
        }
        IMaskStep step2;
        {
            TextView textView = new TextView(this);
            textView.setTextSize(13);
            textView.setText("还有这里");
            textView.setTextColor(Color.WHITE);

            ViewTargetMaskStep.Builder step = ViewTargetMaskStep.newBuilder().
                    setTargetView(findViewById(R.id.button2)).
                    setEraserParam(IMaskStep.EraserParam.build(IMaskStep.EraserParam.ERASER_TYPE_OVAL, null))
                    .setStepViewParam(IMaskStep.StepViewParam.build(
                            new PointF(0.5f, 1f),
                            new PointF(0.5f, -0.1f),
                            0, 0
                    )).setStepView(textView)
                    .convertTo(ViewTargetMaskStep.Builder.class);
            step2 = step.build();
        }
        IMaskStep step3;
        {
            ViewTargetMaskStep.Builder step = ViewTargetMaskStep.newBuilder()
                    .setTargetView(findViewById(R.id.button3))
                    .setEraserParam(IMaskStep.EraserParam.ERASER_TYPE_ROUNDED, 10f)
                    .setStepView(this, R.layout.guide_one)
                    .setStepViewParam(
                            new PointF(0.5f, 0f),
                            new PointF(0f, 1f),
                            0, -10
                    ).setStepType(IMaskStep.STEP_TYPE_TOGETHER)
                    .convertTo(ViewTargetMaskStep.Builder.class);
            step3 = step.build();
        }
        IMaskStep step4;
        {
            TextView textView = new TextView(this);
            textView.setTextSize(13);
            textView.setText("就是这里");
            textView.setTextColor(Color.WHITE);

            ViewTargetMaskStep.Builder step = ViewTargetMaskStep.newBuilder().
                    setTargetView(findViewById(R.id.button3)).
                    setEraserParam(IMaskStep.EraserParam.build(IMaskStep.EraserParam.ERASER_TYPE_ROUNDED, 10f))
                    .setStepViewParam(IMaskStep.StepViewParam.build(
                            new PointF(1, 1),
                            new PointF(0.2f, 0.2f),
                            0, 0
                    )).setStepView(textView)
                    .convertTo(ViewTargetMaskStep.Builder.class);
            step4 = step.build();
        }

        Guides.newGuides()
                .maskColor(Color.parseColor("#44000000"))
                .autoDismiss(true)
                .autoNext(true)
                .autoStart(true)
                .addGuidePage(GuidePage.newGuide(step1,step2,step3))
                .addGuidePage(GuidePage.newGuide(step3,step4))
                .show(this);
    }
}
