package com.xuan.myframework.view.presenterImpl;

import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.presenter.MainPresenter;
import com.xuan.myframework.view.view.MainView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuan on 2017/5/22.
 */

public class MainPresenterImpl extends BasePresenterImpl<MainView,String> implements MainPresenter {

    public MainPresenterImpl(BaseView view) {
        attachView(view);
    }

    @Override
    public void queryDetails() {
        beforeRequest();
        Observable.timer(5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        onSuccess(aLong+"");
                    }
                });
    }
}
