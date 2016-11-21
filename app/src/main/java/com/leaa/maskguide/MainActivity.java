package com.leaa.maskguide;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import com.leaa.maskguide.library.IMaskStep;
import com.leaa.maskguide.library.MaskViewHelper;
import com.leaa.maskguide.library.ViewTargetMaskStep;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<IMaskStep> stepList = new ArrayList<>();
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

            stepList.add(step.build());
        }
        {
            TextView textView = new TextView(this);
            textView.setTextSize(13);
            textView.setText("还有这里");
            textView.setTextColor(Color.WHITE);

            ViewTargetMaskStep.Builder step = ViewTargetMaskStep.newBuilder().
                    setTargetView(findViewById(R.id.button2)).
                    setEraserParam(IMaskStep.EraserParam.build(IMaskStep.EraserParam.ERASER_TYPE_OVAL, null))
                    .setStepViewParam(IMaskStep.StepViewParam.build(
                            new PointF(0f, 0.5f),
                            new PointF(1f, 0.5f),
                            0, 0
                    )).setStepView(textView)
                    .convertTo(ViewTargetMaskStep.Builder.class);

            stepList.add(step.build());
        }
        {
            ViewTargetMaskStep.Builder builder = ViewTargetMaskStep.newBuilder()
                    .setTargetView(findViewById(R.id.button3))
                    .setEraserParam(IMaskStep.EraserParam.ERASER_TYPE_ROUNDED, 10f)
                    .setStepView(this, R.layout.guide_one)
                    .setStepViewParam(
                            new PointF(0.5f, 0f),
                            new PointF(0f, 1f),
                            0, -10
                    ).setStepType(IMaskStep.STEP_TYPE_TOGETHER)
                    .convertTo(ViewTargetMaskStep.Builder.class);

            stepList.add(builder.build());
        }
        MaskViewHelper.show(this, stepList);
    }
}
