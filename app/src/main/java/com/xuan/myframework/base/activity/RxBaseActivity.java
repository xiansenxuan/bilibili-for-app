package com.xuan.myframework.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.xuan.myframework.application.MyApplication;
import com.xuan.myframework.base.Fragmentation.RxSupportActivity;
import com.xuan.myframework.base.presenter.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xuan on 2017/5/19.
 */

public abstract class RxBaseActivity<T extends BasePresenter> extends RxSupportActivity implements IRxBaseActivityInter {

    private Unbinder unbinder;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //设置布局
        setContentView(getLayoutId());
        //初始化
        unbinder = ButterKnife.bind(this);
        //初始化控件
        initView(savedInstanceState);
        //初始化toolbar
        initToolBar();
        //存储activity软引用
        MyApplication.getInstance().registerActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        //销毁activity软引用
        MyApplication.getInstance().unregisterActivity(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";
//
//    /**
//     * 加载多个根Fragment
//     */
//    public void loadMultipleFragment(int containerId, int showPosition, Fragment... tos) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        for (int i = 0; i < tos.length; i++) {
//            Fragment to = tos[i];
//
//            bindContainerId(containerId, tos[i]);
//
//            String toName = to.getClass().getName();
//            ft.add(containerId, to, toName);
//
//            if (i != showPosition) {
//                ft.hide(to);
//            }
//        }
//
//        ft.commitNowAllowingStateLoss();
//
//    }
//
//    public void bindContainerId(int containerId, Fragment to) {
//        Bundle args = to.getArguments();
//        if (args == null) {
//            args = new Bundle();
//            to.setArguments(args);
//        }
//        args.putInt(FRAGMENTATION_ARG_CONTAINER, containerId);
//    }
//
//    /**
//     * show一个Fragment,hide另一个／多个Fragment ; 主要用于类似微信主页那种 切换tab的情况
//     *
//     * @param showFragment 需要show的Fragment
//     * @param hideFragment 需要hide的Fragment
//     */
//    public void showHideFragment(Fragment showFragment, Fragment hideFragment) {
//        if (showFragment == hideFragment) return;
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().show(showFragment);
//
//        if (hideFragment == null) {
//            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//
//            if (fragmentList != null) {
//                for (Fragment fragment : fragmentList) {
//                    if (fragment != null && fragment != showFragment) {
//                        ft.hide(fragment);
//                    }
//                }
//            }
//        } else {
//            ft.hide(hideFragment);
//        }
//        ft.commitAllowingStateLoss();
//    }

}
