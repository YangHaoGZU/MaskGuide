package com.leaa.maskguide.library;

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
                    if (param.obj instanceof Float && !rectF.isEmpty()) {
                        mEraserCanvas.drawRoundRect(rectF, (Float) param.obj, (Float) param.obj, mEraserPaint);
                    }
                    break;
                case IMaskStep.EraserParam.ERASER_TYPE_CUSTOM:
                    if (param.obj instanceof IMaskStep.OnEraseDraw) {
                        ((IMaskStep.OnEraseDraw) param.obj).onEraseDraw(param, rectF);
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
