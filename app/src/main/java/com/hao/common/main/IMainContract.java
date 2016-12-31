package com.hao.common.main;

/**
 * @Package com.haomvp.main
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月16日  10:26
 */


public interface IMainContract {
    interface IView {
        void showHello(String hello);
    }

    interface IPresenter {
        void getHello();
    }
}
