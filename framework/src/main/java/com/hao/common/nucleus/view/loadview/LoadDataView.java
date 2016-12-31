package com.hao.common.nucleus.view.loadview;

import android.content.Context;

/**
 * @Package com.hao.common.nucleus.view.loadview
 * @作用：用于在视图中装载数据回调类
 * @作者：linguoding
 * @日期:2016/3/16 10:09
 */
public class LoadDataView<D> implements ILoadDataView<D> {
    @Override
    public void loadDataToUI(D d) {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showContentView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showFailView() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
