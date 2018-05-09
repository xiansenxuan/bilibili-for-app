package com.xuan.myframework.base.Fragmentation;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import java.util.ArrayList;

import me.yokeyword.fragmentation.helper.internal.TransactionRecord;

/**
 * Created by xuan on 2017/5/24.
 */

public abstract class SupportTransaction {

    /**
     * @param tag Optional tag name for the fragment, to later retrieve the
     *            fragment with {@link RxSupportFragment#findFragment(String)}
     *            , RxSupportFragment.pop(String)
     *            or FragmentManager.findFragmentByTag(String).
     * @return the same SupportTransaction instance.
     */
    public abstract SupportTransaction setTag(String tag);

    /**
     * start a RxSupportFragment for which you would like a result when it exits.
     *
     * @param requestCode If >= 0, this code will be returned in
     *                    onFragmentResult() when the fragment exits.
     * @return the same SupportTransaction instance.
     */
    public abstract SupportTransaction forResult(int requestCode);

    /**
     * @param launchMode Can replace {@link RxSupportFragment#start(RxSupportFragment, int)}
     *                   <p>
     *                   May be one of {@link RxSupportFragment#STANDARD}, {@link RxSupportFragment#SINGLETASK}
     *                   or {@link RxSupportFragment#SINGLETOP}.
     * @return the same SupportTransaction instance.
     */
    public abstract SupportTransaction setLaunchMode(@RxSupportFragment.LaunchMode int launchMode);

    /**
     * Can replace {@link RxSupportFragment#startWithPop(RxSupportFragment)}
     *
     * @param with return true if you need pop currentFragment
     * @return the same SupportTransaction instance.
     */
    public abstract SupportTransaction withPop(boolean with);

    /**
     * Used with custom Transitions to map a View from a removed or hidden
     * Fragment to a View from a shown or added Fragment.
     * <var>sharedElement</var> must have a unique transitionName in the View hierarchy.
     *
     * @param sharedElement A View in a disappearing Fragment to match with a View in an
     *                      appearing Fragment.
     * @param sharedName    The transitionName for a View in an appearing Fragment to match to the shared
     *                      element.
     * @return the same SupportTransaction instance.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public abstract SupportTransaction addSharedElement(View sharedElement, String sharedName);

    /**
     * same as FragmentTransaction.commitAllowingStateLoss()
     * <p>
     * Allows the commit to be executed after an
     * activity's state is saved.  This is dangerous because the commit can
     * be lost if the activity needs to later be restored from its state, so
     * this should only be used for cases where it is okay for the UI state
     * to change unexpectedly on the user.
     */
    public abstract <T extends RxSupportFragment> T commit();

    /**
     * Add some action when calling {@link RxSupportFragment#start(RxSupportFragment)
     * or SupportActivity/RxSupportFragment.startXXX()}
     */
    final static class SupportTransactionImpl<T extends RxSupportFragment> extends SupportTransaction {
        private T mRxSupportFragment;
        private TransactionRecord mRecord;

        SupportTransactionImpl(T RxSupportFragment) {
            this.mRxSupportFragment = RxSupportFragment;
            mRecord = new TransactionRecord();
        }

        @Override
        public SupportTransaction setTag(String tag) {
            mRecord.tag = tag;
            return this;
        }

        @Override
        public SupportTransaction forResult(int requestCode) {
            mRecord.requestCode = requestCode;
            return this;
        }

        @Override
        public SupportTransaction setLaunchMode(@RxSupportFragment.LaunchMode int launchMode) {
            mRecord.launchMode = launchMode;
            return this;
        }

        @Override
        public SupportTransaction withPop(boolean with) {
            mRecord.withPop = with;
            return this;
        }

        @Override
        public SupportTransaction addSharedElement(View sharedElement, String sharedName) {
            if (mRecord.sharedElementList == null) {
                mRecord.sharedElementList = new ArrayList<>();
            }
            mRecord.sharedElementList.add(new TransactionRecord.SharedElement(sharedElement, sharedName));
            return this;
        }

        @Override
        public T commit() {
            mRxSupportFragment.setTransactionRecord(mRecord);
            return mRxSupportFragment;
        }
    }
}
