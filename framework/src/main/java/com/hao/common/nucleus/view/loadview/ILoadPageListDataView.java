package com.hao.common.nucleus.view.loadview;

import java.util.List;

/**
 * @Package com.hao.common.nucleus.view.loadview
 * @作 用:列表分页逻辑UI层回调接口
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016/6/18 0018
 */
public interface ILoadPageListDataView<M> extends ILoadView {
    void onRefreshDataToUI(List<M> ms);//下拉刷新

    void onLoadMoreDataToUI(List<M> ms);//上拉加载更多

    void onRefreshComplete();//刷新完成

    void onLoadComplete();//加载完成

    void onNoDate();//没有数据

    void onNoMoreLoad();//没有更多的数据加载

    int getTotalItem();//得到当前总数


}
