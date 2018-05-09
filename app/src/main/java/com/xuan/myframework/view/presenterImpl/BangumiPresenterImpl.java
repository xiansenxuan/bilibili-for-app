package com.xuan.myframework.view.presenterImpl;

import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.R;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.base.view.BaseView;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.view.fragment.BangumiFragment;
import com.xuan.myframework.view.modle.response.BangumiModle;
import com.xuan.myframework.view.modle.response.BangumiRecommendedModle;
import com.xuan.myframework.view.presenter.BangumiPresenter;
import com.xuan.myframework.view.view.BangumiView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by xuan on 2017/6/2.
 */

public class BangumiPresenterImpl extends BasePresenterImpl<BangumiView,List<BangumiFragment.Visitable>> implements BangumiPresenter {
    private List<BangumiFragment.Visitable> visitableList;

    public BangumiPresenterImpl(BaseView view) {
        attachView(view);
    }

    @Override
    public void queryBangumiData(final LifecycleTransformer lifecycleTransformer) {
        beforeRequest();

        RetrofitManager.getInstance().queryBangumiData(true,lifecycleTransformer)
                .flatMap(new Function<BangumiModle, Observable<BangumiRecommendedModle>>() {
                    @Override
                    public Observable<BangumiRecommendedModle> apply(@NonNull BangumiModle data) throws Exception {

                        loadBangumiDataSuccess(data);

                        return RetrofitManager.getInstance().queryBangumiRecommendedData(true,lifecycleTransformer);
                    }
                }).subscribe(new Consumer<BangumiRecommendedModle>() {
            @Override
            public void accept(@NonNull BangumiRecommendedModle data) throws Exception {
                viewReference.get().hideEmptyView();

                loadBangumiRecommendedDataSuccess(data);

                onSuccess(visitableList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                viewReference.get().showEmptyView();
                onError(throwable);
            }
        });
    }

    @Override
    public void loadBangumiDataSuccess(BangumiModle data) {
        viewReference.get().addBannerHeader(data.result.ad.head);
        viewReference.get().addButtonHeader();
        visitableList=getVisitableData(data);
    }

    @Override
    public List<BangumiFragment.Visitable> getVisitableData(BangumiModle bangumiModle) {
        if(visitableList==null){
            visitableList=new ArrayList<>();
        }else{
            visitableList.clear();
        }

        if(bangumiModle!=null && bangumiModle.result!=null){
            //添加新番head
            BangumiModle.IconHeadModle iconHeadModle=new BangumiModle().new IconHeadModle();
            iconHeadModle.leftText="新番连载";
            iconHeadModle.rightText="所有连载";
            iconHeadModle.iconResId= R.drawable.ic_lianzai;
            iconHeadModle.listener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewReference.get().showShortSnackbar("所有连载，敬请期待!");
                }
            };

            visitableList.add(new BangumiModle().new IconHeadTypeModle(iconHeadModle));

            //添加新番item
            if(bangumiModle.result.serializing!=null && bangumiModle.result.serializing.size()>0) {
                for (BangumiModle.ResultBean.SerializingBean serializingBean : bangumiModle.result.serializing) {
                    visitableList.add(new BangumiModle().new NewBangumiBodyTypeModle(serializingBean));
                }
            }

            //添加大图
            if(bangumiModle.result.ad.body!=null && bangumiModle.result.ad.body.size()>0){
                for (BangumiModle.ResultBean.AdBean.BodyBean bodyBean : bangumiModle.result.ad.body) {
                    visitableList.add(new BangumiModle().new ImageHeadTypeModle(bodyBean));
                }
            }

            //添加月份新番头部
            if(bangumiModle.result.previous!=null){
                visitableList.add(new BangumiModle().new MoonHeadTypeModle(bangumiModle.result.previous));

                //添加月份新番
                if(bangumiModle.result.previous.list!=null && bangumiModle.result.previous.list.size()>0){
                    for (BangumiModle.ResultBean.PreviousBean.ListBean listBean : bangumiModle.result.previous.list) {
                        visitableList.add(new BangumiModle().new MonthNewBangumiBodyTypeModle(listBean));
                    }
                }
            }

        }

        return visitableList;
    }

    @Override
    public void loadBangumiRecommendedDataSuccess(BangumiRecommendedModle data) {
        visitableList=getVisitableData(data);
    }


    @Override
    public List<BangumiFragment.Visitable> getVisitableData(BangumiRecommendedModle data) {
        //添加番剧推荐
        if(data!=null && data.result!=null && data.result.size()>0){
            BangumiModle.IconHeadModle iconHeadModle=new BangumiModle().new IconHeadModle();
            iconHeadModle.leftText="番剧推荐";
            iconHeadModle.rightText="";
            iconHeadModle.iconResId= R.drawable.ic_lianzai;
            visitableList.add(new BangumiModle().new IconHeadTypeModle(iconHeadModle));

            for (BangumiRecommendedModle.ResultBean resultBean : data.result) {
                visitableList.add(new BangumiRecommendedModle().new BangumiRecommendBodyTypeModle(resultBean));
            }
        }
        return visitableList;
    }
}
