package com.xuan.myframework.view.presenterImpl;

import android.app.Activity;
import android.content.res.AssetManager;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.utils.GsonManager;
import com.xuan.myframework.view.modle.response.ClassificationModle;
import com.xuan.myframework.view.presenter.ClassificationPresenter;
import com.xuan.myframework.view.view.ClassificationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * com.xuan.myframework.view.presenterImpl
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author by xuan on 2017/11/15
 * @version [版本号, 2017/11/15]
 * @update by xuan on 2017/11/15
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ClassificationPresenterImpl extends BasePresenterImpl<ClassificationView,ClassificationModle> implements ClassificationPresenter {
    public ClassificationPresenterImpl(ClassificationView view){
        attachView(view);
    }


    @Override
    public void queryClassificationData(Activity activity,LifecycleTransformer lifecycleTransformer) {
        beforeRequest();

        Observable<ClassificationModle> o=Observable.just(readAssetsJson(activity))
                .compose(lifecycleTransformer)
                .map(new Function<String,ClassificationModle>() {
                    @Override
                    public ClassificationModle apply(@NonNull String s) throws Exception {
                        return GsonManager.jsonToBean(s,ClassificationModle.class);
                    }
                });

        RetrofitManager.getInstance().queryClassificationData(true,o
        ,lifecycleTransformer)
                .subscribe(new Consumer<ClassificationModle>() {
                    @Override
                    public void accept(@NonNull ClassificationModle modle) throws Exception {
                        viewReference.get().hideEmptyView();
                        onSuccess(modle);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        viewReference.get().showEmptyView();
                        onError(throwable);
                    }
                });

    }

    /**
     * 读取assets下的json数据
     */
    @Override
    public String readAssetsJson(Activity activity) {
        AssetManager assetManager = activity.getAssets();
        try {
            InputStream is = assetManager.open("region.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
