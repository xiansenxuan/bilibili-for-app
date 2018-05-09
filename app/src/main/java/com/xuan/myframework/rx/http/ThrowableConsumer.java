package com.xuan.myframework.rx.http;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/6/2.
 */

public class ThrowableConsumer implements Consumer<Throwable> {
    @Override
    public void accept(@NonNull Throwable throwable) throws Exception {

    }
}
