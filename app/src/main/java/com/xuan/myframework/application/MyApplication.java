package com.xuan.myframework.application;

import android.app.Activity;
import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xuan.myframework.inter.ConfigInter;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Created by xuan on 2017/5/17.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private WeakReference<Activity> currentActivity;
    private WeakHashMap<WeakReference<Activity>, Object> activityList = new WeakHashMap<>();

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //蒲公英crash上报
//        PgyCrashManager.register(this);
        //初始化内存泄漏检测
//        LeakCanary.install(this);
        //初始化过度绘制检测
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        //初始化Fragment管理器
        initFragmentation();
        //初始化日志
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void initFragmentation() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                // ture时，遇到异常："Can not perform this action after onSaveInstanceState!"时，会抛出
                // false时，不会抛出，会捕获，可以在handleException()里监听到
                .debug(ConfigInter.is_debug)
                // 线上环境时，可能会遇到上述异常，此时debug=false，不会抛出该异常（避免crash），会捕获
                // 建议在回调处上传至我们的Crash检测服务器
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();
    }

    public void registerActivity(Activity act) {
        currentActivity = new WeakReference<>(act);
        activityList.put(currentActivity, null);
    }

    public void unregisterActivity(Activity act) {
        activityList.remove(act);
    }

    public void exitApp() {
        if (activityList != null && !activityList.isEmpty()) {
            synchronized (activityList) {
                Object[] keys = activityList.keySet().toArray();
                for (Object obj : keys) {
                    Activity act = (Activity) obj;
                    if (act != null && !act.isFinishing())
                        act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
