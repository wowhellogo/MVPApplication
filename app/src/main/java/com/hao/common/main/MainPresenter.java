package com.hao.common.main;
import android.os.Bundle;
import com.hao.common.nucleus.presenter.RxPresenter;
/**
 * @Package com.haomvp.main
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月15日  18:13
 */


public class MainPresenter extends RxPresenter<MainActivity> implements IMainContract.IPresenter {
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    public void getHello() {

    }
}
