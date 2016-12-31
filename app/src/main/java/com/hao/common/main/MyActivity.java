package com.hao.common.main;

import android.os.Bundle;

import com.hao.common.adapter.BaseBindingRecyclerViewAdapter;
import com.hao.common.base.BaseDataBindingActivity;
import com.hao.common.main.databinding.ActivityMyBinding;
import com.hao.common.main.databinding.ItemUserBinding;
import com.hao.common.nucleus.factory.RequiresPresenter;
import com.hao.common.utils.ViewUtils;
import com.hao.common.base.TopBarType;


@RequiresPresenter(MyPresenter.class)
public class MyActivity extends BaseDataBindingActivity<MyPresenter, ActivityMyBinding> {
    BaseBindingRecyclerViewAdapter<User, ItemUserBinding> mAdapter;

    @Override
    protected int getRootLayoutResID() {
        return R.layout.activity_my;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding.setEventHandler(this);
        mBinding.titleBar.setTitleText("测试用户列表");
        ViewUtils.initVerticalLinearRecyclerView(this, mBinding.recyclerView);
        mAdapter = new BaseBindingRecyclerViewAdapter<User, ItemUserBinding>(R.layout.item_user) {
            @Override
            protected void bindModel(ItemUserBinding binding, int position, User model) {
                binding.setModel(model);
                binding.setItemEventHandler(MyActivity.this);
            }
        };
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setItemEventHandler(this);
        mAdapter.setData(getPresenter().getUsers());
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected TopBarType getTopBarType() {
        return TopBarType.TitleBar;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

}
