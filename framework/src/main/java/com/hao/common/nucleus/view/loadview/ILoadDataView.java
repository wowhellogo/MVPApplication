package com.hao.common.nucleus.view.loadview;

/**
 * @Package com.hao.common.nucleus.view.loadview
 * @作 用: 装载数据的接口
 * @创 建 人: linguoding
 * @日 期: 2016/1/23
 */
public interface ILoadDataView<D> extends ILoadView {
    void loadDataToUI(D d);
}
