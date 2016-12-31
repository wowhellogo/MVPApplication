package com.hao.common.net.result;

import java.io.Serializable;
import java.util.List;

/**
 * @Package com.daoda.data_library.repository.net
 * @作 用:接口包装的额外信息
 * @创 建 人: linguoding
 * @日 期: 2016-01-15
 */
public class Result<T> implements Serializable {
    private boolean hasData;
    private String data;
    private List<T> list;
    private T model;
    private int total;

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
