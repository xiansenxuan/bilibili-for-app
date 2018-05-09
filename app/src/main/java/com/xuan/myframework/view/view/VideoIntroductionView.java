package com.xuan.myframework.view.view;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.modle.response.VideoDetailsModle;

/**
 * com.xuan.myframework.view.view
 * Created by xuan on 2017/6/27.
 * version
 * desc
 */

public interface VideoIntroductionView extends BaseView<VideoDetailsModle> {
    void showEmptyView();

    void hideEmptyView();
}
