package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.LiveDataModle;
import com.xuan.myframework.view.presenter.HomePresenter;
import com.xuan.myframework.view.view.HomeView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/6/2.
 */

public class HomePresenterImpl extends BasePresenterImpl<HomeView,LiveDataModle> implements HomePresenter {

    public HomePresenterImpl(BaseView view) {
        attachView(view);
    }


    @Override
    public void queryLiveData(LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryLiveData(false, new Consumer<LiveDataModle>() {
                    @Override
                    public void accept(@NonNull LiveDataModle liveDataModle) throws Exception {

                        viewReference.get().hideEmptyView();
                        onSuccess(liveDataModle);
                    }
                }
                , new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        viewReference.get().showEmptyView();
                        onError(throwable);
                    }
                }
                ,lifecycleTransformer);
    }
}
