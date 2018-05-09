package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.modle.response.CommentModle;
import com.xuan.myframework.view.presenter.CommentPresenter;
import com.xuan.myframework.view.view.CommentView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * com.xuan.myframework.view.presenterImpl
 * Created by xuan on 2017/7/6.
 * version
 * desc
 */

public class CommentPresenterImpl extends BasePresenterImpl<CommentView,CommentModle> implements CommentPresenter {
    public CommentPresenterImpl(BaseView view) {
        attachView(view);

        viewReference.get().initEmptyView();
        viewReference.get().initRecyclerView();
    }

    @Override
    public void queryCommentData(int aid,int pageNum,int pageSize,int ver, LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryCommentData(aid, pageNum, pageSize, ver,true
                , new Consumer<CommentModle>() {
                    @Override
                    public void accept(@NonNull CommentModle commentModle) throws Exception {
                        viewReference.get().hideEmptyView();
                        onSuccess(commentModle);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        viewReference.get().showEmptyView();
                        onError(throwable);
                    }
                },lifecycleTransformer);
    }
}
