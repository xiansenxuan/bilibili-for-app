package com.xuan.myframework.inter;

import android.support.design.widget.AppBarLayout;

/**
 * Created by xuan on 2017/6/13.
 */

public abstract class AppbarStatusListener implements AppBarLayout.OnOffsetChangedListener {

    protected enum State {
        EXPANDED,

        COLLAPSED,

        IDLE
    }

    private State mCurrentState = State.IDLE;


    public abstract void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset);


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED, verticalOffset);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED, verticalOffset);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE, verticalOffset);
            }
            mCurrentState = State.IDLE;
        }
    }
}
