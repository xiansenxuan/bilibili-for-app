package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * Created by xuan on 2017/6/2.
 */

public interface LivePlayerPresenter extends BasePresenter {
    void queryLivePlayerData(int cid,LifecycleTransformer lifecycleTransformer);
}
