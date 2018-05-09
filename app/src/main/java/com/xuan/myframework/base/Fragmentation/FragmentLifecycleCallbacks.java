package com.xuan.myframework.base.Fragmentation;

import android.os.Bundle;


/**
 * Created by xuan on 2017/5/24.
 */

public class FragmentLifecycleCallbacks {

    /**
     * Called when the Fragment is called onSaveInstanceState().
     */
    public void onFragmentSaveInstanceState(RxSupportFragment fragment, Bundle outState) {

    }

    /**
     * Called when the Fragment is called onEnterAnimationEnd().
     */
    public void onFragmentEnterAnimationEnd(RxSupportFragment fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onLazyInitView().
     */
    public void onFragmentLazyInitView(RxSupportFragment fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onSupportVisible().
     */
    public void onFragmentSupportVisible(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onSupportInvisible().
     */
    public void onFragmentSupportInvisible(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onAttach().
     */
    public void onFragmentAttached(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onCreate().
     */
    public void onFragmentCreated(RxSupportFragment fragment, Bundle savedInstanceState) {

    }

    // 因为我们一般会移除super.onCreateView()来复写 onCreateView()  所以这里一般是捕捉不到onFragmentCreateView
//    /**
//     * Called when the Fragment is called onCreateView().
//     */
//    public void onFragmentCreateView(RxSupportFragment fragment, Bundle savedInstanceState) {
//
//    }

    /**
     * Called when the Fragment is called onCreate().
     */
    public void onFragmentViewCreated(RxSupportFragment fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onActivityCreated().
     */
    public void onFragmentActivityCreated(RxSupportFragment fragment, Bundle savedInstanceState) {

    }

    /**
     * Called when the Fragment is called onStart().
     */
    public void onFragmentStarted(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onResume().
     */
    public void onFragmentResumed(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onPause().
     */
    public void onFragmentPaused(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onStop().
     */
    public void onFragmentStopped(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onDestroyView().
     */
    public void onFragmentDestroyView(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onDestroy().
     */
    public void onFragmentDestroyed(RxSupportFragment fragment) {

    }

    /**
     * Called when the Fragment is called onDetach().
     */
    public void onFragmentDetached(RxSupportFragment fragment) {

    }
}
