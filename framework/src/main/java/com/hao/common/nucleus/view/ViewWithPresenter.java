package com.hao.common.nucleus.view;


import com.hao.common.nucleus.factory.PresenterFactory;
import com.hao.common.nucleus.factory.ReflectionPresenterFactory;
import com.hao.common.nucleus.presenter.Presenter;

public interface ViewWithPresenter<P extends Presenter> {

    /**
     * Returns a current presenter factory.
     */
    PresenterFactory<P> getPresenterFactory();

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    void setPresenterFactory(PresenterFactory<P> presenterFactory);

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    P getPresenter();
}
