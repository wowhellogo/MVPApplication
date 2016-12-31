package com.hao.common.nucleus.view.loadview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.hao.common.adapter.BaseRecyclerViewAdapter;
import com.hao.common.utils.CommonUtils;
import com.hao.common.widget.LoadingLayout;
import com.hao.common.widget.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * @Package com.hao.common.nucleus.view.loadview
 * @作 用:用于默认处理装载数据回调类分页逻辑
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年10月11日  18:15
 */

public class LoadPageListDataView<M> implements ILoadPageListDataView<M> {
    LoadingLayout mLoadingLayout;
    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    BaseRecyclerViewAdapter<M> mAdapter;
    Context mContext;

    public LoadPageListDataView(Context context, SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, BaseRecyclerViewAdapter<M> adapter) {
        mContext = context;
        mRefreshLayout = refreshLayout;
        mRecyclerView = recyclerView;
        mAdapter = adapter;
    }

    public LoadPageListDataView(Context context, LoadingLayout loadingLayout, SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, BaseRecyclerViewAdapter<M> adapter) {
        mContext = context;
        this.mLoadingLayout = loadingLayout;
        mRefreshLayout = refreshLayout;
        mRecyclerView = recyclerView;
        mAdapter = adapter;
    }

    @Override
    public void onRefreshDataToUI(List<M> ms) {
        if (mAdapter != null) mAdapter.setData(ms);
    }

    @Override
    public void onLoadMoreDataToUI(List<M> ms) {
        if (mAdapter != null) mAdapter.addMoreData(ms);
    }

    @Override
    public void onRefreshComplete() {
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadComplete() {
        if (mRecyclerView instanceof XRecyclerView) {
            XRecyclerView xRecyclerView = (XRecyclerView) mRecyclerView;
            xRecyclerView.loadMoreComplete();
            if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onNoDate() {
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
        CommonUtils.show(getContext(), "暂无数据");
    }

    @Override
    public void onNoMoreLoad() {
        if (mRecyclerView instanceof XRecyclerView) {
            XRecyclerView xRecyclerView = (XRecyclerView) mRecyclerView;
            xRecyclerView.noMoreLoading();
            if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public int getTotalItem() {
        return mAdapter.getItemCount();
    }

    @Override
    public void showLoadingView() {
        if (mLoadingLayout != null) mLoadingLayout.setStatus(LoadingLayout.Loading);
    }

    @Override
    public void showContentView() {
        if (mLoadingLayout != null) mLoadingLayout.setStatus(LoadingLayout.Success);
    }

    @Override
    public void showEmptyView() {
        if (mLoadingLayout != null) mLoadingLayout.setStatus(LoadingLayout.Empty);
    }

    @Override
    public void showFailView() {
        if (mLoadingLayout != null) mLoadingLayout.setStatus(LoadingLayout.Error);
    }

    @Override
    public void showError(String message) {
        CommonUtils.show(getContext(), message);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
