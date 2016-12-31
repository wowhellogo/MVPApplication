package com.hao.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hao.common.R;

/**
 * @Package com.hao.common.widget
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年08月12日  17:44
 */

public class RadarScanImageView extends View {
    private int width;
    private int height;
    private int centerWidth;
    private int centerHeight;
    private Paint mPaint;
    private Bitmap mBitmapBg;
    BitmapFactory.Options mOptions = new BitmapFactory.Options();
    private Matrix matrix;
    private int start = 0;
    private boolean isSweep = true;
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

    public RadarScanImageView(Context context) {
        this(context, null);
    }

    public RadarScanImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarScanImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setKeepScreenOn(true);
        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_radar_bg, mOptions);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        matrix = new Matrix();
        handler.post(run);
    }


    private int measureResult(int widthMeasureSpec, int size) {
        int result = 0;
        int sizeSpec = MeasureSpec.getSize(widthMeasureSpec);
        int modeSpec = MeasureSpec.getMode(widthMeasureSpec);
        if (modeSpec == MeasureSpec.EXACTLY) {
            result = sizeSpec;
        } else {
            result = size;
            if (modeSpec == MeasureSpec.AT_MOST) {
                result = Math.min(result, sizeSpec);
            }
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = measureResult(widthMeasureSpec, mOptions.outWidth);
        height = measureResult(heightMeasureSpec, mOptions.outHeight);
        centerWidth = width >> 1;
        centerHeight = height >> 1;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_radar_bg), 0, 0, mPaint);

    }
}
