package com.xuan.myframework.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import com.xuan.myframework.R;
import com.xuan.myframework.base.fragment.RxBaseFragment;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.DisplayUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.activity.LivePlayerActivity;
import com.xuan.myframework.view.activity.VideoDetailsActivity;
import com.xuan.myframework.view.activity.WebViewActivity;
import com.xuan.myframework.view.modle.response.RecommendBannerModle;
import com.xuan.myframework.view.modle.response.RecommendModle;
import com.xuan.myframework.view.presenterImpl.RecommendPresenterImpl;
import com.xuan.myframework.view.view.RecommendView;
import com.xuan.myframework.widget.ConvenientBanner;
import com.xuan.myframework.widget.EmptyView;
import com.xuan.myframework.widget.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/6/14.
 */

public class RecommendFragment extends RxBaseFragment<RecommendPresenterImpl> implements RecommendView {

    private int[] icons = new int[] {
            R.drawable.ic_header_hot, R.drawable.ic_head_live,
            R.drawable.ic_category_t39,R.drawable.ic_category_t13,
            R.drawable.ic_category_t1,
            R.drawable.ic_category_t3, R.drawable.ic_category_t129,
            R.drawable.ic_category_t4, R.drawable.ic_category_t119,
            R.drawable.ic_category_t36, R.drawable.ic_category_t160,
            R.drawable.ic_category_t155, R.drawable.ic_category_t5,
            R.drawable.ic_category_t11, R.drawable.ic_category_t23
    };


