package com.hao.common.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hao.common.BR;
import com.hao.common.nucleus.presenter.Presenter;

/**
 * @Package com.hao.common.base
 * @作 用:使用ViewDataBinding的Fragment基类
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月16日  14:04
 */


public abstract class BaseDataBindingFragment<P extends Presenter, B extends ViewDataBinding> extends BaseFragment<P> {
    protected B mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, getRootLayoutResID(), container, false);
            mBinding.setVariable(BR.eventHandler, this);
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mBinding.getRoot().getParent();
            if (parent != null) {
                parent.removeView(mBinding.getRoot());
            }
        }
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {
    }
}
