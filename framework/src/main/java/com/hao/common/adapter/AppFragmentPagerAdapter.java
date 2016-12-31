package com.hao.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lgd on 2015/3/2.
 */
public abstract class AppFragmentPagerAdapter<T> extends FragmentPagerAdapter {
    public List<T> data;

    public AppFragmentPagerAdapter(FragmentManager fm, List<T> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return createItem(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void addAll(List<T> list) {
        this.data.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clearData() {
        this.data.clear();
        this.notifyDataSetChanged();
    }

    public abstract Fragment createItem(int position);
}
