package com.hao.common.nucleus.factory;


import com.hao.common.nucleus.presenter.Presenter;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}
