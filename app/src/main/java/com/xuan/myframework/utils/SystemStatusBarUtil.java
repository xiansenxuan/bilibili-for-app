package com.xuan.myframework.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;

import com.xuan.myframework.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Created by xuan on 2017/6/13.
 */

public class SystemStatusBarUtil {

    private final boolean lightStatusBar;

    //透明且背景不占用控件的statusbar
    private final boolean transparentStatusBar;

    private final boolean transparentNavigationbar;

    private final Window window;

    private final View actionBarView;

    private final int current = Build.VERSION.SDK_INT;


    private SystemStatusBarUtil(Window window, boolean lightStatusBar, boolean transparentStatusBar,
                                boolean transparentNavigationbar, View actionBarView) {

        this.lightStatusBar = lightStatusBar;
        this.transparentStatusBar = transparentStatusBar;
        this.window = window;
        this.transparentNavigationbar = transparentNavigationbar;
        this.actionBarView = actionBarView;
    }


    private static float DEFAULT_ALPHA = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            ? 0.2f
            : 0.3f;


    /**
     * Android4.4以上的状态栏着色
     *
     * @param activity Activity对象
     * @param statusBarColor 状态栏颜色
     */
    public static void tintStatusBar(Activity activity, @ColorInt int statusBarColor) {

        tintStatusBar(activity, statusBarColor, DEFAULT_ALPHA);
    }


    /**
     * Android4.4以上的状态栏着色
     *
     * @param activity Activity对象
     * @param statusBarColor 状态栏颜色
     * @param alpha 透明栏透明度[0.0-1.0]
     */
    public static void tintStatusBar(Activity activity,
                                     @ColorInt int statusBarColor,
                                     @FloatRange(from = 0.0, to = 1.0) float alpha) {

        tintStatusBar(activity.getWindow(), statusBarColor, alpha);
    }


    /**
     * Android4.4以上的状态栏着色
     *
     * @param window 一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param statusBarColor 状态栏颜色
     */
    public static void tintStatusBar(Window window, @ColorInt int statusBarColor) {

        tintStatusBar(window, statusBarColor, DEFAULT_ALPHA);
    }


    /**
     * Android4.4以上的状态栏着色
     *
     * @param window 一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param statusBarColor 状态栏颜色
     * @param alpha 透明栏透明度[0.0-1.0]
     */
    public static void tintStatusBar(Window window,
                                     @ColorInt int statusBarColor,
                                     @FloatRange(from = 0.0, to = 1.0) float alpha) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup contentView = (ViewGroup) window.getDecorView()
                .findViewById(Window.ID_ANDROID_CONTENT);
        View rootView = contentView.getChildAt(0);
        if (rootView != null) {
            ViewCompat.setFitsSystemWindows(rootView, true);
        }

