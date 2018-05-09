package com.xuan.myframework.view.presenterImpl;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.R;
import com.xuan.myframework.base.presenter.BasePresenterImpl;
import com.xuan.myframework.rx.http.RetrofitManager;
import com.xuan.myframework.utils.NumberUtils;
import com.xuan.myframework.view.activity.BroadcastTableActivity;
import com.xuan.myframework.view.modle.response.BroadcastTableModle;
import com.xuan.myframework.view.presenter.BroadcastTablePresenter;
import com.xuan.myframework.view.view.BroadcastTableView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * com.xuan.myframework.view.presenterImpl
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public class BroadcastTablePresenterImpl extends BasePresenterImpl<BroadcastTableView,List<BroadcastTableActivity.Visitable>> implements BroadcastTablePresenter {

    private List<BroadcastTableActivity.Visitable> visitableList;

    public BroadcastTablePresenterImpl(BroadcastTableView view){
        attachView(view);
    }


    @Override
    public void queryBroadcastTableData(LifecycleTransformer lifecycleTransformer) {
        beforeRequest();
        RetrofitManager.getInstance().queryBroadcastTableData(true, new Consumer<BroadcastTableModle>() {
            @Override
            public void accept(@NonNull BroadcastTableModle data) throws Exception {
                viewReference.get().hideEmptyView();

                visitableList=getVisitableData(data);

                onSuccess(visitableList);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                viewReference.get().showEmptyView();
                onError(throwable);
            }
        },lifecycleTransformer);
    }

    @Override
    public List<BroadcastTableActivity.Visitable> getVisitableData(BroadcastTableModle data) {
        if(visitableList==null){
            visitableList=new ArrayList<>();
        }else{
            visitableList.clear();
        }



        ArrayList<ArrayList<BroadcastTableModle.ResultBean>> itemList=new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            itemList.add(new ArrayList<BroadcastTableModle.ResultBean>());
        }

        if(data!=null && data.result!=null && data.result.size()>0){
            for (BroadcastTableModle.ResultBean bean : data.result) {
                switch (NumberUtils.getWeek(bean.pub_date)) {//判断星期几
                    case NumberUtils.SUNDAY_TYPE:
                        itemList.get(0).add(bean);
                        break;
                    case NumberUtils.MONDAY_TYPE:
                        itemList.get(1).add(bean);
                        break;
                    case NumberUtils.TUESDAY_TYPE:
                        itemList.get(2).add(bean);
                        break;
                    case NumberUtils.WEDNESDAY_TYPE:
                        itemList.get(3).add(bean);
                        break;
                    case NumberUtils.THURSDAY_TYPE:
                        itemList.get(4).add(bean);
                        break;
                    case NumberUtils.FRIDAY_TYEP:
                        itemList.get(5).add(bean);
                        break;
                    case NumberUtils.SATURDAY_TYPE:
                        itemList.get(6).add(bean);
                        break;
                }
            }

        }

        for (int i = 0; i < 7; i++) {
            //添加头
            BroadcastTableModle.SeasonModle modle=getSeasonModle(i);
            modle.time=itemList.get(i).get(0).pub_date;
            visitableList.add(new BroadcastTableModle().new SeasonHeadTypeModle(modle));
            //添加体
            for (BroadcastTableModle.ResultBean bean : itemList.get(i)) {
                visitableList.add(new BroadcastTableModle().new SeasonBodyTypeModle(bean));
            }
        }

        return visitableList;
    }

    private BroadcastTableModle.SeasonModle getSeasonModle(int index){
        BroadcastTableModle.SeasonModle modle=new BroadcastTableModle().new SeasonModle();

        switch (index) {//判断星期几
            case 0:
                modle.weekTitle=NumberUtils.SUNDAY_TYPE;
                modle.iconResId=R.drawable.bangumi_timeline_weekday_7;
                break;
            case 1:
                modle.weekTitle=NumberUtils.MONDAY_TYPE;
                modle.iconResId=R.drawable.bangumi_timeline_weekday_1;
                break;
            case 2:
                modle.weekTitle=NumberUtils.TUESDAY_TYPE;
                modle.iconResId=R.drawable.bangumi_timeline_weekday_2;
                break;
            case 3:
                modle.weekTitle=NumberUtils.WEDNESDAY_TYPE;
                modle.iconResId=R.drawable.bangumi_timeline_weekday_3;
                break;
            case 4:
                modle.weekTitle=NumberUtils.THURSDAY_TYPE;
                modle.iconResId=R.drawable.bangumi_timeline_weekday_4;
                break;
            case 5:
                modle.weekTitle=NumberUtils.FRIDAY_TYEP;
                modle.iconResId=R.drawable.bangumi_timeline_weekday_5;
                break;
            case 6:
                modle.weekTitle=NumberUtils.SATURDAY_TYPE;
                modle.iconResId=R.drawable.bangumi_timeline_weekday_6;
                break;
        }
        
        return modle;
    }
}
