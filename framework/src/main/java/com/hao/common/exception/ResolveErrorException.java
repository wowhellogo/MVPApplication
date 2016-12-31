package com.hao.common.exception;

/**
 * @Package com.daoda.aijiacommunity.common.exception
 * @作 用:解析出错
 * @创 建 人: linguoding
 * @日 期: 2016-01-18
 */
public class ResolveErrorException extends Exception {
    public ResolveErrorException() {
        super();
    }

    public ResolveErrorException(String detailMessage) {
        super(detailMessage);
    }

    public ResolveErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResolveErrorException(Throwable throwable) {
        super(throwable);
    }
}
