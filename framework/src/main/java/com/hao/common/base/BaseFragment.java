package com.hao.common.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hao.common.R;
import com.hao.common.nucleus.presenter.Presenter;
import com.hao.common.nucleus.view.NucleusRxFragment;
import com.hao.common.widget.titlebar.TitleBar;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @Package com.hao.common.base
 * @作 用:基本的Fragment
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月15日  17:49
 */


public abstract class BaseFragment<P extends Presenter> extends NucleusRxFragment<P> implements EasyPermissions.PermissionCallbacks, TitleBar.Delegate {
    protected View mContentView;
    protected boolean mIsLoadedData = false;
    private TitleBar mTitleBar;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            initContentView();
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            handleOnVisibilityChangedToUser(isVisibleToUser);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(false);
        }
    }

    /**
     * 处理对用户是否可见
     *
     * @param isVisibleToUser
     */
    private void handleOnVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            // 对用户可见
            if (!mIsLoadedData) {
                Log.d(this.getClass().getSimpleName(), " 懒加载一次");
                mIsLoadedData = true;
                onLazyLoadOnce();
            }
            Log.d(this.getClass().getSimpleName(), " 对用户可见");
            onVisibleToUser();
        } else {
            // 对用户不可见
            Log.d(this.getClass().getSimpleName(), " 对用户不可见");
            onInvisibleToUser();
        }
    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    protected void onLazyLoadOnce() {
    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    protected void onVisibleToUser() {
    }

    /**
     * 对用户不可见时触发该方法
     */
    protected void onInvisibleToUser() {
    }


    protected void initContentView() {
        if (getTopBarType() == TopBarType.None) {
            mContentView = LayoutInflater.from(getActivity()).inflate(getRootLayoutResID(), null);
        } else if (getTopBarType() == TopBarType.TitleBar) {
            initTitleBarContentView();
        } else if (getTopBarType() == TopBarType.ToolBar) {
            initToolbarContentView();
        }
    }

    protected void initToolbarContentView() {
        mContentView = LayoutInflater.from(getActivity()).inflate(isLinear() ? R.layout.rootlayout_linear : R.layout.rootlayout_frame, null);

        ViewStubCompat toolbarVs = getViewById(R.id.toolbarVs);
        toolbarVs.setLayoutResource(R.layout.inc_toolbar);
        toolbarVs.inflate();
        mToolbar = getViewById(R.id.toolbar);

        ViewStubCompat viewStub = getViewById(R.id.contentVs);
        viewStub.setLayoutResource(getRootLayoutResID());
        viewStub.inflate();

        setHasOptionsMenu(true);
    }


    protected void initTitleBarContentView() {
        mContentView = LayoutInflater.from(getActivity()).inflate(isLinear() ? R.layout.rootlayout_linear : R.layout.rootlayout_frame, null);

        ViewStubCompat toolbarVs = getViewById(R.id.toolbarVs);
        toolbarVs.setLayoutResource(R.layout.inc_titlebar);
        toolbarVs.inflate();

        mTitleBar = getViewById(R.id.titleBar);
        mTitleBar.setDelegate(this);

        ViewStubCompat viewStub = getViewById(R.id.contentVs);
        viewStub.setLayoutResource(getRootLayoutResID());
        viewStub.inflate();
    }


    public BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        return null;
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


    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public abstract
    @LayoutRes
    int getRootLayoutResID();

    protected abstract void initView(Bundle savedInstanceState);

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

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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


    public void setTitle(CharSequence title) {
        if (getTopBarType() == TopBarType.TitleBar) {
            mTitleBar.setTitleText(title);
        } else if (getTopBarType() == TopBarType.ToolBar) {
            mToolbar.setTitle(title);
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
}
