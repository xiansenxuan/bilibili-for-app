package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.fragment.BangumiFragment;
import com.xuan.myframework.view.modle.response.BangumiModle;

import java.util.List;

/**
 * Created by xuan on 2017/6/2.
 */

public interface BangumiView extends BaseView<List<BangumiFragment.Visitable>>{

    void showEmptyView();

    void hideEmptyView();

    void initRecycler();

    void initEmptyView();

    void addBannerHeader(List<BangumiModle.ResultBean.AdBean.HeadBean> data);

    void addButtonHeader();

    void showShortSnackbar(String content);
}
