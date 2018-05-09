package com.xuan.myframework.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.xuan.myframework.R;
import com.xuan.myframework.base.fragment.RxBaseFragment;

/**
 * Created by xuan on 2017/5/26.
 */

public class VipFragment extends RxBaseFragment {

    public static VipFragment newInstance(Bundle args) {
        VipFragment fragment = new VipFragment();
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
