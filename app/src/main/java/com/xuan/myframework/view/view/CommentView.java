package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.modle.response.CommentModle;

/**
 * com.xuan.myframework.view.view
 * Created by xuan on 2017/7/6.
 * version
 * desc
 */

public interface CommentView extends BaseView<CommentModle> {
    void initRecyclerView();
    void initEmptyView();
    void showEmptyView();
    void hideEmptyView();
}
