package com.hao.common.exception;

/**
 * @Package com.daoda.aijiacommunity.common.exception
 * @作 用:请求失败的异常，hasError为true
 * @创 建 人: linguoding
 * @日 期: 2016-01-18
 */
public class RestErrorException extends Exception {
    public RestErrorException() {
        super();
    }

    public RestErrorException(String detailMessage) {
        super(detailMessage);
    }

    public RestErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RestErrorException(Throwable throwable) {
        super(throwable);
    }
}
