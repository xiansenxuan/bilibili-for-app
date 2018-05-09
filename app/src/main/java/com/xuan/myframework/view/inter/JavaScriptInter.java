package com.xuan.myframework.view.inter;

import android.content.Context;
import android.webkit.JavascriptInterface;


/**
 * Created by xuan on 2017/6/7.
 */

public class JavaScriptInter {
    Context context;

    //sdk17版本以上加上注解
    public JavaScriptInter(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void imageZoom(String imageSrc, String imageListJson) {

    }
}
