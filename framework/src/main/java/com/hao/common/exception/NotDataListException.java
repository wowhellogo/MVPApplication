package com.hao.common.exception;

/**
 * @Package com.daoda.aijiacommunity.common.exception
 * @作 用:list为null,没有更多的数据异常
 * @创 建 人: linguoding
 * @日 期: 2016-01-25
 */
public class NotDataListException extends Exception {
    private int total;

    public NotDataListException() {
        super();
    }

    public NotDataListException(String detailMessage) {
        super(detailMessage);
    }

    public NotDataListException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotDataListException(Throwable throwable) {
        super(throwable);
    }

    public NotDataListException(String detailMessage, int total) {
        super(detailMessage);
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
