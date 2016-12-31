package com.hao.common.nucleus.presenter;

import com.hao.common.exception.ErrorMessageFactory;
import com.hao.common.exception.NotDataListException;
import com.hao.common.exception.NotFoundDataException;
import com.hao.common.nucleus.view.loadview.ILoadDataView;
import com.hao.common.nucleus.view.loadview.ILoadPageListDataView;
import com.hao.common.nucleus.view.loadview.LoadPageListDataView;
import com.hao.common.rx.RxUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action2;
import rx.functions.Func0;

/**
 * @Package com.hao.common.base
 * @作 用:加载数据的Presenter
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月29日  17:55
 */


public class LoadPresenter extends RxPresenter {
    protected int startIndex = 0;//索引
    protected int pageSize = 10;
    protected boolean isShowing = true;//是否显示加载view

    private final static int LOAD_MODEL = 0;
    private final static int LOAD_LIST = 1;

    //####################################### Start加载Model #######################################################
    private class DefaultModelAction2<View extends ILoadDataView<Model>, Model> implements Action2<View, Model> {
        @Override
        public void call(View view, Model model) {
            view.loadDataToUI(model);
            view.showContentView();
        }
    }

    private class DefaultModelErrorAction2<View extends ILoadDataView> implements Action2<View, Throwable> {
        @Override
        public void call(View view, Throwable e) {
            if (isShowing) {
                if (e instanceof NotFoundDataException) {
                    view.showEmptyView();
                } else {
                    view.showFailView();
                    view.showError(ErrorMessageFactory.create(view.getContext(), (Exception) e));
                }
            }
        }
    }


    public <View extends ILoadDataView<Model>, Model> void loadModel(Observable<Model> observable) {
        restartableLatestCache(LOAD_MODEL, new Func0<Observable<Model>>() {
            @Override
            public Observable<Model> call() {
                return observable.compose(RxUtil.applySchedulersJobUI());
            }
        }, new DefaultModelAction2<View, Model>(), new DefaultModelErrorAction2<View>());
        start(LOAD_MODEL);
    }
    //####################################### End加载Model #######################################################

    //####################################### Start加载Lisst #######################################################
    private class DefaultListErrorAction2<View extends ILoadPageListDataView> implements Action2<View, Throwable> {

        @Override
        public void call(View view, Throwable e) {
            startIndex -= pageSize;
            if (isShowing) {
                if (e instanceof NotDataListException) {
                    view.showEmptyView();
                } else {
                    view.showFailView();
                    view.showError(ErrorMessageFactory.create(view.getContext(), (Exception) e));
                }

            } else {
                if (e instanceof NotDataListException) {
                    NotDataListException exception = (NotDataListException) e;
                    //没有更多数据
                    if (isNoMoreData(exception.getTotal(), view.getTotalItem())) {
                        view.onNoMoreLoad();
                    }
                } else if (e instanceof NotFoundDataException) {
                    view.onNoDate();
                } else {
                    view.showError(ErrorMessageFactory.create(view.getContext(), (Exception) e));
                }
            }
        }
    }

    private class DefaultListAction2<View extends ILoadPageListDataView<Model>, Model> implements Action2<View, List<Model>> {

        @Override
        public void call(View view, List<Model> models) {
            if (isRefresh()) {
                view.onRefreshDataToUI(models);
                view.onRefreshComplete();
            } else {
                view.onLoadMoreDataToUI(models);
                view.onLoadComplete();
            }
        }
    }

    /**
     * 是否是刷新
     *
     * @return
     */
    protected boolean isRefresh() {
        return startIndex == 0;
    }

    /**
     * 是否没有更多数据
     *
     * @param total
     * @param count
     * @return
     */
    private boolean isNoMoreData(int total, int count) {
        return (total > count && total != count) ? false : true;
    }


    public <View extends ILoadPageListDataView<Model>, Model> void loadList(Observable<List<Model>> observable) {
        restartableLatestCache(LOAD_LIST, new Func0<Observable<List<Model>>>() {
            @Override
            public Observable<List<Model>> call() {
                return observable.compose(RxUtil.applySchedulersJobUI());
            }
        }, new DefaultListAction2<View, Model>(), new DefaultListErrorAction2<View>());
        start(LOAD_LIST);
    }
    //####################################### End加载Lisst #######################################################

}
