package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.view.modle.response.VideoPlayerModle;
import com.xuan.myframework.view.presenter.VideoPlayerPresenter;
import com.xuan.myframework.view.view.VideoPlayerView;

/**
 * com.xuan.myframework.view.presenterImpl
 * Created by xuan on 2017/7/17.
 * version
 * desc
 */

public class VideoPlayerPresenterImpl extends BasePresenterImpl<VideoPlayerView,VideoPlayerModle> implements VideoPlayerPresenter {

    public VideoPlayerPresenterImpl(VideoPlayerView view) {
        attachView(view);
    }


    @Override
    public void queryVideoPlayerData(int cid, int quailty,String videoType, LifecycleTransformer lifecycleTransformer) {
        beforeRequest();

        onSuccess(new VideoPlayerModle());

//        RetrofitManager.getInstance().queryVideoPlayerData(cid, quailty, videoType, true, new Consumer<VideoPlayerModle>() {
//            @Override
//            public void accept(@NonNull VideoPlayerModle videoPlayerModle) throws Exception {
//                onSuccess(videoPlayerModle);
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                onError(throwable);
//            }
//        },lifecycleTransformer);

    }
}
