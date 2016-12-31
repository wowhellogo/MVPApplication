package com.hao.common.main;

import com.hao.common.nucleus.presenter.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.haomvp.main
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月16日  11:37
 */


public class MyPresenter extends RxPresenter<MyActivity> {
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new User("测试用户" + i, i + "岁", "男"));
        }
        return list;
    }
}
