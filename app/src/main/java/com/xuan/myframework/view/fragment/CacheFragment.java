package com.xuan.myframework.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.xuan.myframework.R;
import com.xuan.myframework.base.fragment.RxBaseFragment;

/**
 * Created by xuan on 2017/5/26.
 */

public class CacheFragment extends RxBaseFragment {

    public static CacheFragment newInstance(Bundle args) {
        CacheFragment fragment = new CacheFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void initData() {
    }

    @Override
    public void initToolBar() {

    }
}
