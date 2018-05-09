package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.VideoDetailsModle;
import com.xuan.myframework.view.presenter.VideoIntroductionPresenter;
import com.xuan.myframework.view.view.VideoIntroductionView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * com.xuan.myframework.view.presenterImpl
 * Created by xuan on 2017/6/27.
 * version
 * desc
 */

public class VideoIntroductionPresenterImpl extends BasePresenterImpl<VideoIntroductionView,VideoDetailsModle> implements
        VideoIntroductionPresenter {

    public VideoIntroductionPresenterImpl(VideoIntroductionView view) {
        attachView(view);
    }


    @Override
    public void queryVideoIntroductionData(int avId, LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryVideoDetails(avId, true,
                new Consumer<VideoDetailsModle>() {
                    @Override
                    public void accept(@NonNull VideoDetailsModle videoDetailsModle) throws Exception {
                        viewReference.get().hideEmptyView();
                        onSuccess(videoDetailsModle);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        viewReference.get().showEmptyView();
                        onError(throwable);
                    }
                }, lifecycleTransformer);
    }
}
