package com.xuan.myframework.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.myframework.base.Fragmentation.RxSupportFragment;
import com.xuan.myframework.base.presenter.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by xuan on 2017/5/19.
 */

public abstract class RxBaseFragment<T extends BasePresenter> extends RxSupportFragment implements IRxBaseFragmentInter {

    private Unbinder unbinder;
    protected T mPresenter;

//    /**
//     * fragment视图是否显示
//     */
//    protected boolean isVisible      = false;
//    /**
//     * 是否初始化完成，即onCreateView过，才可以doBusiness，否则会nullpoint
//     */
//    protected boolean isPrepared     = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        //初始化ButterKnife
        unbinder = ButterKnife.bind(this, view);
        //初始化控件
        initView(view);
        //初始化toolbar
        initToolBar();

        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        initData();
    }

    //    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        isPrepared=true;
//    }

//    /**
//     * 在onCreateView之前调用
//     * 在这里实现Fragment数据的缓加载
//     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()) {
//            isVisible = true;
//            onVisible();
//        } else {
//            isVisible = false;
//            onInvisible();
//        }
//    }
//
//    protected void onVisible() {
//        // 业务处理
//        if (isPrepared && isVisible) {
//            initData();
//        }
//    }
//
//    protected void onInvisible() {
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//
//        isVisible=false;
//        isPrepared=false;
    }


//    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";
//
//    /**
//     * 加载多个根Fragment
//     */
//    public void loadMultipleFragment(int containerId, int showPosition, Fragment... tos) {
//
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
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
//        ft.commitAllowingStateLoss();
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
//    public void showHideFragment(RxSupportFragment showFragment, Fragment hideFragment) {
//
//        if (showFragment == hideFragment) return;
//
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction().show(showFragment);
//
//        if (hideFragment == null) {
//            List<Fragment> fragmentList = getChildFragmentManager().getFragments();
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
