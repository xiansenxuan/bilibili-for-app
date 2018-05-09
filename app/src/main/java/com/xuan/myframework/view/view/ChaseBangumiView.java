package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.modle.response.ChaseBangumiModle;

/**
 * com.xuan.myframework.view.view
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public interface ChaseBangumiView  extends BaseView<ChaseBangumiModle>{
    void showEmptyView();

    void hideEmptyView();

    void initRecycler();

    void initEmptyView();
}
