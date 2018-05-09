package com.xuan.myframework.rx.http;

import com.xuan.myframework.base.modle.BaseModle;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by xuan on 2017/6/5.
 */

public class SeparationResultFunction<T> implements Function<BaseModle<T>,T> {

    @Override
    public T apply(@NonNull BaseModle<T> tBaseModle){
        if(tBaseModle.code!=0){
            throw  new SeparationResultException(tBaseModle.code,tBaseModle.message);
        }
        return tBaseModle.data;
    }
}
