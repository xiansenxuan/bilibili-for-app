package com.xuan.myframework.view.presenter;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * com.xuan.myframework.view.presenter
 * Created by xuan on 2017/7/17.
 * version
 * desc
 */

public interface VideoPlayerPresenter extends BasePresenter{
    void queryVideoPlayerData(int cid,int quailty,String videoType,LifecycleTransformer lifecycleTransformer);
}
