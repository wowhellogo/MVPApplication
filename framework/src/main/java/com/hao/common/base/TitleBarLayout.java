package com.hao.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hao.common.R;
import com.hao.common.utils.StatusBarUtil;
import com.hao.common.utils.UIUtil;
import com.hao.common.widget.titlebar.TitleBar;

import static com.hao.common.utils.StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;

/**
 * @Package com.mydatabuilding.widget
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017/1/12
 */

public class TitleBarLayout extends FrameLayout {
    private Toolbar mToolbar;
    private TitleBar mTitleBar;
    private TopBarType mTopBarType = TopBarType.None;
    private boolean isOverlay;

    private int mActionBarSize;
    private ViewGroup mContentView;
    private Context mContext;
    private LayoutInflater mInflater;
    private AppCompatActivity mActivity;

    public TitleBarLayout(Context context) {
        this(context, null);
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    private void initTitleLayoutView() {
        switch (mTopBarType) {
            case None:
                initContentView();
                break;
            case ToolBar:
                initContentView();
                initToolBar();
                break;
            case TitleBar:
                initContentView();
                initTitleBar();
                break;
        }
    }


    public void initContentView() {
        initActionBarSize();
        mContentView = new FrameLayout(mContext);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = (isOverlay && mTopBarType != TopBarType.None) ? 0 : mActionBarSize;
        addView(mContentView, params);

    }

    private void initActionBarSize() {
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        mActionBarSize = (int) typedArray.getDimension(0, mContext.getResources().getDimension(R.dimen.actionBarSize));
    }

    private void initToolBar() {
        mToolbar = (Toolbar) mInflater.inflate(R.layout.inc_toolbar, null);
        mActivity.setSupportActionBar(mToolbar);
        if (mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addView(mToolbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mActionBarSize));
    }

    private void initTitleBar() {
        mTitleBar = (TitleBar) mInflater.inflate(R.layout.inc_titlebar, null);
        addView(mTitleBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mActionBarSize));
    }


    public void setContentView(View view) {
        mContentView.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setContentView(int layoutID) {
        mContentView.addView(mInflater.inflate(layoutID, null), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setTopBarType(TopBarType topBarType) {
        this.mTopBarType = topBarType;
    }


    /**
     * @return 是否悬浮
     */
    public void setOverlay(boolean isOverlay) {
        this.isOverlay = isOverlay;
    }

    public void setTitle(CharSequence title) {
        if (mTopBarType == TopBarType.None) {
            mActivity.setTitle(title);
        } else if (mTopBarType == TopBarType.TitleBar) {
            mTitleBar.setTitleText(title);
        } else if (mTopBarType == TopBarType.ToolBar) {
            mActivity.getSupportActionBar().setTitle(title);
        }
    }

    public void attachToActivity(AppCompatActivity activity) {
        mActivity = activity;
        initTitleLayoutView();
        StatusBarUtil.setColorForSwipeBack(this,getResources().getColor(R.color.colorPrimary),DEFAULT_STATUS_BAR_ALPHA);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View contentView = decorView.getChildAt(0);
        decorView.removeView(contentView);
        decorView.addView(this);
        setContentView(contentView);

    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }
}
