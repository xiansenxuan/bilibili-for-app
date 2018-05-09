package com.xuan.myframework.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * com.xuan.myframework.base.adapter
 * Created by xuan on 2017/6/27.
 * version
 * desc
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);
    }


}
