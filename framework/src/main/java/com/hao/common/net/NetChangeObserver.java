package com.hao.common.net;

/**
 * @Package com.daoda.aijiacommunity.common.net
 * @作 用:检测网络改变的观察者
 * @创 建 人: linguoding
 * @日 期: 2016-01-19
 */
public interface NetChangeObserver {
    /**
     * 网络连接连接时调用
     */
    void onChange(NetWorkUtil.NetType type);

    /**
     * 当前没有网络连接
     */
    void onDisConnect();
}
