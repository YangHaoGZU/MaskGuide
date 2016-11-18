package com.leaa.maskguide.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.leaa.maskguide.R;

/**
 * Created by 杨浩 on 2016/11/18.
 */
public class LinkView extends View {

    private final int TURN_TYPE_BASE_START_X = 0;
    private final int TURN_TYPE_BASE_END_X = 1;
    private final int TURN_TYPE_DIRECT = 2;

    private int mStartPointColor;
    private int mStartPointRadius;
    private int mEndPointColor;
    private int mEndPointRadius;
    private int mLineColor;
    private int mLineWidth;
    private float mStartX;
    private float mStartY;
    private float mEndX;
    private float mEndY;
    private int mTurnType;

    private PointF mStartPoint;
    private PointF mEndPoint;
    private Path mLinePath;

    private Paint mPaint;
    private Bitmap mCacheBitmap;
    private Canvas mCacheCanvas;

    public LinkView(Context context) {
        super(context);
        init(context, null);
    }

    public LinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LinkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.LinkView);
        mStartPointColor = a.getColor(R.styleable.LinkView_start_point_color, Color.BLACK);
        mStartPointRadius = a.getDimensionPixelSize(R.styleable.LinkView_start_point_radius, 10);
        mEndPointColor = a.getColor(R.styleable.LinkView_end_point_color, Color.BLACK);
        mEndPointRadius = a.getDimensionPixelSize(R.styleable.LinkView_end_point_radius, 10);
        mLineColor = a.getColor(R.styleable.LinkView_line_color, Color.BLACK);
        mLineWidth = a.getDimensionPixelSize(R.styleable.LinkView_line_width, 5);
        mStartX = a.getFloat(R.styleable.LinkView_startX, 0);
        mStartY = a.getFloat(R.styleable.LinkView_startY, 0);
        mEndX = a.getFloat(R.styleable.LinkView_endX, 1);
        mEndY = a.getFloat(R.styleable.LinkView_endY, 1);
        mTurnType = a.getInt(R.styleable.LinkView_turn_type, 0);
        a.recycle();

        mStartPoint = new PointF();
        mEndPoint = new PointF();
        mLinePath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheBitmap);

        float startX = getWidth() * mStartX + mStartPointRadius;
        if (startX > getWidth()) {
            startX = getWidth() - mStartPointRadius;
        }
        float startY = getHeight() * mStartY + mStartPointRadius;
        if (startY > getHeight()) {
            startY = getHeight() - mStartPointRadius;
        }
        mStartPoint.set(startX, startY);

        float endX = getWidth() * mEndX + mEndPointRadius;
        if (endX > getWidth()) {
            endX = getWidth() - mEndPointRadius;
        }
        float endY = getHeight() * mEndY + mEndPointRadius;
        if (endY > getHeight()) {
            endY = getHeight() - mEndPointRadius;
        }
        mEndPoint.set(endX, endY);

        mLinePath.reset();
        mLinePath.moveTo(mStartPoint.x, mStartPoint.y);
        switch (mTurnType) {
            case TURN_TYPE_BASE_START_X:
                mLinePath.lineTo(mStartPoint.x, mEndPoint.y);
                break;
            case TURN_TYPE_BASE_END_X:
                mLinePath.lineTo(mEndPoint.x, mStartPoint.y);
                break;
        }
        mLinePath.lineTo(mEndPoint.x, mEndPoint.y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCacheBitmap.eraseColor(Color.TRANSPARENT);
        //画线
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mCacheCanvas.drawPath(mLinePath, mPaint);

        //画两个点
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mStartPointColor);
        mCacheCanvas.drawCircle(mStartPoint.x, mStartPoint.y, mStartPointRadius, mPaint);
        mPaint.setColor(mEndPointColor);
        mCacheCanvas.drawCircle(mEndPoint.x, mEndPoint.y, mEndPointRadius, mPaint);

        canvas.drawBitmap(mCacheBitmap, 0, 0, null);
    }

}
