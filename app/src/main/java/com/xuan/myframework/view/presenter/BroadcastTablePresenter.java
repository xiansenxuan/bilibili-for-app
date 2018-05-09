package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;
import com.xuan.myframework.view.activity.BroadcastTableActivity;
import com.xuan.myframework.view.modle.response.BroadcastTableModle;

import java.util.List;

/**
 * com.xuan.myframework.view.presenter
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public interface BroadcastTablePresenter extends BasePresenter {
    void queryBroadcastTableData(LifecycleTransformer lifecycleTransformer);

    List<BroadcastTableActivity.Visitable> getVisitableData(BroadcastTableModle data);
}
