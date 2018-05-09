package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.modle.response.BangumiCommentsModle;
import com.xuan.myframework.view.modle.response.BangumiDetailsRecommendedModle;
import com.xuan.myframework.view.modle.response.BangumiDetailModle;

/**
 * com.xuan.myframework.view.view
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public interface BangumiDetailView extends BaseView<BangumiCommentsModle>{
    void showEmptyView();
    void hideEmptyView();
    void initEmptyView();

    void initBangumiDetailsRecycler();
    void initBangumiDetailsRecommendedRecycler();
    void initBangumicommentsRecycler();

    void loadBangumiDetailsSuccess(BangumiDetailModle data);
    void loadBangumiDeatilsRecommendedSuccess(BangumiDetailsRecommendedModle data);
    void loadBangumicommentsSuccess(BangumiCommentsModle data);
}
