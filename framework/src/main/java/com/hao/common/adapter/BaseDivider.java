package com.hao.common.adapter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.hao.common.R;
import com.hao.common.manager.AppManager;

/**
 * @Package com.hao.common.adapter
 * @作 用:RecyclerView 分隔线
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年01月06日  17:20
 */


public class BaseDivider extends RecyclerView.ItemDecoration {
    private Drawable mDividerDrawable;
    private int mLeftMargin;
    private int mRightMargin;
    private int mOrientation = LinearLayout.VERTICAL;

    private BaseDivider(@DrawableRes int drawableResId) {
        mDividerDrawable = ContextCompat.getDrawable(AppManager.getApp(), drawableResId);
    }

    /**
     * 自定义 drawable 资源分隔线
     *
     * @param drawableResId
     * @return
     */
    public static BaseDivider newDrawableDivider(@DrawableRes int drawableResId) {
        return new BaseDivider(drawableResId);
    }

    /**
     * 默认的 shape 资源分隔线
     *
     * @return
     */
    public static BaseDivider newShapeDivider() {
        return new BaseDivider(R.drawable.adapter_divider_shape);
    }

    /**
     * 默认的图片分隔线
     *
     * @return
     */
    public static BaseDivider newBitmapDivider() {
        return new BaseDivider(R.mipmap.adapter_divider_bitmap);
    }

    /**
     * 设置左边距和右边距资源 id
     *
     * @param resId
     * @return
     */
    public BaseDivider setBothMarginResource(@DimenRes int resId) {
        mLeftMargin = AppManager.getApp().getResources().getDimensionPixelOffset(resId);
        mRightMargin = mLeftMargin;
        return this;
    }

    /**
     * 设置左边距和右边距
     *
     * @param dpValue 单位为 dp
     * @return
     */
    public BaseDivider setBothMargin(int dpValue) {
        mLeftMargin = dp2px(dpValue);
        mRightMargin = mLeftMargin;
        return this;
    }

    /**
     * 设置左边距资源 id
     *
     * @param resId
     * @return
     */
    public BaseDivider setLeftMarginResource(@DimenRes int resId) {
        mLeftMargin = AppManager.getApp().getResources().getDimensionPixelOffset(resId);
        return this;
    }

    /**
     * 设置左边距
     *
     * @param dpValue 单位为 dp
     * @return
     */
    public BaseDivider setLeftMargin(int dpValue) {
        mLeftMargin = dp2px(dpValue);
        return this;
    }

    /**
     * 设置右边距资源 id
     *
     * @param resId
     * @return
     */
    public BaseDivider setRightMarginResource(@DimenRes int resId) {
        mRightMargin = AppManager.getApp().getResources().getDimensionPixelOffset(resId);
        return this;
    }

    /**
     * 设置右边距
     *
     * @param dpValue 单位为 dp
     * @return
     */
    public BaseDivider setRightMargin(int dpValue) {
        mRightMargin = dp2px(dpValue);
        return this;
    }

    /**
     * 设置分隔线颜色资源 id
     *
     * @param resId
     * @param isSrcTop
     * @return
     */
    public BaseDivider setColorResource(@ColorRes int resId, boolean isSrcTop) {
        return setColor(AppManager.getApp().getResources().getColor(resId), isSrcTop);
    }

    /**
     * 设置分隔线颜色
     *
     * @param color
     * @param isSrcTop
     * @return
     */
    public BaseDivider setColor(@ColorInt int color, boolean isSrcTop) {
        mDividerDrawable.setColorFilter(color, isSrcTop ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.SRC);
        return this;
    }

    /**
     * 设置为水平方向
     *
     * @return
     */
    public BaseDivider setHorizontal() {
        mOrientation = LinearLayout.HORIZONTAL;
        return this;
    }

    /**
     * 旋转分隔线，仅用于分隔线为 Bitmap 时。应用场景：UI 给了一个水平分隔线，恰巧项目里需要一条一模一样的竖直分隔线
     *
     * @return
     */
    public BaseDivider rotateDivider() {
        if (mDividerDrawable != null && mDividerDrawable instanceof BitmapDrawable) {
            Bitmap inputBitmap = ((BitmapDrawable) mDividerDrawable).getBitmap();
            Matrix matrix = new Matrix();
            matrix.setRotate(90, (float) inputBitmap.getWidth() / 2, (float) inputBitmap.getHeight() / 2);

            float outputX = inputBitmap.getHeight();
            float outputY = 0;

            final float[] values = new float[9];
            matrix.getValues(values);
            float x1 = values[Matrix.MTRANS_X];
            float y1 = values[Matrix.MTRANS_Y];
            matrix.postTranslate(outputX - x1, outputY - y1);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getHeight(), inputBitmap.getWidth(), Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            Canvas canvas = new Canvas(outputBitmap);
            canvas.drawBitmap(inputBitmap, matrix, paint);
            mDividerDrawable = new BitmapDrawable(null, outputBitmap);
        }
        return this;
    }

    private boolean isNeedSkip(int position, int itemCount) {
        if (position == itemCount - 1) {
            return true;
        }
        return false;
    }

    // 如果等于分隔线的宽度或高度的话可以不用重写该方法
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || parent.getAdapter() == null) {
            return;
        }

        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();

        if (mOrientation == LinearLayout.VERTICAL) {
            if (isNeedSkip(position, itemCount)) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, mDividerDrawable.getIntrinsicHeight());
            }
        } else {
            if (isNeedSkip(position, itemCount)) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, mDividerDrawable.getIntrinsicWidth(), 0);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || parent.getAdapter() == null) {
            return;
        }

        if (mOrientation == LinearLayout.VERTICAL) {
            drawVertical(canvas, parent);
        } else {
            drawHorizontal(canvas, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft() + mLeftMargin;
        int right = parent.getWidth() - parent.getPaddingRight() - mRightMargin;
        View child;
        RecyclerView.LayoutParams layoutParams;
        int top;
        int bottom;
        int itemCount = parent.getAdapter().getItemCount();
        for (int position = 0; position < itemCount; position++) {
            if (isNeedSkip(position, itemCount)) {
                continue;
            }

            child = parent.getChildAt(position);
            if (child == null || child.getLayoutParams() == null) {
                continue;
            }

            layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + layoutParams.bottomMargin;
            bottom = top + mDividerDrawable.getIntrinsicHeight();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(canvas);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {

    }

    private static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, AppManager.getApp().getResources().getDisplayMetrics());
    }
}

