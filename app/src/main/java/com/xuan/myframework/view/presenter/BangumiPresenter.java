package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;
import com.xuan.myframework.view.fragment.BangumiFragment;
import com.xuan.myframework.view.modle.response.BangumiModle;
import com.xuan.myframework.view.modle.response.BangumiRecommendedModle;

import java.util.List;

/**
 * Created by xuan on 2017/6/2.
 */

public interface BangumiPresenter extends BasePresenter {
    void queryBangumiData(LifecycleTransformer lifecycleTransformer);

    void loadBangumiDataSuccess(BangumiModle data);
    void loadBangumiRecommendedDataSuccess(BangumiRecommendedModle data);

    List<BangumiFragment.Visitable> getVisitableData(BangumiModle data);
    List<BangumiFragment.Visitable> getVisitableData(BangumiRecommendedModle data);
}
