package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.activity.BroadcastTableActivity;

import java.util.List;

/**
 * com.xuan.myframework.view.view
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public interface BroadcastTableView extends BaseView<List<BroadcastTableActivity.Visitable>>{
    void showEmptyView();

    void hideEmptyView();

    void initRecycler();

    void initEmptyView();
}
