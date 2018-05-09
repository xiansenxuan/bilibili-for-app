package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.modle.response.RecommendBannerModle;
import com.xuan.myframework.view.modle.response.RecommendModle;

/**
 * Created by xuan on 2017/6/14.
 */

public interface RecommendView extends BaseView<RecommendModle> {

    void showEmptyView();

    void hideEmptyView();

    void loadBannerDataSuccess(RecommendBannerModle modle);
}
