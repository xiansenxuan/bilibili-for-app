package com.xuan.myframework.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.myframework.R;

/**
 * Created by xuan on 2017/6/2.
 */

public class EmptyView extends FrameLayout {
    private ImageView iv_empty;
    private TextView tv_empty;

    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, this);
        iv_empty = (ImageView) view.findViewById(R.id.iv_empty);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);

    }

    public void setEmptyImage(int resId) {
        iv_empty.setImageResource(resId);
    }

    public void setEmptyText(String text) {
        tv_empty.setText(text);
    }

}
