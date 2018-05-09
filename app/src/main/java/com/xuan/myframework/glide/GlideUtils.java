package com.xuan.myframework.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.xuan.myframework.R;

/**
 * Created by xuan on 2017/6/1.
 */

public class GlideUtils {
    /**
     * 渐显动画时间 ms
     */
    public static final int duration = 500;
    public static final int placeholder = R.drawable.broken;
    public static final int error = R.drawable.broken;
    public static final int fallback = R.drawable.broken;

    public static RequestOptions defaultRequestOptions;
    public static RequestOptions circleRequestOptions;
    public static TransitionOptions transitionOptions;

    static {
        if (defaultRequestOptions == null) {
            defaultRequestOptions = new RequestOptions()
                    .centerCrop()
//                    .placeholder(placeholder)
                    .error(error)
                    .fallback(fallback)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
        }

        if (circleRequestOptions == null) {
            circleRequestOptions = new RequestOptions()
                    .centerCrop()
//                    .placeholder(placeholder)
                    .error(error)
                    .fallback(fallback)
                    .priority(Priority.HIGH)
                    .transform(new CircleTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
        }

        if (transitionOptions == null) {
            transitionOptions = new DrawableTransitionOptions().crossFade(duration);
        }
    }


//    private static void initDefaultRequestOptions() {
//        if (defaultRequestOptions == null) {
//            defaultRequestOptions = new RequestOptions()
//                    .centerCrop()
//                    .placeholder(placeholder)
//                    .error(error)
//                    .fallback(fallback)
//                    .priority(Priority.HIGH)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE);
//        }
//
//    }
//
//    private static void initCircleRequestOptions() {
//        if (circleRequestOptions == null) {
//            circleRequestOptions = new RequestOptions()
//                    .centerCrop()
//                    .placeholder(placeholder)
//                    .error(error)
//                    .fallback(fallback)
//                    .priority(Priority.HIGH)
//                    .transform(new CircleTransform())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE);
//        }
//
//    }
//
//    private static void initDefaultTransition() {
//        if (transitionOptions == null) {
//            transitionOptions = new DrawableTransitionOptions().crossFade(duration);
//        }
//    }

    /**
     * 加载网络图片 圆形
     *
     * @param context
     * @param url
     * @param iv
     */
    public static void loadCircleImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .apply(circleRequestOptions)
                .transition(transitionOptions)
                .into(iv);

    }

    /**
     * 加载网络图片 正常
     *
     * @param context
     * @param url
     * @param iv
     */
    public static void loadDefaultImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .apply(defaultRequestOptions)
                .transition(transitionOptions)
                .into(iv);
    }

    /**
     * 加载网络图片 带转换器
     *
     * @param context
     * @param url
     * @param iv
     */
    public static void loadDefaultImage(Context context, String url, ImageView iv,
                                        @NonNull RequestOptions options) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .transition(transitionOptions)
                .into(iv);
    }

}
