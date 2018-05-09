package com.xuan.myframework.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.xuan.myframework.R;

/**
 * Created by xuan on 2017/6/19.
 */

public class SnackbarUtils {
    public final static int snackbar_bg_color= R.color.base_white;
    public final static int snackbar_text_color= R.color.base_pink;
    public final static int snackbar_action_color= R.color.base_pink;

    public static void showShortSnackbar(View view,String text){
        Snackbar snackbar=Snackbar.make(view,text,Snackbar.LENGTH_SHORT);

        View snackbarView=snackbar.getView();
        snackbarView.setBackgroundResource(snackbar_bg_color);
        ((TextView)snackbarView.findViewById(R.id.snackbar_text)).
                setTextColor(android.support.v4.content.ContextCompat.getColor(view.getContext(),snackbar_text_color));
        ((TextView)snackbarView.findViewById(R.id.snackbar_action)).
                setTextColor(android.support.v4.content.ContextCompat.getColor(view.getContext(),snackbar_action_color));

        snackbar.show();
    }

    public static void showLongSnackbar(View view,String text){
        Snackbar snackbar=Snackbar.make(view,text,Snackbar.LENGTH_LONG);

        View snackbarView=snackbar.getView();
        snackbarView.setBackgroundResource(snackbar_bg_color);
        ((TextView)snackbarView.findViewById(R.id.snackbar_text)).
                setTextColor(android.support.v4.content.ContextCompat.getColor(view.getContext(),snackbar_text_color));
        ((TextView)snackbarView.findViewById(R.id.snackbar_action)).
                setTextColor(android.support.v4.content.ContextCompat.getColor(view.getContext(),snackbar_action_color));

        snackbar.show();
    }
}
