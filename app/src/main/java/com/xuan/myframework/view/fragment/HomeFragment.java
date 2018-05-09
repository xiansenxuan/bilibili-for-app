package com.xuan.myframework.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.xuan.myframework.R;
import com.xuan.myframework.base.fragment.RxBaseFragment;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.DisplayUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.activity.LivePlayerActivity;
import com.xuan.myframework.view.activity.WebViewActivity;
import com.xuan.myframework.view.modle.response.LiveDataModle;
import com.xuan.myframework.view.presenterImpl.HomePresenterImpl;
import com.xuan.myframework.view.view.HomeView;
import com.xuan.myframework.widget.ConvenientBanner;
import com.xuan.myframework.widget.EmptyView;
import com.xuan.myframework.widget.MySwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/5/26.
 */
public class HomeFragment extends RxBaseFragment<HomePresenterImpl> implements HomeView {
    @BindView(R.id.refresh_layout)MySwipeRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)RecyclerView recycler_view;
    @BindView(R.id.empty_view)EmptyView empty_view;

    MyRecyclerViewAdapter adapter;

    private int[] entranceIcons = new int[] {
            R.drawable.live_home_follow_anchor,
            R.drawable.live_home_live_center,
            R.drawable.live_home_search_room,
            R.drawable.live_home_all_category
    };

    private String[] entranceTitles = new String[] {
            "关注主播", "直播中心",
            "搜索直播", "全部分类"
    };

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view) {
        mPresenter =new HomePresenterImpl(this);

        initEmptyView();

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.queryLiveData(HomeFragment.this.bindToLifecycle());
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.queryLiveData(this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {
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
        SnackbarUtils.showShortSnackbar(recycler_view,"数据加载失败,请重新加载或者检查网络是否链接");
    }

    @Override
    public void loadDataSuccess(final LiveDataModle modle) {
        //设置adapter
        if(adapter==null){
            adapter=new MyRecyclerViewAdapter();
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),12,GridLayoutManager.VERTICAL,false);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.getSpanSize(position);
                }
            });

            recycler_view.setLayoutManager(gridLayoutManager);
            recycler_view.setAdapter(adapter);

            adapter.refreshData(modle);
            adapter.notifyDataSetChanged();
            recycler_view.scrollToPosition(0);
        }else{
            adapter.refreshData(modle);
            adapter.notifyDataSetChanged();
            recycler_view.scrollToPosition(0);
        }

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


