package com.hao.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.hao.common.R;
import com.hao.common.nucleus.presenter.Presenter;
import com.hao.common.nucleus.view.NucleusRxAppCompatActivity;
import com.hao.common.utils.KeyboardUtil;
import com.hao.common.utils.StatusBarUtil;
import com.hao.common.widget.swipeback.SwipeBackLayout;
import com.hao.common.widget.titlebar.TitleBar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @Package com.hao.common.base
 * @作 用:Activity基类,所有Activity继承它
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月14日  11:31
 */
public abstract class BaseActivity<P extends Presenter> extends NucleusRxAppCompatActivity<P> implements TitleBar.Delegate, EasyPermissions.PermissionCallbacks, SwipeBackLayout.PanelSlideListener {
    protected MaterialDialog mLoadingDialog;
    private Toolbar mToolbar;
    private TitleBar mTitleBar;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//开启使用矢量图
        initContentView();
        setStatusBar();
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

    protected void initContentView() {
        if (getTopBarType() == TopBarType.None) {
            setContentView(getRootLayoutResID());
        } else if (getTopBarType() == TopBarType.TitleBar) {
            initTitleBarContentView();
        } else if (getTopBarType() == TopBarType.Toolbar) {
            initToolbarContentView();
        }
    }


    private void initTitleBarContentView() {
        super.setContentView(isLinear() ? R.layout.rootlayout_linear : R.layout.rootlayout_merge);
        ViewStubCompat toolbarVs = getViewById(R.id.toolbarVs);
        toolbarVs.setLayoutResource(R.layout.inc_titlebar);
        toolbarVs.inflate();

        mTitleBar = getViewById(R.id.titleBar);
        mTitleBar.setDelegate(this);

        ViewStubCompat viewStub = getViewById(R.id.contentVs);
        viewStub.setLayoutResource(getRootLayoutResID());
        viewStub.inflate();
    }

    private void initToolbarContentView() {
        super.setContentView(isLinear() ? R.layout.rootlayout_linear : R.layout.rootlayout_merge);

        ViewStubCompat toolbarVs = getViewById(R.id.toolbarVs);
        toolbarVs.setLayoutResource(R.layout.inc_toolbar);
        toolbarVs.inflate();

        mToolbar = getViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewStubCompat viewStub = getViewById(R.id.contentVs);
        viewStub.setLayoutResource(getRootLayoutResID());
        viewStub.inflate();
    }

    /**
     * 有 TitleBar 或者 Toolbar 时，是否为线性布局
     *
     * @return
     */
    protected boolean isLinear() {
        return true;
    }

