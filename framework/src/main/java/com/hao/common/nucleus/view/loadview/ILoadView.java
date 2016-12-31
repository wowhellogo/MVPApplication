/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.hao.common.nucleus.view.loadview;

import android.content.Context;

/**
 * 这个接口代表将使用加载数据的view
 */
public interface ILoadView {
    /**
     * 显示加载的view
     */
    void showLoadingView();

    /**
     * 加载成功的view
     */
    void showContentView();

    /**
     * 显示空view
     */
    void showEmptyView();

    /**
     * 显示失败View
     */
    void showFailView();

    /**
     * 显示错误信息
     *
     * @param message A string representing an error.
     */
    void showError(String message);
    /**
     * Get a {@link Context}.
     */
    Context getContext();
}
