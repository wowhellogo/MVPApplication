package com.hao.common.exception;

/**
 * @Package com.daoda.aijiacommunity.common.exception
 * @作 用:没有data异常，hasData为false
 * @创 建 人: linguoding
 * @日 期: 2016-01-18
 */
public class NotFoundDataException extends Exception {
    public NotFoundDataException() {
        super();
    }

    public NotFoundDataException(String detailMessage) {
        super(detailMessage);
    }

    public NotFoundDataException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotFoundDataException(Throwable throwable) {
        super(throwable);
    }
}
