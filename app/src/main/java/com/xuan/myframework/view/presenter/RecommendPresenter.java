package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * Created by xuan on 2017/6/14.
 */

public interface RecommendPresenter extends BasePresenter {

    void queryRecommendData(LifecycleTransformer lifecycleTransformer);
}
