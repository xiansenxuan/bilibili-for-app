package com.xuan.myframework.base.view;

/**
 * Created by xuan on 2017/5/22.
 */

public interface ProgressCallBack<T> {

    void beforeRequest();

    void onSuccess(T data);

    void onError(Throwable throwable);
}
