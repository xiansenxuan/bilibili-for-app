package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * com.xuan.myframework.view.presenter
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public interface BangumiIndexPresenter extends BasePresenter {
    void queryBroadcastTableData(LifecycleTransformer lifecycleTransformer);
}
