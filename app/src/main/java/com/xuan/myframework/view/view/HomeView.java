package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.modle.response.LiveDataModle;

/**
 * Created by xuan on 2017/6/2.
 */

public interface HomeView extends BaseView<LiveDataModle>{

    void showEmptyView();

    void hideEmptyView();

}
