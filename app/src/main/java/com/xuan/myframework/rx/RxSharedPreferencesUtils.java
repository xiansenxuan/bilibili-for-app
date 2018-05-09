package com.xuan.myframework.rx;

import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.xuan.myframework.application.MyApplication;

/**
 * Created by xuan on 2017/5/19.
 */

public class RxSharedPreferencesUtils {
    private volatile static RxSharedPreferences rxsp;

    public static RxSharedPreferences getInstance() {
        if (rxsp == null) {
            synchronized (RxSharedPreferencesUtils.class){
                if(rxsp==null){
                    rxsp = RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance()));
                }
            }
        }
        return rxsp;
    }
}
