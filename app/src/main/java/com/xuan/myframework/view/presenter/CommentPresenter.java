package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * com.xuan.myframework.view.presenter
 * Created by xuan on 2017/7/6.
 * version
 * desc
 */

public interface CommentPresenter extends BasePresenter {
    void queryCommentData(int aid,int pageNum,int pageSize,int ver, LifecycleTransformer lifecycleTransformer);
}