//    private void addHeaderView(LiveDataModle modle){
//        adapter.removeAllHeaderView();
//
//        //添加广告头部UI
//        View bannerView= getActivity().getLayoutInflater().inflate(R.layout.item_live_banner, (ViewGroup) recycler_view.getParent(),false);
//        ConvenientBanner convenient_banner= (ConvenientBanner) bannerView.findViewById(R.id.convenient_banner);
//        convenient_banner.setPages(new CBViewHolderCreator<ImageViewHodler>() {
//            @Override
//            public ImageViewHodler createHolder() {
//                return new ImageViewHodler();
//            }
//        },modle.banner)
//        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//        .setPageIndicator(new int[]{R.drawable.point_normal, R.drawable.point_focus}
//        , DisplayUtils.dp2px(getContext(),5),0,0,DisplayUtils.dp2px(getContext(),5)
//                ,DisplayUtils.dp2px(getContext(),5),DisplayUtils.dp2px(getContext(),5))
//        //设置指示器的方向
//        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
//        //设置翻页的效果，不需要翻页效果可用不设
//        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
////        convenientBanner.setManualPageable(false);//设置不能手动影响
//
//        adapter.addHeaderView(bannerView,0);
//
//        //添加入口头部UI
//        LinearLayout linearLayout=new LinearLayout(getActivity());
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        for (int i = 0; i < entranceTitles.length; i++) {
//            View entranceView= getActivity().getLayoutInflater().inflate(R.layout.item_live_entrance, linearLayout,false);
//            ImageView iv_header= (ImageView) entranceView.findViewById(R.id.iv_header);
//            TextView tv_title= (TextView) entranceView.findViewById(R.id.tv_title);
//
//            iv_header.setImageResource(entranceIcons[i]);
//            tv_title.setText(entranceTitles[i]);
//
//            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) entranceView.getLayoutParams();
//            params.weight=1;
//            params.width=0;
//            entranceView.setLayoutParams(params);
//
//            linearLayout.addView(entranceView);
//        }
//
//        adapter.addHeaderView(linearLayout,1);
//    }


    private class ImageViewHodler implements Holder<LiveDataModle.LiveBanner>{
        ImageView view;

        @Override
        public View createView(Context context) {
            view=new ImageView(context);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, LiveDataModle.LiveBanner data) {
            GlideUtils.loadDefaultImage(context,data.img,view);
        }

    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final int type_banner=1;
        public static final int type_entrance=2;
        public static final int type_title=3;
        public static final int type_content=4;

        private int entranceSize=4;//默认入口4个按钮

        private LiveDataModle modle;

        public void refreshData(LiveDataModle modle){
            if(this.modle!=null){
                this.modle=null;
            }
            this.modle=modle;
        }

        /**
         * @param position
         * @return
         * GridLayoutManager 权重12
         * banner占满一行 权重12
         * entrance一行4个 权重3
         * title占满一行 权重12
         * content一行2个 权重6
         *
         */
        public int getSpanSize(int position){
            int viewType=getItemViewType(position);
            switch (viewType){
                case type_banner:
                    return 12;
                case type_entrance:
                    return 3;
                case type_title:
                    return 12;
                case type_content:
                    return 6;
                default:
                    return 0;
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if(viewType==type_banner){
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_banner,null);
                return new BannerViewHodler(view);

            }else if(viewType==type_entrance){
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_entrance,null);
                return new EntranceViewHodler(view);

            }else if(viewType==type_title){
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_partitions_title,null);
                return new TitleViewHodler(view);

            }else if(viewType==type_content){
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_partitions_content,null);
                return new ContentViewHodler(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof BannerViewHodler){
                BannerViewHodler viewHodler= (BannerViewHodler) holder;
                addBannerItem(modle,viewHodler.convenient_banner);

            }else if(holder instanceof EntranceViewHodler){
                EntranceViewHodler viewHodler= (EntranceViewHodler) holder;
                addEntranceItem(viewHodler.iv_header,viewHodler.tv_title,position);

            }else if(holder instanceof TitleViewHodler){
                TitleViewHodler viewHodler= (TitleViewHodler) holder;

                LiveDataModle.Partition partition =
                        modle.partitions.get(getTitlePosition(position)).partition;

                GlideUtils.loadCircleImage(HomeFragment.this.getActivity()
                ,partition.sub_icon.src,viewHodler.iv_live_icon);

                viewHodler.tv_live_name.setText(partition.name);

                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
                        "当前" + partition.count + "个直播");
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(
                        android.support.v4.content.ContextCompat.getColor(getContext(),R.color.base_pink));
                stringBuilder.setSpan(foregroundColorSpan, 2,
                        stringBuilder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHodler.tv_live_num.setText(stringBuilder);

            }else if(holder instanceof ContentViewHodler){
                ContentViewHodler viewHodler = (ContentViewHodler) holder;

                final LiveDataModle.Lives lives= modle.partitions.get(getTitlePosition(position))
                        .lives.get(getContentPosition(position));

                GlideUtils.loadDefaultImage(HomeFragment.this.getActivity(),
                        lives.cover.src,viewHodler.iv_photo);

                GlideUtils.loadCircleImage(HomeFragment.this.getActivity(),
                        lives.cover.src,viewHodler.iv_header);

                viewHodler.tv_title.setText(lives.title);
                viewHodler.tv_name.setText(lives.owner.name);
                viewHodler.tv_live_count.setText(lives.online+"");

                RxView.clicks(viewHodler.card_view)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                LivePlayerActivity.
                                        launchActivity(getActivity(), lives.room_id,
                                                lives.title, lives.online, lives.owner.face,
                                                lives.owner.name, lives.owner.mid);
                            }
                        });

            }

        }

        public void addBannerItem(final LiveDataModle modle, ConvenientBanner convenient_banner){
            //添加广告头部UI
            convenient_banner.setPages(new CBViewHolderCreator<ImageViewHodler>() {
                @Override
                public ImageViewHodler createHolder() {
                    return new ImageViewHodler();
                }
            },modle.banner)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.drawable.point_normal, R.drawable.point_focus}
                            , DisplayUtils.dp2px(getContext(),5),0,0,DisplayUtils.dp2px(getContext(),5)
                            ,DisplayUtils.dp2px(getContext(),5),DisplayUtils.dp2px(getContext(),5))
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            WebViewActivity.launchActivity(getActivity(),modle.banner.get(position).link,
                                    modle.banner.get(position).title);
                        }
                    });
            //设置翻页的效果，不需要翻页效果可用不设
            //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(false);//设置不能手动影响
        }

        public void addEntranceItem(ImageView iv_header,TextView tv_title,int position){
            //添加入口头部UI
            iv_header.setImageResource(entranceIcons[position-1]);
            tv_title.setText(entranceTitles[position-1]);
        }

        @Override
        public int getItemCount() {
            int bannerCount=0;
            int entranceCount=0;
            int titleCount=0;
            int contentCount=0;

            if(modle!=null){
                if(modle.banner!=null && modle.banner.size()>0){
                    bannerCount=1;
                }

                entranceCount=entranceIcons.length;

                if(modle.partitions!=null && modle.partitions.size()>0){
                    //因为一组只取4个Lives数据
                    for (LiveDataModle.LivePartitions partition : modle.partitions) {
                        if(partition.lives!=null && partition.lives.size()>=entranceSize){
                            titleCount+=1;
                            contentCount+=entranceSize;
                        }
                    }
                }

            }

            return bannerCount+entranceCount+titleCount+contentCount;
        }

        @Override
        public int getItemViewType(int position) {
            if(position==0){
                return type_banner;
            }else if(0<position && position<=entranceSize){
                return type_entrance;
            }else if(isPartitionsTitle(position)){
                return type_title;
            }else{
                return type_content;
            }
        }

        /**
         * @param position
         * @return
         * 根据真实position获取数据取第几个Partitions
         * 也就是第几个title
         *
         */
        private int getTitlePosition(int position){
            //第几个title
            int titlePosition=position;

            if(modle!=null){
                if(modle.banner!=null && modle.banner.size()>0){
                    titlePosition-=1;
                }

                titlePosition-=entranceSize;
            }

            //整除得知是第几组
            //比如: position=6-9 那么第0组
            //     position=10 那么第1组
            return titlePosition/5;
        }

        /**
         * @param position
         * @return
         * 根据真实position获取数据取第几个Partitions 然后继续获取lives
         * 也就是第几个title的第几个content
         *
         */
        private int getContentPosition(int position){
            //第几个title
            int titlePosition=position;
            int contentPosition=position;

            if(modle!=null){
                if(modle.banner!=null && modle.banner.size()>0){
                    titlePosition-=1;
                    contentPosition-=1;
                }

                titlePosition-=entranceSize;
                contentPosition-=entranceSize;
            }

            //整除得知是第几组
            // position=7 那么第0组 第2个
            // position=12 那么第1组 第2个
            titlePosition/=5;
            contentPosition-=titlePosition*5;

            return contentPosition;
        }

        /**
         * @param position
         * @return
         * 0 banner
         * 1-4 入口4个按钮
         * 5 title
         * 6-9 content
         * 10 title
         * 11-15 content
         * 15 title
         * 规则:
         * (5-5)%5==0
         * (10-5)%5==0
         * (15-5)%5==0
         */
        private boolean isPartitionsTitle(int position) {

            position -= (1+entranceSize);

            return (position % 5 == 0);
        }

        class BannerViewHodler extends RecyclerView.ViewHolder{
            @BindView(R.id.convenient_banner)ConvenientBanner convenient_banner;

            public BannerViewHodler(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }

        class EntranceViewHodler extends RecyclerView.ViewHolder{
            @BindView(R.id.iv_header)ImageView iv_header;
            @BindView(R.id.tv_title)TextView tv_title;

            public EntranceViewHodler(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }

        class TitleViewHodler extends RecyclerView.ViewHolder{
            @BindView(R.id.iv_live_icon)ImageView iv_live_icon;
            @BindView(R.id.tv_live_name)TextView tv_live_name;
            @BindView(R.id.tv_live_num)TextView tv_live_num;

            public TitleViewHodler(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }

        class ContentViewHodler extends RecyclerView.ViewHolder{
            @BindView(R.id.card_view)CardView card_view;
            @BindView(R.id.iv_photo)ImageView iv_photo;
            @BindView(R.id.iv_header)ImageView iv_header;
            @BindView(R.id.tv_title)TextView tv_title;
            @BindView(R.id.tv_name)TextView tv_name;
            @BindView(R.id.tv_live_count)TextView tv_live_count;

            public ContentViewHodler(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }


    }
}
