package com.xuan.myframework.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.xuan.myframework.R;

/**
 * Created by xuan on 2017/6/2.
 */
public class MySwipeRefreshLayout extends  SwipeRefreshLayout{
    private int[] resId= {R.color.base_pink};

    public MySwipeRefreshLayout(Context context) {
        super(context);
        initView();
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        setColorSchemeResources(resId);
    }


}