    protected TopBarType getTopBarType() {
        return TopBarType.None;
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBackFinish() {
        if (isSupportSwipeBack()) {
            mSwipeBackLayout = new SwipeBackLayout(this);
            mSwipeBackLayout.attachToActivity(this);
            mSwipeBackLayout.setPanelSlideListener(this);

            // 设置滑动返回是否可用。默认值为 true
            mSwipeBackLayout.setSwipeBackEnable(true);
            // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
            mSwipeBackLayout.setIsOnlyTrackingLeftEdge(true);
            // 设置是否是微信滑动返回样式。默认值为 true「如果需要启用微信滑动返回样式，必须在 Application 的 onCreate 方法中执行 SwipeBackManager.getInstance().init(this)」
            mSwipeBackLayout.setIsWeChatStyle(true);
            // 设置阴影资源 id。默认值为 R.drawable.swipebacklayout_shadow
            mSwipeBackLayout.setShadowResId(R.drawable.swipebacklayout_shadow);
            // 设置是否显示滑动返回的阴影效果。默认值为 true
            mSwipeBackLayout.setIsNeedShowShadow(true);
            // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
            mSwipeBackLayout.setIsShadowAlphaGradient(true);
        }
    }

    /**
     * 是否支持滑动返回。默认支持，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    protected boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 动态设置滑动返回是否可用。
     *
     * @param swipeBackEnable
     */
    protected void setSwipeBackEnable(boolean swipeBackEnable) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setSwipeBackEnable(swipeBackEnable);
        }
    }

    protected void setStatusBar() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
        setStatusBarColor(color, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backward();
    }

    /**
     * 跳转到下一个Activity，并且销毁当前Activity
     *
     * @param cls 下一个Activity的Class
     */
    public void forwardAndFinish(Class<?> cls) {
        forward(cls);
        finish();
    }

    /**
     * 跳转到下一个Activity，不销毁当前Activity
     *
     * @param cls 下一个Activity的Class
     */
    public void forward(Class<?> cls) {
        KeyboardUtil.closeKeyboard(this);
        startActivity(new Intent(this, cls));
        executeForwardAnim();
    }

    public void forward(Class<?> cls, int requestCode) {
        forward(new Intent(this, cls), requestCode);
    }

    public void forwardAndFinish(Intent intent) {
        forward(intent);
        finish();
    }

    public void forward(Intent intent) {
        KeyboardUtil.closeKeyboard(this);
        startActivity(intent);
        executeForwardAnim();
    }

    public void forward(Intent intent, int requestCode) {
        KeyboardUtil.closeKeyboard(this);
        startActivityForResult(intent, requestCode);
        executeForwardAnim();
    }

    /**
     * 执行跳转到下一个Activity的动画
     */
    public void executeForwardAnim() {
        overridePendingTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit);
    }

    /**
     * 回到上一个Activity，并销毁当前Activity
     */
    public void backward() {
        KeyboardUtil.closeKeyboard(this);
        finish();
        executeBackwardAnim();
    }

    /**
     * 滑动返回上一个Activity，并销毁当前Activity
     */
    public void swipeBackward() {
        KeyboardUtil.closeKeyboard(this);
        finish();
        executeSwipeBackAnim();
    }

    /**
     * 回到上一个Activity，并销毁当前Activity（应用场景：欢迎、登录、注册这三个界面）
     *
     * @param cls 上一个Activity的Class
     */
    public void backwardAndFinish(Class<?> cls) {
        KeyboardUtil.closeKeyboard(this);
        startActivity(new Intent(this, cls));
        executeBackwardAnim();
        finish();
    }

    /**
     * 执行回到到上一个Activity的动画
     */
    public void executeBackwardAnim() {
        overridePendingTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit);
    }

    /**
     * 执行滑动返回到到上一个Activity的动画
     */
    public void executeSwipeBackAnim() {
        overridePendingTransition(R.anim.activity_swipeback_enter, R.anim.activity_swipeback_exit);
    }



    /**
     * 获取布局文件根视图
     *
     * @return
     */
    protected abstract
    @LayoutRes
    int getRootLayoutResID();

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     * 初始化View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);


    /**
     * 设置点击事件，并防止重复点击
     *
     * @param id
     * @param action
     */
    protected void setOnClick(@IdRes int id, Action1 action) {
        setOnClick(getViewById(id), action);
    }

    /**
     * 设置点击事件，并防止重复点击
     *
     * @param view
     * @param action
     */
    protected void setOnClick(View view, Action1 action) {
        RxView.clicks(view).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(action);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtil.handleAutoCloseKeyboard(isAutoCloseKeyboard(), getCurrentFocus(), ev, this);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 点击非 EditText 时，是否自动关闭键盘
     *
     * @return
     */
    protected boolean isAutoCloseKeyboard() {
        return true;
    }

    /**
     * 显示加载对话框
     *
     * @param resId
     */
    public void showLoadingDialog(@StringRes int resId) {
        showLoadingDialog(getString(resId));
    }

    public void showLoadingDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MaterialDialog.Builder(this)
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }
        mLoadingDialog.setContent(msg);
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getTopBarType() == TopBarType.None) {
            super.setTitle(title);
        } else if (getTopBarType() == TopBarType.TitleBar) {
            mTitleBar.setTitleText(title);
        } else if (getTopBarType() == TopBarType.Toolbar) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onClickLeftCtv() {

    }

    @Override
    public void onClickTitleCtv() {

    }

    @Override
    public void onClickRightCtv() {

    }

    @Override
    public void onClickRightSecondaryCtv() {

    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {
        swipeBackward();
    }

    @Override
    public void onPanelClosed(View panel) {

    }
}
