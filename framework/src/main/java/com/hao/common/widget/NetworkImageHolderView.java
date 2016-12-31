package com.hao.common.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.hao.common.R;

/**
 * @Package com.daoda.aijiacommunity.common.widget
 * @作 用:网络图片加载
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2016/6/6 0006
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {
        imageView.setImageResource(R.mipmap.ic_defualt_loading);
        Glide.with(context)
                .load(data)
                .dontAnimate()
                .placeholder(R.mipmap.ic_defualt_loading)
                .into(imageView);
    }
}
