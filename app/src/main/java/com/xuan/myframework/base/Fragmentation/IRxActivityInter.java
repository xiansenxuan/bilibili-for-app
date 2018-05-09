package com.xuan.myframework.base.Fragmentation;


/**
 * Created by xuan on 2017/5/24.
 */

public interface IRxActivityInter {

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    void loadRootFragment(int containerId, RxSupportFragment toFragment);

    /**
     * 以replace方式加载根Fragment
     */
    void replaceLoadRootFragment(int containerId, RxSupportFragment toFragment, boolean addToBack);

    /**
     * 加载多个根Fragment
     *
     * @param containerId 容器id
     * @param toFragments 目标Fragments
     */
    void loadMultipleRootFragment(int containerId, int showPosition, RxSupportFragment... toFragments);

    /**
     * show一个Fragment,hide上一个Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     *
     * @param showFragment 需要show的Fragment
     */
    void showHideFragment(RxSupportFragment showFragment);

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     *
     * @param showFragment 需要show的Fragment
     * @param hideFragment 需要hide的Fragment
     */
    void showHideFragment(RxSupportFragment showFragment, RxSupportFragment hideFragment);

    /**
     * 启动目标Fragment
     *
     * @param toFragment 目标Fragment
     */
    void start(RxSupportFragment toFragment);

    /**
     * 启动目标Fragment
     *
     * @param toFragment 目标Fragment
     * @param launchMode 启动模式
     */
    void start(RxSupportFragment toFragment, @RxSupportFragment.LaunchMode int launchMode);

    /**
     * 类似startActivityForResult
     *
     * @param toFragment  目标Fragment
     * @param requestCode requsetCode
     */
    void startForResult(RxSupportFragment toFragment, int requestCode);

    /**
     * 启动目标Fragment,并pop当前Fragment
     *
     * @param toFragment 目标Fragment
     */
    void startWithPop(RxSupportFragment toFragment);

    /**
     * @return 栈顶Fragment
     */
    RxSupportFragment getTopFragment();

    /**
     * @param fragmentClass 目标Fragment的Class
     * @param <T>           继承自RxSupportFragment的Fragment
     * @return 目标Fragment
     */
    <T extends RxSupportFragment> T findFragment(Class<T> fragmentClass);

    <T extends RxSupportFragment> T findFragment(String fragmentTag);

    /**
     * 出栈
     */
    void pop();

    /**
     * 出栈到目标Fragment
     *
     * @param targetFragmentClass     目标Fragment的Class
     * @param includeTargetFragment   是否包含目标Fragment
     */
    void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment);

    void popTo(String targetFragmentTag, boolean includeTargetFragment);

    /**
     * 出栈到目标Fragment,并在出栈后立即进行Fragment事务(可以防止出栈后,直接进行Fragment事务的异常)
     *
     * @param targetFragmentClass               目标Fragment的Class
     * @param includeTargetFragment             是否包含目标Fragment
     * @param afterPopTransactionRunnable       出栈后紧接着的Fragment事务
     */
    void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable);

    void popTo(String targetFragmentTag, boolean includeTargetFragment, Runnable afterPopTransactionRunnable);
}
