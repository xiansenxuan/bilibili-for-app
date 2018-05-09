package com.xuan.myframework.base.view;

/**
 * Created by xuan on 2017/5/22.
 */

public interface BaseView<E> {
    void loadDataSuccess(E data);

    void showProgress();

    void hideProgress();

    void showMsg(String message);

}
