package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.VideoDetailsModle;
import com.xuan.myframework.view.presenter.VideoDetailsPresenter;
import com.xuan.myframework.view.view.VideoDetailsView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/6/21.
 */

public class VideoDetailsPresenterImpl extends BasePresenterImpl<VideoDetailsView,VideoDetailsModle> implements VideoDetailsPresenter {

    public VideoDetailsPresenterImpl(VideoDetailsView view) {
        attachView(view);
    }

    @Override
    public void queryVideoDetails(int aid,LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryVideoDetails(aid, true,
                new Consumer<VideoDetailsModle>() {
                    @Override
                    public void accept(@NonNull VideoDetailsModle videoDetailsModle) throws Exception {
                        onSuccess(videoDetailsModle);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        onError(throwable);
                    }
                }, lifecycleTransformer);



    }
}
