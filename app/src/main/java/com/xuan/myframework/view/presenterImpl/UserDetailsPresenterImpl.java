package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.UserDetailsModle;
import com.xuan.myframework.view.view.UserDetailsView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/6/12.
 */

public class UserDetailsPresenterImpl extends BasePresenterImpl<UserDetailsView,UserDetailsModle> implements UserDetailsPresenter{


    public UserDetailsPresenterImpl(UserDetailsView view) {
        attachView(view);
    }

    @Override
    public void queryDetails(int mid,LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryUserDetails(mid, true,
                new Consumer<UserDetailsModle>() {
                    @Override
                    public void accept(@NonNull UserDetailsModle modle) throws Exception {
                        onSuccess(modle);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        onError(throwable);
                    }
                },lifecycleTransformer);
    }
}
