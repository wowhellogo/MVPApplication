package com.hao.common.main;

import android.os.Bundle;
import android.app.Activity;

import com.hao.common.base.BaseActivity;
import com.hao.common.base.BaseDecorActivity;
import com.hao.common.base.TopBarType;

public class Main2Activity extends BaseDecorActivity {

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }
}
