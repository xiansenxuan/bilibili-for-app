package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.BangumiIndexModle;
import com.xuan.myframework.view.presenter.BangumiIndexPresenter;
import com.xuan.myframework.view.view.BangumiIndexView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * com.xuan.myframework.view.presenterImpl
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public class BangumiIndexPresenterImpl extends BasePresenterImpl<BangumiIndexView,BangumiIndexModle> implements BangumiIndexPresenter {

    public BangumiIndexPresenterImpl(BangumiIndexView view){
        attachView(view);
    }


    @Override
    public void queryBroadcastTableData(LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryBangumiIndexData(true, new Consumer<BangumiIndexModle>() {
            @Override
            public void accept(@NonNull BangumiIndexModle data) throws Exception {
                viewReference.get().hideEmptyView();

                onSuccess(data);

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
