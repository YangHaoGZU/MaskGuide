package com.leaa.maskguide.lib;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨浩 on 2016/11/17.
 * 引导页的蒙层背景，会根据 RectF和EraserParam 进行区域擦除，效果就是挖空对应区域，使其能看到下面的控件
 * 见{@link IMaskStep#getEraseRect()}和{@link com.leaa.maskguide.lib.IMaskStep.EraserParam}
 */
public class EraserDrawable extends Drawable {

    private List<Pair<RectF, IMaskStep.EraserParam>> mErasers = new ArrayList<>();
    private Paint mEraserPaint;
    private Paint mLayerPaint;
    private Bitmap mEraserBitmap;
    private Canvas mEraserCanvas;

    public EraserDrawable(int layerColor) {
        mLayerPaint = new Paint();
        mLayerPaint.setColor(layerColor);

        mEraserPaint = new Paint();
        mEraserPaint.setColor(Color.TRANSPARENT);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mEraserPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public void reset() {
        mErasers.clear();
        invalidateSelf();
    }

    /**
     * 添加擦除区域
     *
     * @param rect  擦除区域
     * @param param 擦除参数，详见{@link com.leaa.maskguide.lib.IMaskStep.EraserParam}
     * @return 当前对象
     */
    public EraserDrawable addEraserRect(RectF rect, IMaskStep.EraserParam param) {
        if (rect != null && param != null) {
            mErasers.add(new Pair<>(rect, param));
            invalidateSelf();
        }
        return this;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);

        mEraserBitmap = Bitmap.createBitmap(right - left, bottom - top, Bitmap.Config.ARGB_8888);
        mEraserCanvas = new Canvas(mEraserBitmap);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mEraserBitmap.eraseColor(Color.TRANSPARENT);
        mEraserCanvas.drawPaint(mLayerPaint);
        for (Pair<RectF, IMaskStep.EraserParam> eraser : mErasers) {
            RectF rectF = eraser.first;
            IMaskStep.EraserParam param = eraser.second;
            switch (param.eraserType) {
                case IMaskStep.EraserParam.ERASER_TYPE_OVAL:
                    if (!rectF.isEmpty()) {
                        mEraserCanvas.drawOval(rectF, mEraserPaint);
                    }
                    break;
                case IMaskStep.EraserParam.ERASER_TYPE_RECT:
                    if (!rectF.isEmpty()) {
                        mEraserCanvas.drawRect(rectF, mEraserPaint);
                    }
                    break;
                case IMaskStep.EraserParam.ERASER_TYPE_ROUNDED:
                    if (!rectF.isEmpty()) {
                        float radius = 0;
                        if (param.obj instanceof Float) {
                            radius = (float) param.obj;
                        } else if (param.obj instanceof Integer) {
                            radius = (int) param.obj;
                        } else if (param.obj instanceof Long) {
                            radius = (long) param.obj;
                        }
                        mEraserCanvas.drawRoundRect(rectF, radius, radius, mEraserPaint);
                    }
                case IMaskStep.EraserParam.ERASER_TYPE_CIRCLE:
                    if (!rectF.isEmpty()) {
                        float radius = 0;
                        if (param.obj instanceof Float) {
                            radius = (float) param.obj;
                        } else if (param.obj instanceof Integer) {
                            radius = (int) param.obj;
                        } else if (param.obj instanceof Long) {
                            radius = (long) param.obj;
                        }
                        mEraserCanvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, mEraserPaint);
                    }
                    break;
                case IMaskStep.EraserParam.ERASER_TYPE_CUSTOM:
                    if (param.obj instanceof IMaskStep.OnEraseDraw) {
                        ((IMaskStep.OnEraseDraw) param.obj).onEraseDraw(param, rectF, mEraserCanvas);
                    }
                    break;
            }
        }
        canvas.drawBitmap(mEraserBitmap, 0, 0, null);
    }

    @Override
    public void setAlpha(int alpha) {
        mLayerPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
