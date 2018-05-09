package com.xuan.myframework.base.activity;

import android.os.Bundle;

/**
 * Created by xuan on 2017/5/19.
 */

public interface IRxBaseActivityInter {
    int getLayoutId();

    void initView(Bundle savedInstanceState);

    void initToolBar();
}
