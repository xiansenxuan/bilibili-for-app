package com.xuan.myframework.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * com.xuan.myframework.glide
 * Created by xuan on 2017/8/24.
 * version
 * desc
 */

public class BlurTransformation extends BitmapTransformation {
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return null;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
