package com.xuan.myframework.view.view;

import android.app.Activity;

import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.view.modle.response.ClassificationModle;

/**
 * com.xuan.myframework.view.view
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author by xuan on 2017/11/15
 * @version [版本号, 2017/11/15]
 * @update by xuan on 2017/11/15
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ClassificationView extends BaseView<ClassificationModle> {
    void showEmptyView();

    void hideEmptyView();

    void initRecycler();

    void initEmptyView();

}
