package com.hao.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * @Package com.daoda.aijiacommunity.common.adapter
 * @作 用:ExpandableAdapter的基类
 * @创 建 人: 林国定
 * @日 期: 2015/3/13
 */
public abstract class AppBaseExpandableAdapter<T> extends BaseExpandableListAdapter {
    protected Context context;
    protected List<T> data;
    protected LayoutInflater inflater;

    protected AppBaseExpandableAdapter(Context context, List<T> data) {
        this.context=context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    /*子类重写该方法得到子item的个数*/
    @Override
    public int getChildrenCount(int groupPosition) {
        return getChildrenNum(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    /*子类重写该方法,得到子item的id*/
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return getChildren(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return createGroupView(groupPosition, isExpanded, convertView, parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return createChildView(groupPosition, childPosition, isLastChild, convertView, parent);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setDatas(List<T> datas) {
        if (datas != null) {
            this.data = datas;
        } else {
            this.data.clear();
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        this.data.clear();
    }

    /*子view的方法，实现展示数据*/
    protected abstract View createGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    /*父view，组数据展示*/
    protected abstract View createChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    protected abstract int getChildrenNum(int groupPosition);

    protected abstract Object getChildren(int groupPosition, int childPosition);
}