    @BindView(R.id.refresh_layout)MySwipeRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)LRecyclerView recycler_view;
    @BindView(R.id.empty_view)EmptyView empty_view;

    private MyRecyclerViewAdapter adapter;
    private LRecyclerViewAdapter lAdapter;
    private List<Visitable> visitableList;

    private static final String type_icon="recommend";
    private static final String type_live="live";
    private static final String type_bangumi_2="bangumi_2";

    private static final String style_live="gm_live";
    private static final String style_bangumi="gs_bangumi";

    @Override
    public void loadBannerDataSuccess(final RecommendBannerModle modle) {
        SnackbarUtils.showShortSnackbar(this.getView(),"loadBannerDataSuccess");

        View view= LayoutInflater.from(this.getContext()).inflate(R.layout.item_recommend_banner,null);
        lAdapter.removeHeaderView();
        lAdapter.addHeaderView(view);

        ConvenientBanner convenient_banner= (ConvenientBanner) view.findViewById(R.id.convenient_banner);
        //添加广告头部UI
        convenient_banner.setPages(new CBViewHolderCreator<ImageViewHodler>() {
            @Override
            public ImageViewHodler createHolder() {
                return new ImageViewHodler();
            }
        },modle.data)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.point_normal, R.drawable.point_focus}
                        , DisplayUtils.dp2px(getContext(),5),0,0,DisplayUtils.dp2px(getContext(),5)
                        ,DisplayUtils.dp2px(getContext(),5),DisplayUtils.dp2px(getContext(),5))
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        WebViewActivity.launchActivity(getActivity(),modle.data.get(position).image,
                                modle.data.get(position).title);
                    }
                });
    }

    private class ImageViewHodler implements Holder<RecommendBannerModle.BannerModle> {
        ImageView view;

        @Override
        public View createView(Context context) {
            view=new ImageView(context);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, RecommendBannerModle.BannerModle data) {
            GlideUtils.loadDefaultImage(context,data.image,view);
        }

    }

    @Override
    public void loadDataSuccess(RecommendModle data) {
        SnackbarUtils.showShortSnackbar(this.getView(),"loadDataSuccess");

        visitableList=getVisitableData(data);
        adapter.refreshData(visitableList);
        adapter.notifyDataSetChanged();
        recycler_view.scrollToPosition(0);
    }

    private List<Visitable> getVisitableData(RecommendModle data) {
        if(visitableList==null){
            visitableList=new ArrayList<>();
        }else{
            visitableList.clear();
        }

        if(data!=null && data.result!=null && data.result.size()>0){
            for (RecommendModle.ResultModle resultModle : data.result) {

                if(type_icon.equals(resultModle.type)){
                    visitableList.add(new RecommendModle().new IconHeadTypeModle(resultModle.head));
                }else if(type_live.equals(resultModle.type)){
                    visitableList.add(new RecommendModle().new LiveHeadTypeModle(resultModle.head));
                }else{
                    visitableList.add(new RecommendModle().new MoreHeadTypeModle(resultModle.head));
                }

                for (RecommendModle.BodyModle bodyModle : resultModle.body) {
                    if(style_live.equals(bodyModle.style)){
                        visitableList.add(new RecommendModle().new LiveBodyTypeModle(bodyModle));
                    }else if(style_bangumi.equals(bodyModle.style)){
                        visitableList.add(new RecommendModle().new BangumiBodyTypeModle(bodyModle));
                    }else{
                        visitableList.add(new RecommendModle().new IconBodyTypeModle(bodyModle));
                    }
                }

                if(type_icon.equals(resultModle.type)){
                    visitableList.add(new RecommendModle().new ChangeFoodTypeModle(resultModle.type));
                }else if(type_bangumi_2.equals(resultModle.type)){
                    visitableList.add(new RecommendModle().new BangumiFoodTypeModle(resultModle.type));
                }else{
                    visitableList.add(new RecommendModle().new RefreshFoodTypeModle(resultModle.type));
                }
            }
        }

        return visitableList;
    }

    @Override
    public void showProgress() {
        refresh_layout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        refresh_layout.setRefreshing(false);
    }

    @Override
    public void showMsg(String message) {
        SnackbarUtils.showShortSnackbar(this.getView(),"数据加载失败,请重新加载或者检查网络是否链接");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initView(View view) {

        initEmptyView();
        initRecyclerView();

        mPresenter=new RecommendPresenterImpl(this);

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPresenter.queryRecommendData(RecommendFragment.this.bindToLifecycle());
            }
        });
    }

    private void initRecyclerView() {
        if(adapter==null){
            adapter=new MyRecyclerViewAdapter(new ItemTypeFactory());
            lAdapter=new LRecyclerViewAdapter(adapter);

            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
            recycler_view.setLayoutManager(gridLayoutManager);

            recycler_view.setAdapter(lAdapter);

            //是否允许嵌套滑动
            recycler_view.setNestedScrollingEnabled(true);
            recycler_view.setHasFixedSize(true);
            recycler_view.setPullRefreshEnabled(false);
            recycler_view.setLoadMoreEnabled(false);


            lAdapter.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    if(visitableList!=null && visitableList.get(position)!=null){
                        Object item = visitableList.get(position);
                        if (item instanceof RecommendModle.IconHeadTypeModle ||
                                item instanceof RecommendModle.MoreHeadTypeModle ||
                                item instanceof RecommendModle.LiveHeadTypeModle ||
                                item instanceof RecommendModle.RefreshFoodTypeModle
                                || item instanceof RecommendModle.ChangeFoodTypeModle
                                || item instanceof RecommendModle.BangumiFoodTypeModle){
                            return 2;
                        }else if(item instanceof RecommendModle.IconBodyTypeModle || item instanceof RecommendModle.LiveBodyTypeModle ||
                                item instanceof RecommendModle.BangumiBodyTypeModle ){
                            return 1;
                        }else{
                            return gridLayoutManager.getSpanCount();
                        }
                    }else{
                        return gridLayoutManager.getSpanCount();
                    }

                }
            });

        }


    }

    @Override
    public void initData() {
        mPresenter.queryRecommendData(this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {

    }

    private void initEmptyView() {
        empty_view.setVisibility(View.GONE);
        empty_view.setEmptyImage(R.drawable.load_error);
        empty_view.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
    }

    @Override
    public void showEmptyView() {
        recycler_view.setVisibility(View.GONE);
        empty_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        recycler_view.setVisibility(View.VISIBLE);
        empty_view.setVisibility(View.GONE);
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecommendFragment.MyViewHolder> {
        private TypeFactory mTypeFactory;
        private List<Visitable> visitableList;


        public MyRecyclerViewAdapter(TypeFactory mTypeFactory) {
            this.mTypeFactory = mTypeFactory;
        }

        public void refreshData(List<Visitable> visitableList){
            if(this.visitableList!=null){
                this.visitableList=null;
            }
            this.visitableList=visitableList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i(RecommendFragment.class.getSimpleName(),
                    "viewType   "+viewType);
            return mTypeFactory.onCreateViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindItem(visitableList.get(position));
        }


        @Override
        public int getItemCount() {
            if(visitableList!=null && visitableList.size()>0){
                return visitableList.size();
            }else{
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return visitableList.get(position).type(mTypeFactory);
        }
    }

    public abstract class MyViewHolder<T extends Visitable> extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(T t);
    }

    public interface Visitable {

        int type(TypeFactory typeFactory);

    }

    public interface TypeFactory {

        int type(RecommendModle.IconHeadTypeModle head);
        int type(RecommendModle.MoreHeadTypeModle head);
        int type(RecommendModle.LiveHeadTypeModle head);
        int type(RecommendModle.IconBodyTypeModle body);
        int type(RecommendModle.LiveBodyTypeModle body);
        int type(RecommendModle.BangumiBodyTypeModle body);
        int type(RecommendModle.RefreshFoodTypeModle foot);
        int type(RecommendModle.ChangeFoodTypeModle foot);
        int type(RecommendModle.BangumiFoodTypeModle foot);

        MyViewHolder onCreateViewHolder(View itemView, int viewType);

    }

    public class ItemTypeFactory implements TypeFactory{

        @Override
        public int type(RecommendModle.IconHeadTypeModle head) {
            return R.layout.item_recommend_icon_title;
        }

        @Override
        public int type(RecommendModle.MoreHeadTypeModle head) {
            return R.layout.item_recommend_more_title;
        }

        @Override
        public int type(RecommendModle.LiveHeadTypeModle head) {
            return R.layout.item_recommend_live_title;
        }

        @Override
        public int type(RecommendModle.IconBodyTypeModle body) {
            return R.layout.item_recommend_icon_content;
        }

        @Override
        public int type(RecommendModle.LiveBodyTypeModle body) {
            return R.layout.item_recommend_live_content;
        }

        @Override
        public int type(RecommendModle.BangumiBodyTypeModle body) {
            return R.layout.item_recommend_bangumi_content;
        }

        @Override
        public int type(RecommendModle.RefreshFoodTypeModle foot) {
            return R.layout.item_recommend_refresh_food;
        }

        @Override
        public int type(RecommendModle.ChangeFoodTypeModle foot) {
            return R.layout.item_recommend_change_food;
        }

        @Override
        public int type(RecommendModle.BangumiFoodTypeModle foot) {
            return R.layout.item_recommend_bangumi_food;
        }

        @Override
        public MyViewHolder onCreateViewHolder(View itemView, int viewType) {
            MyViewHolder viewHolder=null;
            switch (viewType){
                case R.layout.item_recommend_icon_title:
                    viewHolder=new IconTitleViewHolder(itemView);
                    break;
                case R.layout.item_recommend_live_title:
                    viewHolder=new LiveTitleViewHolder(itemView);
                    break;
                case R.layout.item_recommend_more_title:
                    viewHolder=new MoreTitleViewHolder(itemView);
                    break;


                case R.layout.item_recommend_icon_content:
                    viewHolder=new IconContentViewHolder(itemView);
                    break;
                case R.layout.item_recommend_live_content:
                    viewHolder=new LiveContentViewHolder(itemView);
                    break;
                case R.layout.item_recommend_bangumi_content:
                    viewHolder=new BangumiContentViewHolder(itemView);
                    break;


                case R.layout.item_recommend_refresh_food:
                    viewHolder=new RefreshFoodViewHolder(itemView);
                    break;
                case R.layout.item_recommend_change_food:
                    viewHolder=new ChangeFoodViewHolder(itemView);
                    break;
                case R.layout.item_recommend_bangumi_food:
                    viewHolder=new BangumiFoodViewHolder(itemView);
                    break;
                default:
                    break;
            }
            return viewHolder;
        }
    }

    class IconTitleViewHolder extends MyViewHolder{
        @BindView(R.id.iv_icon)ImageView iv_icon;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_right)TextView tv_right;

        public IconTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.IconHeadTypeModle head= (RecommendModle.IconHeadTypeModle) visitable;
            RecommendModle.HeadModle modle=head.head;

            setTypeIcon(modle.title,iv_icon);
            tv_title.setText(modle.title);

            RxView.clicks(tv_right)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            //排行榜
                        }
                    });

        }
    }

    class LiveTitleViewHolder extends MyViewHolder{
        @BindView(R.id.iv_icon)ImageView iv_icon;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_count)TextView tv_count;
        @BindView(R.id.tv_right)TextView tv_right;

        public LiveTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.LiveHeadTypeModle head= (RecommendModle.LiveHeadTypeModle) visitable;
            RecommendModle.HeadModle modle=head.head;

            setTypeIcon(modle.title,iv_icon);
            tv_title.setText(modle.title);

            SpannableStringBuilder stringBuilder = new SpannableStringBuilder("当前" + modle.count + "个直播");
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(
                    ContextCompat.getColor(getContext(),R.color.base_pink));
            stringBuilder.setSpan(foregroundColorSpan, 2,
                    stringBuilder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_count.setText(stringBuilder);

            RxView.clicks(tv_right)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            SnackbarUtils.showShortSnackbar(getView(),"敬請期待!");
                        }
                    });
        }
    }

    class MoreTitleViewHolder extends MyViewHolder{
        @BindView(R.id.iv_icon)ImageView iv_icon;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_right)TextView tv_right;

        public MoreTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.MoreHeadTypeModle head= (RecommendModle.MoreHeadTypeModle) visitable;
            RecommendModle.HeadModle modle=head.head;

            setTypeIcon(modle.title,iv_icon);
            tv_title.setText(modle.title);

            RxView.clicks(tv_right)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            SnackbarUtils.showShortSnackbar(getView(),"敬請期待!");
                        }
                    });
        }
    }

    class IconContentViewHolder extends MyViewHolder{
        @BindView(R.id.card_view)CardView card_view;
        @BindView(R.id.iv_photo)ImageView iv_photo;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_play_count)TextView tv_play_count;
        @BindView(R.id.tv_danmaku)TextView tv_comment;

        public IconContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.IconBodyTypeModle body= (RecommendModle.IconBodyTypeModle) visitable;
            final RecommendModle.BodyModle modle=body.body;

            GlideUtils.loadDefaultImage(getActivity(),modle.cover,iv_photo);

            tv_title.setText(modle.title);
            tv_play_count.setText(modle.play);
            tv_comment.setText(modle.danmaku);

            RxView.clicks(card_view)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            VideoDetailsActivity.launchActivity(getActivity(),
                                    Integer.valueOf(modle.param),modle.cover);
                        }
                    });

        }
    }
    class LiveContentViewHolder extends MyViewHolder{
        @BindView(R.id.card_view)CardView card_view;
        @BindView(R.id.iv_photo)ImageView iv_photo;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_name)TextView tv_name;
        @BindView(R.id.tv_live_count)TextView tv_live_count;

        public LiveContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            final RecommendModle.LiveBodyTypeModle body= (RecommendModle.LiveBodyTypeModle) visitable;
            final RecommendModle.BodyModle modle=body.body;

            GlideUtils.loadDefaultImage(getActivity(),modle.cover,iv_photo);

            tv_title.setText(modle.title);
            tv_name.setText(modle.up);
            tv_live_count.setText(modle.online+"");

            RxView.clicks(card_view)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            LivePlayerActivity.launchActivity(getActivity(),
                                    Integer.valueOf(modle.param),modle.title,modle.online,modle.upFace,modle.up,0);
                        }
                    });
        }
    }

    class BangumiContentViewHolder extends MyViewHolder{
        @BindView(R.id.card_view)CardView card_view;
        @BindView(R.id.iv_photo)ImageView iv_photo;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_name)TextView tv_name;

        public BangumiContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.BangumiBodyTypeModle body= (RecommendModle.BangumiBodyTypeModle) visitable;
            RecommendModle.BodyModle modle=body.body;

            GlideUtils.loadDefaultImage(getActivity(),modle.cover,iv_photo);

            tv_title.setText(modle.title);
            tv_name.setText(modle.desc1);
        }
    }

    class RefreshFoodViewHolder extends MyViewHolder{
        @BindView(R.id.tv_more)TextView tv_more;
        @BindView(R.id.tv_count)TextView tv_count;
        @BindView(R.id.iv_refresh)ImageView iv_refresh;

        public RefreshFoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.RefreshFoodTypeModle food= (RecommendModle.RefreshFoodTypeModle) visitable;
            String type=food.type;

            tv_count.setText(String.valueOf(new Random().nextInt(200)) + "条新动态,点这里刷新");

            RxView.clicks(tv_more)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            SnackbarUtils.showShortSnackbar(getView(),"敬請期待!");
                        }
                    });

            RxView.clicks(iv_refresh)
                    .throttleFirst(800,TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            iv_refresh.animate()
                                    .rotation(360)
                                    .setInterpolator(new LinearInterpolator())
                                    .setDuration(1000).start();
                        }
                    });
        }
    }
    class ChangeFoodViewHolder extends MyViewHolder{
        @BindView(R.id.iv_refresh)ImageView iv_refresh;

        public ChangeFoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.ChangeFoodTypeModle food= (RecommendModle.ChangeFoodTypeModle) visitable;
            String type=food.type;

            RxView.clicks(iv_refresh)
                    .throttleFirst(500,TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            iv_refresh.animate()
                                    .rotation(360)
                                    .setInterpolator(new LinearInterpolator())
                                    .setDuration(1000).start();
                        }
                    });
        }
    }
    class BangumiFoodViewHolder extends MyViewHolder{
        @BindView(R.id.iv_bangumi_timeline)ImageView iv_bangumi_timeline;
        @BindView(R.id.iv_bangumi_index)ImageView iv_bangumi_index;

        public BangumiFoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            RecommendModle.BangumiFoodTypeModle food= (RecommendModle.BangumiFoodTypeModle) visitable;
            String type=food.type;

            RxView.clicks(iv_bangumi_timeline)
                    .throttleFirst(800,TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {

                        }
                    });
        }
    }

    /**
     * 根据title设置typeIcon
     */
    private void setTypeIcon(String title,ImageView imageView) {

        switch (title) {
            case "热门焦点":
                imageView.setImageResource(icons[0]);
                break;
            case "正在直播":
                imageView.setImageResource(icons[1]);
                break;
            case "番剧区":
                imageView.setImageResource(icons[2]);
                break;
            case "国创区":
                imageView.setImageResource(icons[3]);
                break;
            case "动画区":
                imageView.setImageResource(icons[4]);
                break;
            case "音乐区":
                imageView.setImageResource(icons[5]);
                break;
            case "舞蹈区":
                imageView.setImageResource(icons[6]);
                break;
            case "游戏区":
                imageView.setImageResource(icons[7]);
                break;
            case "鬼畜区":
                imageView.setImageResource(icons[8]);
                break;
            case "科技区":
                imageView.setImageResource(icons[9]);
                break;
            case "生活区":
                imageView.setImageResource(icons[10]);
                break;
            case "时尚区":
                imageView.setImageResource(icons[11]);
                break;
            case "娱乐区":
                imageView.setImageResource(icons[12]);
                break;
            case "电视剧区":
                imageView.setImageResource(icons[13]);
                break;
            case "电影区":
                imageView.setImageResource(icons[14]);
                break;
        }
    }

    public static RecommendFragment newInstance(Bundle args) {
        RecommendFragment fragment=new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
