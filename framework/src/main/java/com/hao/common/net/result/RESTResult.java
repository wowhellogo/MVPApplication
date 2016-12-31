package com.hao.common.net.result;

import java.io.Serializable;

/**
 * @Package com.daoda.data_library.repository.net
 * @作 用:包装接口数据
 * @创 建 人: linguoding
 * @日 期: 2016-01-15
 */
public class RESTResult<T> implements Serializable {
    private int code;
    private boolean hasError;//是否有错误
    private String message;//错误信息
    private Result<T> result;

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RESTResult{" +
                "code=" + code +
                ", hasError=" + hasError +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
