package com.hao.common.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

/**
 * View工具类
 *
 * @author yebin
 * @date 2014-10-23
 */
public class ViewUtils {

    /**
     * 添加一个view到布局 *
     */
    public static void addViewToLayout(LayoutInflater inflater, int resId, ViewGroup targetView) {
        View mContentView = inflater.inflate(resId, null);
        addViewToLayout(mContentView, targetView);
    }

    /**
     * 添加一个view到布局 *
     */
    public static void addViewToLayout(View view, ViewGroup targetView) {
        LayoutParams mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(mLayoutParams);
        if (null != targetView) {
            targetView.addView(view);
        }
    }

    public static void initVerticalLinearRecyclerView(Context context, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }


}
