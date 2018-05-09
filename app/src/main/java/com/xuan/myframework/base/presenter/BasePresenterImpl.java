package com.xuan.myframework.base.presenter;

import android.support.annotation.NonNull;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.base.view.ProgressCallBack;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


/**
 * Created by xuan on 2017/5/22.
 */

public class BasePresenterImpl<T extends BaseView<E>, E> implements BasePresenter,ProgressCallBack<E> {
    protected Reference<T> viewReference;

    @Override
    public void attachView(@NonNull BaseView view)
    {
        this.viewReference=new WeakReference(view);
    }

    @Override
    public void detachView() {
        if (viewReference != null) {
            viewReference.clear();
            viewReference = null;
        }
    }

    @Override//to do
    public void beforeRequest() {
        this.viewReference.get().showProgress();
    }

    @Override//to do
    public void onSuccess(E data) {
        this.viewReference.get().hideProgress();
        this.viewReference.get().loadDataSuccess(data);
    }

    @Override//to do
    public void onError(Throwable throwable) {
        this.viewReference.get().hideProgress();
        this.viewReference.get().showMsg(throwable.getMessage());

        throwable.printStackTrace();
    }
}
