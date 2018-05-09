package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.ChaseBangumiModle;
import com.xuan.myframework.view.presenter.ChaseBangumiPresenter;
import com.xuan.myframework.view.view.ChaseBangumiView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * com.xuan.myframework.view.presenterImpl
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public class ChaseBangumiPresenterImpl extends BasePresenterImpl<ChaseBangumiView,ChaseBangumiModle> implements ChaseBangumiPresenter {

    public ChaseBangumiPresenterImpl(ChaseBangumiView view){
        attachView(view);
    }


    @Override
    public void queryChaseBangumiData(int mid, LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryChaseBangumiData(mid, true, new Consumer<ChaseBangumiModle>() {
            @Override
            public void accept(@NonNull ChaseBangumiModle chaseBangumiModle) throws Exception {
                viewReference.get().hideEmptyView();
                onSuccess(chaseBangumiModle);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                viewReference.get().showEmptyView();
                onError(throwable);
            }
        },lifecycleTransformer);
    }
}
