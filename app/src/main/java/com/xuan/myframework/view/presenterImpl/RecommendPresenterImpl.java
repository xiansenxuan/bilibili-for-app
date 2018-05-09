package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.RecommendBannerModle;
import com.xuan.myframework.view.modle.response.RecommendModle;
import com.xuan.myframework.view.presenter.RecommendPresenter;
import com.xuan.myframework.view.view.RecommendView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuan on 2017/6/14.
 */

public class RecommendPresenterImpl extends BasePresenterImpl<RecommendView,RecommendModle> implements RecommendPresenter {
    public RecommendPresenterImpl(RecommendView view) {
        attachView(view);
    }

    @Override
    public void queryRecommendData(final LifecycleTransformer lifecycleTransformer) {
        beforeRequest();

        RetrofitManager.getInstance().queryRecommendBannerData(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer)
                .flatMap(new Function<RecommendBannerModle, Observable<RecommendModle>>() {
            @Override
            public Observable<RecommendModle> apply(@NonNull RecommendBannerModle bannerModle) throws Exception {
                viewReference.get().loadBannerDataSuccess(bannerModle);
                return RetrofitManager.getInstance().queryRecommendedData(true)
                                        .compose(lifecycleTransformer)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
            }
        })
                .subscribe(new Consumer<RecommendModle>() {
                        @Override
                        public void accept(@NonNull RecommendModle recommendModle) throws Exception {
                            viewReference.get().hideEmptyView();
                            onSuccess(recommendModle);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            viewReference.get().showEmptyView();
                            onError(throwable);
                        }
                    });

    }
}
