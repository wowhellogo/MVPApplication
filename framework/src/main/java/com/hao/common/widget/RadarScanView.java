package com.hao.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hao.common.R;


/**
 * 雷达View
 */
public class RadarScanView extends View {
    private int width;
    private int height;
    private int centerWidth;
    private int centerHeight;
    private int radiusCircle;
    private Paint circlePaint;//圆的画笔
    private Paint linePaint;//线的画笔
    private Paint sweepPaint;//扫描画笔
    private int circleColor = Color.parseColor("#30B1A2");//圆的颜色
    private int ringColor = Color.parseColor("#40C3B3");//环形的颜色
    private int lineColor = Color.parseColor("#38F3EE");//线的颜色
    private int tailColor = Color.parseColor("#00ffffff");
    private int headColor = Color.parseColor("#ccffffff");
    private int radarColor = Color.parseColor("#ffA8D7A7");
    private int start = 0;
    private boolean isSweep = true;
    private Matrix matrix;
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            if (isSweep) {
                start = (start + 2) % 360;
                Log.e("lgd", start + "");
                matrix = new Matrix();
                matrix.postRotate(start, 0, 0);
                postInvalidate();
                handler.postDelayed(run, 20);
            }
        }
    };

    public RadarScanView(Context context) {
        super(context);
        init(null, 0);
        initView();
    }


    public RadarScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        initView();
    }

    public RadarScanView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        initView();
    }

    private void initView() {
        setKeepScreenOn(true);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        circlePaint.setStrokeWidth(2);
        sweepPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sweepPaint.setColor(radarColor);
        sweepPaint.setStrokeWidth(0);
        matrix = new Matrix();
        handler.post(run);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RadarScanView, defStyle, 0);
        circleColor = typedArray.getColor(R.styleable.RadarScanView_circleColor, Color.parseColor("#30B1A2"));
        ringColor = typedArray.getColor(R.styleable.RadarScanView_ringColor, Color.parseColor("#40C3B3"));
        lineColor = typedArray.getColor(R.styleable.RadarScanView_lineColor, Color.parseColor("#40C3B3"));
        headColor = typedArray.getColor(R.styleable.RadarScanView_headColor, Color.parseColor("#ccffffff"));
        tailColor = typedArray.getColor(R.styleable.RadarScanView_tailColor, Color.parseColor("#00ffffff"));
        typedArray.recycle();
    }

    private int measureResult(int widthMeasureSpec) {
        int result = 0;
        int sizeSpec = MeasureSpec.getSize(widthMeasureSpec);
        int modeSpec = MeasureSpec.getMode(widthMeasureSpec);
        if (modeSpec == MeasureSpec.EXACTLY) {
            result = sizeSpec;
        } else {
            result = 400;
            if (modeSpec == MeasureSpec.AT_MOST) {
                result = Math.min(result, sizeSpec);
            }
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = measureResult(widthMeasureSpec);
        height = measureResult(heightMeasureSpec);
        centerWidth = width >> 1;
        centerHeight = height >> 1;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerWidth = w / 2;
        centerHeight = h / 2;
        radiusCircle = Math.min(w, h) * 2 / 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerWidth, centerHeight);
        radiusCircle = Math.min(centerWidth, centerHeight) * 2 / 3;
        circlePaint.setColor(circleColor);
        canvas.save();
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setColor(circleColor);
        canvas.drawCircle(0, 0, radiusCircle + px2dip(60), circlePaint);
        canvas.restore();


        //分别绘制四个圆
        canvas.save();
        circlePaint.setStrokeWidth(px2dip(10));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(lineColor);
        canvas.drawCircle(0, 0, radiusCircle / 6, circlePaint);
        canvas.drawCircle(0, 0, radiusCircle >> 1, circlePaint);
        canvas.drawCircle(0, 0, radiusCircle * 3 / 4, circlePaint);
        canvas.drawCircle(0, 0, radiusCircle, circlePaint);
        int outRadius = Math.min(centerWidth, centerHeight) * 3 / 4;
        canvas.drawCircle(0, 0, outRadius, circlePaint);
        canvas.restore();

        canvas.save();
        for (int i = 0; i < 72; i++) {
            if (i % 9 == 0) {
                canvas.drawLine(outRadius, 0, outRadius + 50, 0, circlePaint);
            } else {
                canvas.drawLine(outRadius, 0, outRadius + 30, 0, circlePaint);
            }

            canvas.rotate(5);
        }
        canvas.drawCircle(0, 0, outRadius + 20, circlePaint);
        canvas.restore();

        //绘制四条线
        canvas.save();
        for (int i = 0; i < 4; i++) {
            DashPathEffect pathEffect = new DashPathEffect(new float[]{20, 10}, 1);
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.reset();
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(px2dip(5));
            linePaint.setPathEffect(pathEffect);
            linePaint.setColor(lineColor);
            Path path = new Path();
            path.moveTo(-radiusCircle, 0);
            path.lineTo(radiusCircle, 0);
            canvas.drawPath(path, linePaint);

           /* canvas.drawLine(-radiusCircle, 0, radiusCircle, 0, linePaint);*/
            canvas.rotate(45);
        }
        canvas.restore();

        //扫描
        canvas.save();
        Shader shader = new SweepGradient(0, 0, tailColor, headColor);
        sweepPaint.setShader(shader);
        sweepPaint.setStrokeWidth(0);
        canvas.concat(matrix);
        canvas.drawArc(new RectF(-radiusCircle, -radiusCircle, radiusCircle, radiusCircle), 0, 135, true, sweepPaint);
        canvas.restore();
    }


    public void stopSweep() {
        isSweep = false;
        invalidate();
    }

    public void startSweep() {
        isSweep = true;
        handler.post(run);
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    private int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
