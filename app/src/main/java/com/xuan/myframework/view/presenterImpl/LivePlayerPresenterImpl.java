package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.presenter.LivePlayerPresenter;
import com.xuan.myframework.view.view.LivePlayerView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/6/9.
 */

public class LivePlayerPresenterImpl extends BasePresenterImpl<LivePlayerView,String> implements LivePlayerPresenter{

    public LivePlayerPresenterImpl(BaseView view) {
        attachView(view);
    }


    @Override
    public void queryLivePlayerData(int cid,LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryLivePlayerData(cid, true,
                new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String response) throws Exception {
                        onSuccess(response);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        onError(throwable);
                    }
                },lifecycleTransformer);
    }
}
