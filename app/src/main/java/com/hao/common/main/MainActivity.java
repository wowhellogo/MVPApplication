package com.hao.common.main;
import android.os.Bundle;
import android.view.View;
import com.hao.common.base.BaseActivity;
import com.hao.common.base.TopBarType;
import com.hao.common.nucleus.factory.RequiresPresenter;


@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter> implements IMainContract.IView {
    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_main;
    }

    /**
     * 主界面不需要支持滑动返回，重写该方法永久禁用当前界面的滑动返回功能
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("MVP示例");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    public void onText(View view) {
        mSwipeBackHelper.forward(MyActivity.class);
    }

    @Override
    public void showHello(String hello) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.Toolbar;
    }

}
