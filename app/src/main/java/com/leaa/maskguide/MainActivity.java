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
            ViewTargetMaskStep step = new ViewTargetMaskStep();
            step.setTargetView(findViewById(R.id.button));
            step.setEraserParam(IMaskStep.EraserParam.build(IMaskStep.EraserParam.ERASER_TYPE_ROUNDED, 10f));
            step.setStepViewParam(IMaskStep.StepViewParam.build(
                    new PointF(1, 1),
                    new PointF(0.2f, 0.2f),
                    0, 0
            ));

            TextView textView = new TextView(this);
            textView.setTextSize(13);
            textView.setText("就是这里");
            textView.setTextColor(Color.WHITE);
            step.setStepView(textView);

            stepList.add(step);
        }
        {
            ViewTargetMaskStep step = new ViewTargetMaskStep();
            step.setTargetView(findViewById(R.id.button2));
            step.setEraserParam(IMaskStep.EraserParam.build(IMaskStep.EraserParam.ERASER_TYPE_CIRCLE, 0f));
            step.setStepViewParam(IMaskStep.StepViewParam.build(
                    new PointF(0.5f, 0.5f),
                    new PointF(0.5f, 0.5f),
                    0, 0
            ));

            TextView textView = new TextView(this);
            textView.setTextSize(13);
            textView.setText("还有这里");
            textView.setTextColor(Color.WHITE);
            step.setStepView(textView);

            stepList.add(step);
        }
        MaskViewHelper.show(this, stepList);
    }
}