        setStatusBar(decorView, statusBarColor, true);
        setTranslucentView(decorView, alpha);
    }


    /**
     * Android4.4以上的状态栏着色(针对于DrawerLayout)
     * 注:
     * 1.如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性,尤其是DrawerLayout的fitsSystemWindows属性
     * 2.可以版本判断在5.0以上不调用该方法,使用系统自带
     *
     * @param activity Activity对象
     * @param drawerLayout DrawerLayout对象
     * @param statusBarColor 状态栏颜色
     */
    public static void tintStatusBarForDrawer(Activity activity, DrawerLayout drawerLayout,
                                              @ColorInt int statusBarColor) {

        tintStatusBarForDrawer(activity, drawerLayout, statusBarColor, DEFAULT_ALPHA);
    }


    /**
     * Android4.4以上的状态栏着色(针对于DrawerLayout)
     * 注:
     * 1.如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性,尤其是DrawerLayout的fitsSystemWindows属性
     * 2.可以版本判断在5.0以上不调用该方法,使用系统自带
     *
     * @param activity Activity对象
     * @param drawerLayout DrawerLayout对象
     * @param statusBarColor 状态栏颜色
     * @param alpha 透明栏透明度[0.0-1.0]
     */
    public static void tintStatusBarForDrawer(Activity activity, DrawerLayout drawerLayout,
                                              @ColorInt int statusBarColor,
                                              @FloatRange(from = 0.0, to = 1.0) float alpha) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        Window window = activity.getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup drawContent = (ViewGroup) drawerLayout.getChildAt(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            drawerLayout.setStatusBarBackgroundColor(statusBarColor);

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setStatusBar(decorView, statusBarColor, true, true);
        setTranslucentView(decorView, alpha);

        drawerLayout.setFitsSystemWindows(false);
        drawContent.setFitsSystemWindows(true);
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawer.setFitsSystemWindows(false);
    }


    /**
     * Android4.4以上的沉浸式全屏模式
     * 注:
     * 1.删除fitsSystemWindows属性:Android5.0以上使用该方法如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性
     * 或者调用forceFitsSystemWindows方法
     * 2.不删除fitsSystemWindows属性:也可以区别处理,Android5.0以上使用自己的方式实现,不调用该方法
     *
     * @param activity Activity对象
     */
    public static void immersiveStatusBar(Activity activity) {

        immersiveStatusBar(activity, DEFAULT_ALPHA);
    }


    /**
     * Android4.4以上的沉浸式全屏模式
     * 注:
     * 1.删除fitsSystemWindows属性:Android5.0以上使用该方法如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性
     * 或者调用forceFitsSystemWindows方法
     * 2.不删除fitsSystemWindows属性:也可以区别处理,Android5.0以上使用自己的方式实现,不调用该方法
     *
     * @param activity Activity对象
     * @param alpha 透明栏透明度[0.0-1.0]
     */
    public static void immersiveStatusBar(Activity activity,
                                          @FloatRange(from = 0.0, to = 1.0) float alpha) {

        immersiveStatusBar(activity.getWindow(), alpha);
    }


    /**
     * Android4.4以上的沉浸式全屏模式
     * 注:
     * 1.删除fitsSystemWindows属性:Android5.0以上使用该方法如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性
     * 或者调用forceFitsSystemWindows方法
     * 2.不删除fitsSystemWindows属性:也可以区别处理,Android5.0以上使用自己的方式实现,不调用该方法
     *
     * @param window 一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     */
    public static void immersiveStatusBar(Window window) {

        immersiveStatusBar(window, DEFAULT_ALPHA);
    }


    /**
     * Android4.4以上的沉浸式全屏模式
     * 注:
     * 1.删除fitsSystemWindows属性:Android5.0以上使用该方法如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性
     * 或者调用forceFitsSystemWindows方法
     * 2.不删除fitsSystemWindows属性:也可以区别处理,Android5.0以上使用自己的方式实现,不调用该方法
     *
     * @param window 一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param alpha 透明栏透明度[0.0-1.0]
     */
    public static void immersiveStatusBar(Window window,
                                          @FloatRange(from = 0.0, to = 1.0) float alpha) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup contentView = (ViewGroup) window.getDecorView()
                .findViewById(Window.ID_ANDROID_CONTENT);
        View rootView = contentView.getChildAt(0);
        int statusBarHeight = getStatusBarHeight(window.getContext());
        if (rootView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rootView.getLayoutParams();
            ViewCompat.setFitsSystemWindows(rootView, true);
            lp.topMargin = -statusBarHeight;
            rootView.setLayoutParams(lp);
        }

        setTranslucentView(decorView, alpha);
    }


    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    public static void setStatusBarDarkMode(Activity activity) {

        setStatusBarDarkMode(activity.getWindow());
    }


    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    public static void setStatusBarDarkMode(Window window) {

        if (isFlyme4Later()) {
            setStatusBarDarkModeForFlyme4(window, true);
        } else if (isMIUI6Later()) {
            setStatusBarDarkModeForMIUI6(window, true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarDarkModeForM(window);
        }
    }

    //------------------------->


    /**
     * android 6.0设置字体颜色
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setStatusBarDarkModeForM(Window window) {

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }


    /**
     * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     */
    public static boolean setStatusBarDarkModeForFlyme4(Window window, boolean dark) {

        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams e = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField(
                        "MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(e);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }

                meizuFlags.setInt(e, value);
                window.setAttributes(e);
                result = true;
            } catch (Exception var8) {
                Log.e("StatusBar", "setStatusBarDarkIcon: failed");
            }
        }

        return result;
    }


    /**
     * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
     * http://dev.xiaomi.com/doc/p=4769/
     */
    public static void setStatusBarDarkModeForMIUI6(Window window, boolean darkmode) {

        Class<? extends Window> clazz = window.getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建假的状态栏View
     */
    private static void setStatusBar(ViewGroup container, @ColorInt
            int statusBarColor, boolean visible, boolean addToFirst) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View statusBarView = container.findViewById(R.id.statusbar_view);
            if (statusBarView == null) {
                statusBarView = new View(container.getContext());
                statusBarView.setId(R.id.statusbar_view);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
                if (addToFirst) {
                    container.addView(statusBarView, 0, lp);
                } else {
                    container.addView(statusBarView, lp);
                }
            }

            statusBarView.setBackgroundColor(statusBarColor);
            statusBarView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * 创建假的状态栏View
     */
    private static void setStatusBar(ViewGroup container,
                                     @ColorInt int statusBarColor, boolean visible) {

        setStatusBar(container, statusBarColor, visible, false);
    }


    /**
     * 创建假的透明栏
     */
    private static void setTranslucentView(ViewGroup container,
                                           @FloatRange(from = 0.0, to = 1.0) float alpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View translucentView = container.findViewById(R.id.translucent_view);
            if (translucentView == null) {
                translucentView = new View(container.getContext());
                translucentView.setId(R.id.translucent_view);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
                container.addView(translucentView, lp);
            }

            translucentView.setBackgroundColor(Color.argb((int) (alpha * 255), 0, 0, 0));
        }
    }


    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {

        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }


    /**
     * 判断是否Flyme4以上
     */
    public static boolean isFlyme4Later() {

        return Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                ||
                Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
    }


    /**
     * 判断是否为MIUI6以上
     */
    public static boolean isMIUI6Later() {

        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method mtd = clz.getMethod("get", String.class);
            String val = (String) mtd.invoke(null, "ro.miui.ui.version.name");
            val = val.replaceAll("[vV]", "");
            int version = Integer.parseInt(val);
            return version >= 6;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     */
    public static void setHeightAndPadding(Context context, View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);//增高
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }


    /**
     * 增加View的paddingTop,增加的值为状态栏高度
     */
    public static void setPadding(Context context, View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }


    /**
     * 强制rootView下面的子View的FitsSystemWindows为false
     */
    public static void forceFitsSystemWindows(Activity activity) {

        forceFitsSystemWindows(activity.getWindow());
    }


    /**
     * 强制rootView下面的子View的FitsSystemWindows为false
     */
    public static void forceFitsSystemWindows(Window window) {

        forceFitsSystemWindows(
                (ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT));
    }


    /**
     * 强制rootView下面的子View的FitsSystemWindows为false
     */
    public static void forceFitsSystemWindows(ViewGroup viewGroup) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    forceFitsSystemWindows((ViewGroup) view);
                } else {
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        ViewCompat.setFitsSystemWindows(view, false);
                    }
                }
            }
        }
    }

    public static boolean isLessKitkat() {

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }


    public static Builder from(Activity activity) {

        return new Builder().setWindow(activity);
    }


    public static Builder from(Dialog dialog) {

        return new Builder().setWindow(dialog);
    }


    public static Builder from(Window window) {

        return new Builder().setWindow(window);
    }


    /**
     * Default status dp = 24 or 25
     * mhdpi = dp * 1
     * hdpi = dp * 1.5
     * xhdpi = dp * 2
     * xxhdpi = dp * 3
     * eg : 1920x1080, xxhdpi, => status/all = 25/640(dp) = 75/1080(px)
     * <p/>
     * don't forget toolbar's dp = 48
     *
     * @return px
     */
    @IntRange(from = 0, to = 75)
    public static int getStatusBarOffsetPx(Context context) {

        if (isLessKitkat()) {
            return 0;
        }
        Context appContext = context.getApplicationContext();
        int result = 0;
        int resourceId =
                appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @IntRange(from = 0, to = 75)
    public static int getNavigationBarOffsetPx(Context context) {

        if (isLessKitkat()) {
            return 0;
        }
        Context appContext = context.getApplicationContext();
        int result = 0;
        int resourceId =
                appContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void processActionBar(final View v) {

        if (v == null || !transparentStatusBar || isLessKitkat()) {
            return;
        }
        v.post(new Runnable() {
            @Override
            public void run() {
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop() + getStatusBarOffsetPx(v.getContext()),
                        v.getPaddingRight(), v.getPaddingBottom());
                v.getLayoutParams().height += getStatusBarOffsetPx(v.getContext());
            }
        });
    }


    /**
     * 调用私有API处理颜色
     */
    private void processPrivateAPI() {

        try {
            processFlyMe(lightStatusBar);
        } catch (Exception e) {
            try {
                processMIUI(lightStatusBar);
            } catch (Exception e2) {
                //
            }
        }
    }


    private void process() {
        //调用私有API处理颜色
        processPrivateAPI();
        processActionBar(actionBarView);

        //处理4.4~5.0沉浸
        if (current >= Build.VERSION_CODES.KITKAT && current < Build.VERSION_CODES.M) {
            processKitkat();
        } else if (current >= Build.VERSION_CODES.M) {
            processM();
        }
    }


    /**
     * 处理4.4沉浸
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void processKitkat() {

        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (transparentStatusBar) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }


    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
     * Tested on: MIUIV7 5.0 Redmi-Note3
     */
    private void processMIUI(boolean lightStatusBar) throws Exception {

        Class<? extends Window> clazz = window.getClass();
        int darkModeFlag;
        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
        darkModeFlag = field.getInt(layoutParams);
        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
        extraFlagField.invoke(window, lightStatusBar ? darkModeFlag : 0, darkModeFlag);
    }


    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private void processFlyMe(boolean isLightStatusBar) throws Exception {

        WindowManager.LayoutParams lp = window.getAttributes();
        Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
        int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
        Field field = instance.getDeclaredField("meizuFlags");
        field.setAccessible(true);
        int origin = field.getInt(lp);
        if (isLightStatusBar) {
            field.set(lp, origin | value);
        } else {
            field.set(lp, (~value) & origin);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void processM() {

        if (current < Build.VERSION_CODES.M) {
            return;
        }
        int flag = window.getDecorView().getSystemUiVisibility();
        if (lightStatusBar) {
            /**
             * 改变字体颜色
             * see {@link <a href="https://developer.android.com/reference/android/R.attr.html#windowLightStatusBar"></a>}
             */
            flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (transparentStatusBar) {
            flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (transparentNavigationbar) {
            flag |= (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        window.getDecorView().setSystemUiVisibility(flag);
    }


    public final static class Builder {

        private Window window;

        private boolean lightStatusBar = false;

        private boolean transparentStatusbar = false;

        private boolean transparentNavigationbar = false;

        private View actionBarView;


        public Builder setActionbarView(@NonNull View actionbarView) {

            this.actionBarView = actionbarView;
            return this;
        }


        private Builder setWindow(@NonNull Window Window) {

            this.window = Window;
            return this;
        }


        private Builder setWindow(@NonNull Activity activity) {

            this.window = activity.getWindow();
            return this;
        }


        private Builder setWindow(@NonNull Dialog dialog) {

            this.window = dialog.getWindow();
            return this;
        }


        public Builder setLightStatusBar(boolean lightStatusBar) {

            this.lightStatusBar = lightStatusBar;
            return this;
        }


        public Builder setTransparentStatusbar(boolean transparentStatusbar) {

            this.transparentStatusbar = transparentStatusbar;
            return this;
        }


        public Builder setTransparentNavigationbar(boolean transparentNavigationbar) {

            this.transparentNavigationbar = transparentNavigationbar;
            return this;
        }


        public void process() {

            new SystemStatusBarUtil(window, lightStatusBar, transparentStatusbar, transparentNavigationbar,
                    actionBarView).process();
        }
    }
}
