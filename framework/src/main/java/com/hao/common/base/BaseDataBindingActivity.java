package com.hao.common.base;

import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.ViewGroup;

import com.hao.common.BR;
import com.hao.common.nucleus.presenter.Presenter;

/**
 * @Package com.hao.common.base
 * @作 用:使用ViewDataBinding的Activity基类
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月16日  13:34
 */
public abstract class BaseDataBindingActivity<P extends Presenter, B extends ViewDataBinding> extends BaseActivity<P> {
    protected B mBinding;
    @Override
    protected void initContentView() {
        mBinding = DataBindingUtil.setContentView(this, getRootLayoutResID());
        mBinding.setVariable(BR.eventHandler, this);
        mBinding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                ViewGroup view = (ViewGroup) binding.getRoot();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(view);
                }
                return true;
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {
    }
}
