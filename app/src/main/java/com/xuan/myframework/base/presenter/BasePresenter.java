package com.xuan.myframework.base.presenter;

import android.support.annotation.NonNull;

import com.xuan.myframework.base.view.BaseView;

/**
 * Created by xuan on 2017/5/22.
 */

public interface BasePresenter {

    void attachView(@NonNull BaseView view);

    void detachView();

}
