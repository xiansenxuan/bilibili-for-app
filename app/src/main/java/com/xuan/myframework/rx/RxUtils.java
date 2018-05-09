package com.xuan.myframework.rx;


import io.reactivex.disposables.Disposable;

/**
 * Created by xuan on 2017/5/22.
 */

public class RxUtils {

    public static void cancelSubscription(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


}
