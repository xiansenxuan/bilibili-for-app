package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * Created by xuan on 2017/6/12.
 */

public interface UserDetailsPresenter extends BasePresenter {
    void queryDetails(int mid,LifecycleTransformer lifecycleTransformer);
}
