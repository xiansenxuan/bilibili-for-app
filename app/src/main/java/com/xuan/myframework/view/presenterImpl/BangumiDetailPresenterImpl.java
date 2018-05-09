package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.BangumiCommentsModle;
import com.xuan.myframework.view.modle.response.BangumiDetailsRecommendedModle;
import com.xuan.myframework.view.modle.response.BangumiDetailModle;
import com.xuan.myframework.view.presenter.BangumiDetailPresenter;
import com.xuan.myframework.view.view.BangumiDetailView;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * com.xuan.myframework.view.presenterImpl
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public class BangumiDetailPresenterImpl extends BasePresenterImpl<BangumiDetailView,BangumiCommentsModle> implements BangumiDetailPresenter {

    public BangumiDetailPresenterImpl(BangumiDetailView view){
        attachView(view);
    }


    @Override
    public void queryBangumiDetailsData(final LifecycleTransformer lifecycleTransformer) {
        beforeRequest();

        RetrofitManager.getInstance().queryBangumiDetailsData(true,lifecycleTransformer)
            .flatMap(new Function<BangumiDetailModle, Observable<BangumiDetailsRecommendedModle>>() {
                @Override
                public Observable<BangumiDetailsRecommendedModle> apply(@NonNull BangumiDetailModle bangumiDetailModle) throws Exception {
                    viewReference.get().loadBangumiDetailsSuccess(bangumiDetailModle);
                    return RetrofitManager.getInstance().queryBangumiDetailsRecommendData(true,lifecycleTransformer);
                }
            })
            .flatMap(new Function<BangumiDetailsRecommendedModle, Observable<BangumiCommentsModle>>() {
                @Override
                public Observable<BangumiCommentsModle> apply(@NonNull BangumiDetailsRecommendedModle bangumiDetailsRecommendedModle) throws Exception {
                    viewReference.get().loadBangumiDeatilsRecommendedSuccess(bangumiDetailsRecommendedModle);
                    return RetrofitManager.getInstance().queryBangumiDetailsCommentsData(true,lifecycleTransformer);
                }
            })
            .subscribe(new Consumer<BangumiCommentsModle>() {
                @Override
                public void accept(@NonNull BangumiCommentsModle bangumiCommentsModle) throws Exception {
                    viewReference.get().hideProgress();
                    viewReference.get().hideEmptyView();

                    viewReference.get().loadBangumicommentsSuccess(bangumiCommentsModle);
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
