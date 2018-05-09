package com.xuan.myframework.view.presenter;

import android.app.Activity;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenter;

/**
 * com.xuan.myframework.view.presenter
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author by xuan on 2017/11/15
 * @version [版本号, 2017/11/15]
 * @update by xuan on 2017/11/15
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ClassificationPresenter extends BasePresenter{
    void queryClassificationData(Activity activity,LifecycleTransformer lifecycleTransformer);

    String readAssetsJson(Activity activity);
}
