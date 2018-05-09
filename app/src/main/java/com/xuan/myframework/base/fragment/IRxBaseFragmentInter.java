package com.xuan.myframework.base.fragment;


import android.view.View;


/**
 * Created by xuan on 2017/5/19.
 */

public interface IRxBaseFragmentInter {

    int getLayoutId();

    void initView(View view);

    void initData();

    void initToolBar();
}
