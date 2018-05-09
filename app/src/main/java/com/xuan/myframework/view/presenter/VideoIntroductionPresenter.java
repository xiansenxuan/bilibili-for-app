package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * com.xuan.myframework.view.presenter
 * Created by xuan on 2017/6/27.
 * version
 * desc
 */

public interface VideoIntroductionPresenter extends BasePresenter {
    void queryVideoIntroductionData(int avId, LifecycleTransformer lifecycleTransformer);
}
