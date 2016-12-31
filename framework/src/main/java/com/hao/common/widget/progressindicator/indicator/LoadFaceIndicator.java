package com.hao.common.widget.progressindicator.indicator;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.mycustomviewdemo.progressindicator.indicator
 * @作 用:加载中转动人脸
 * @创 建 人: linguoding
 * @日 期: 2016/2/25
 */
public class LoadFaceIndicator extends BaseIndicatorController {
    private Paint eyePaint;
    private int centerWidth;
    private int centerHeight;
    private float degrees;
    private int radius = 0;
    private int sweepRadius;

    public LoadFaceIndicator() {
        init();
    }

    private void init() {
        //眼眶画笔
        eyePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eyePaint.setColor(Color.WHITE);
        eyePaint.setStyle(Paint.Style.FILL);

    }


    @Override
    public void draw(Canvas canvas, Paint paint) {

        centerWidth = getWidth() / 2;
        centerHeight = getHeight() / 2;
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
        canvas.drawCircle(0, (eyeRadius >> 1), eyeBallRadius, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(centerWidth / 2, -centerHeight >> 3);
        canvas.rotate(-degrees);
        canvas.drawCircle(0, (eyeRadius >> 1), eyeBallRadius, paint);
        canvas.restore();

        //画两个眼皮
        canvas.save();
        canvas.translate(-centerWidth / 2, -centerHeight >> 3);
        canvas.drawArc(new RectF(-eyeRadius, -eyeRadius, eyeRadius, eyeRadius), -this.radius, -this.sweepRadius, false, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(centerWidth / 2, -centerHeight >> 3);
        canvas.drawArc(new RectF(-eyeRadius, -eyeRadius, eyeRadius, eyeRadius), -this.radius, -this.sweepRadius, false, paint);
        canvas.restore();

    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();

        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, 360).setDuration(3000);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(-1);
        rotateAnimator.setEvaluator(new FloatEvaluator());
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        rotateAnimator.start();
        animators.add(rotateAnimator);
        ValueAnimator translationAnimator = ValueAnimator.ofInt(0, 90, 0).setDuration(3000);
        translationAnimator.setInterpolator(new LinearInterpolator());
        translationAnimator.setRepeatCount(-1);
        translationAnimator.setEvaluator(new FloatEvaluator());
        translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                radius = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        translationAnimator.start();
        animators.add(translationAnimator);

        ValueAnimator sweepAnimator = ValueAnimator.ofInt(180, 0, 180).setDuration(3000);
        sweepAnimator.setInterpolator(new LinearInterpolator());
        sweepAnimator.setRepeatCount(-1);
        sweepAnimator.setEvaluator(new FloatEvaluator());
        sweepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepRadius = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        sweepAnimator.start();
        animators.add(sweepAnimator);
        return animators;
    }
}
