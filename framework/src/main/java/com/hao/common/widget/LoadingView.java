package com.hao.common.widget;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;

import com.hao.common.R;

/**
 * @Package com.daoda.aijiacommunity.common.widget
 * @作 用:加载的view
 * @创 建 人: linguoding
 * @日 期: 2016/2/24
 */
public class LoadingView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Canvas canvas;//画布
    private Paint paint;//画笔
    private Paint eyePaint;
    private Paint eyeballPaint;
    private int backgroupColor;
    private int eyeColor;
    private int width;
    private int height;
    private int centerWidth;
    private int centerHeight;
    private boolean isDrawing;
    private float degrees;
    private int radius;

    public LoadingView(Context context) {
        super(context);
        initView();
    }


    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        backgroupColor = array.getColor(R.styleable.LoadingView_backgroupColor, Color.BLACK);
        eyeColor = array.getColor(R.styleable.LoadingView_eyeColor, Color.BLACK);
        array.recycle();
        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*super.onMeasure(widthMeasureSpec, heightMeasureSpec);*/
        width = measureResult(widthMeasureSpec);
        height = measureResult(heightMeasureSpec);
        centerWidth = width >> 1;
        centerHeight = height >> 1;
        setMeasuredDimension(width, height);

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


    private void initView() {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroupColor);

        //眼眶画笔
        eyePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eyePaint.setColor(Color.WHITE);
        eyePaint.setStyle(Paint.Style.FILL);

        //眼睛画笔
        eyeballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eyeballPaint.setColor(eyeColor);
        eyeballPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
        createAnimatorSet();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        while (isDrawing) {
            drawView();
        }
    }


    private void createAnimatorSet() {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, 360).setDuration(3000);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(-1);
        rotateAnimator.setEvaluator(new FloatEvaluator());
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
            }
        });
        int x = Math.min(getWidth() / 6, getHeight() / 6);
        ValueAnimator translationAnimator = ValueAnimator.ofInt(0, x, 0).setDuration(3000);
        translationAnimator.setInterpolator(new LinearInterpolator());
        translationAnimator.setRepeatCount(-1);
        translationAnimator.setEvaluator(new FloatEvaluator());
        translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (int) animation.getAnimatedValue();

            }
        });
        set.playTogether(rotateAnimator, translationAnimator);
        set.start();
    }


    public void drawView() {
        try {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.translate(centerWidth, centerHeight);
            int radius = Math.min(centerWidth, centerHeight);
            //画圆
            canvas.drawCircle(0, 0, radius, paint);
            //画两个眼框
            int eyeRadius = Math.min(centerWidth / 3, centerHeight / 3);
            canvas.drawCircle(-centerWidth / 2, -centerHeight >> 3, eyeRadius, eyePaint);
            canvas.drawCircle(centerWidth / 2, -centerHeight >> 3, eyeRadius, eyePaint);
            //画嘴巴
            canvas.drawArc(new RectF(-eyeRadius, 0, eyeRadius, eyeRadius * 2), 0, 180, true, eyePaint);

            canvas.save();
            //画两个眼睛
            int eyeBallRadius = Math.min(centerWidth >> 3, centerHeight >> 3);
            canvas.translate(-centerWidth / 2, -centerHeight >> 3);
            canvas.rotate(-degrees);
            canvas.drawCircle(0, (eyeRadius >> 1), eyeBallRadius, eyeballPaint);
            canvas.restore();

            canvas.save();
            canvas.translate(centerWidth / 2, -centerHeight >> 3);
            canvas.rotate(-degrees);
            canvas.drawCircle(0, (eyeRadius >> 1), eyeBallRadius, eyeballPaint);
            canvas.restore();

            //画两个眼皮
            canvas.save();
            canvas.translate(-centerWidth / 2, -centerHeight >> 3);
            canvas.drawRect(new RectF(-eyeRadius, -eyeRadius, eyeRadius, -this.radius), paint);
            canvas.restore();

            canvas.save();
            canvas.translate(centerWidth / 2, -centerHeight >> 3);
            canvas.drawRect(new RectF(-eyeRadius, -eyeRadius, eyeRadius, -this.radius), paint);
            canvas.restore();

        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }


}
