package com.xuan.myframework.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.xuan.myframework.R;
import com.xuan.myframework.base.fragment.RxBaseFragment;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.DisplayUtils;
import com.xuan.myframework.utils.NumberUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.activity.BangumiDetailsActivity;
import com.xuan.myframework.view.activity.BangumiIndexActivity;
import com.xuan.myframework.view.activity.BroadcastTableActivity;
import com.xuan.myframework.view.activity.ChaseBangumiActivity;
import com.xuan.myframework.view.activity.WebViewActivity;
import com.xuan.myframework.view.modle.response.BangumiModle;
import com.xuan.myframework.view.modle.response.BangumiRecommendedModle;
import com.xuan.myframework.view.presenterImpl.BangumiPresenterImpl;
import com.xuan.myframework.view.view.BangumiView;
import com.xuan.myframework.widget.ConvenientBanner;
import com.xuan.myframework.widget.EmptyView;
import com.xuan.myframework.widget.MySwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuan on 2017/7/26.
 * 番剧
 */
public class BangumiFragment extends RxBaseFragment<BangumiPresenterImpl> implements BangumiView {
    @BindView(R.id.refresh_layout)MySwipeRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)LRecyclerView recycler_view;
    @BindView(R.id.empty_view)EmptyView empty_view;

    MyRecyclerViewAdapter adapter;
    LRecyclerViewAdapter lAdapter;

    private List<Visitable> visitableList;

    private int[] entranceIcons = new int[] {
            R.drawable.ic_bangumi_following,
            R.drawable.ic_bangumi_calendar_7,
            R.drawable.ic_bangumi_category,
    };
    private int[] entranceBgs = new int[]{
            R.drawable.bg_round_solid_yellow_btn,
            R.drawable.bg_round_solid_pink_btn,
            R.drawable.bg_round_solid_blue_btn
    };
    private String[] entranceTexts=new String[]{
            "追番","放映","索引"
    };

    private String[] moonTexts=new String[]{
            "1月新番","4月新番","7月新番","10月新番"
    };
    private int[] moonIcons=new int[]{
            R.drawable.spring,
            R.drawable.summer,
            R.drawable.autumn,
            R.drawable.winter
    };

    private View.OnClickListener chasebangumiListener;
    private View.OnClickListener projectionListener;
    private View.OnClickListener indexListener;

    public static BangumiFragment newInstance(Bundle args) {
        BangumiFragment fragment = new BangumiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bangumi;
    }

    @Override
    public void initView(View view) {
        mPresenter =new BangumiPresenterImpl(this);

        initEmptyView();
        initRecycler();
    }

    @Override
    public void initData() {
        mPresenter.queryBangumiData(this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {
    }

    @Override
    public void loadDataSuccess(List<Visitable> data) {
        this.visitableList=data;

        adapter.refreshData(visitableList);
        adapter.notifyDataSetChanged();
        recycler_view.scrollToPosition(0);
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
    public void initEmptyView() {
        empty_view.setVisibility(View.GONE);
        empty_view.setEmptyImage(R.drawable.load_error);
        empty_view.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
    }

    @Override
    public void addBannerHeader(final List<BangumiModle.ResultBean.AdBean.HeadBean> data) {
        lAdapter.removeHeaderView();
        lAdapter.removeHeaderView();
        //添加广告头部UI
        View view= LayoutInflater.from(this.getContext()).inflate(R.layout.item_bangumi_banner,null);
        lAdapter.addHeaderView(view);

        ConvenientBanner convenient_banner= (ConvenientBanner) view.findViewById(R.id.convenient_banner);
        //添加广告头部UI
        convenient_banner.setPages(new CBViewHolderCreator<ImageViewHolder>() {
            @Override
            public ImageViewHolder createHolder() {
                return new ImageViewHolder();
            }
        },data)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.point_normal, R.drawable.point_focus}
                        , DisplayUtils.dp2px(getContext(),5),0,0,DisplayUtils.dp2px(getContext(),5)
                        ,DisplayUtils.dp2px(getContext(),5),DisplayUtils.dp2px(getContext(),5))
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        WebViewActivity.launchActivity(getActivity(),data.get(position).img,
                                data.get(position).title);
                    }
                });
    }

    private class ImageViewHolder implements Holder<BangumiModle.ResultBean.AdBean.HeadBean> {
        ImageView view;

        @Override
        public View createView(Context context) {
            view=new ImageView(context);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, BangumiModle.ResultBean.AdBean.HeadBean data) {
            GlideUtils.loadDefaultImage(context,data.img,view);
        }

    }

    @Override
    public void addButtonHeader() {
        //添加入口头部UI
        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        chasebangumiListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChaseBangumiActivity.launchActivity(BangumiFragment.this.getActivity(),-1);
            }
        };
        projectionListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BroadcastTableActivity.launchActivity(BangumiFragment.this.getActivity());
            }
        };
        indexListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BangumiIndexActivity.launchActivity(BangumiFragment.this.getActivity());
            }
        };

        View.OnClickListener[] listeners=new View.OnClickListener[]{
                chasebangumiListener,projectionListener,indexListener
        };


        for (int i = 0; i < entranceIcons.length; i++) {
            TextView entranceView= (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_bangumi_entrance, linearLayout,false);

            Drawable drawable =android.support.v4.content.ContextCompat.getDrawable(getActivity(),entranceIcons[i]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            entranceView.setCompoundDrawables(drawable,null,null,null);
            entranceView.setCompoundDrawablePadding(5);
            entranceView.setBackgroundResource(entranceBgs[i]);
            entranceView.setText(entranceTexts[i]);

            entranceView.setOnClickListener(listeners[i]);

            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) entranceView.getLayoutParams();
            params.weight=1;
            params.width=0;
            entranceView.setLayoutParams(params);

            linearLayout.addView(entranceView);
        }
        lAdapter.addHeaderView(linearLayout);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void showShortSnackbar(String content) {
        SnackbarUtils.showShortSnackbar(getView(),content);
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

    @Override
    public void initRecycler() {
        //设置adapter
        if(adapter==null){
            adapter=new MyRecyclerViewAdapter(new ItemTypeFactory());
            lAdapter=new LRecyclerViewAdapter(adapter);

            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(), 3,GridLayoutManager.VERTICAL,false);
            recycler_view.setLayoutManager(gridLayoutManager);

            recycler_view.setAdapter(lAdapter);

            recycler_view.setNestedScrollingEnabled(true);
            recycler_view.setHasFixedSize(true);
            recycler_view.setPullRefreshEnabled(false);
            recycler_view.setLoadMoreEnabled(false);

            lAdapter.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    if(visitableList!=null && visitableList.get(position)!=null){
                        Object item = visitableList.get(position);
                        if (item instanceof BangumiModle.NewBangumiBodyTypeModle ||
                                item instanceof BangumiModle.MonthNewBangumiBodyTypeModle){
                            return 1;
                        }else if(item instanceof BangumiModle.IconHeadTypeModle || item instanceof BangumiModle.MoonHeadTypeModle ||
                                item instanceof BangumiModle.ImageHeadTypeModle || item instanceof BangumiRecommendedModle.BangumiRecommendBodyTypeModle){
                            return 3;
                        }else{
                            return gridLayoutManager.getSpanCount();
                        }
                    }else{
                        return gridLayoutManager.getSpanCount();
                    }
                }

            });

//            DividerDecoration divider = new DividerDecoration.Builder(getContext())
//                    .setHeight(R.dimen.DividerDecoration_height)
//                    .setColorResource(R.color.base_white_line)
//                    .build();
//            recycler_view.addItemDecoration(divider);

            refresh_layout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPresenter.queryBangumiData(BangumiFragment.this.bindToLifecycle());
                }
            });

        }
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<BangumiFragment.MyViewHolder> {
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

    public abstract class MyViewHolder<T extends Visitable> extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(T t);
    }

    public interface TypeFactory {

        int type(BangumiModle.IconHeadTypeModle head);
        int type(BangumiModle.MoonHeadTypeModle head);
        int type(BangumiModle.ImageHeadTypeModle head);
        int type(BangumiModle.NewBangumiBodyTypeModle body);
        int type(BangumiModle.MonthNewBangumiBodyTypeModle body);
        int type(BangumiRecommendedModle.BangumiRecommendBodyTypeModle body);

        MyViewHolder onCreateViewHolder(View itemView, int viewType);

    }

    public class ItemTypeFactory implements TypeFactory{

        @Override
        public int type(BangumiModle.IconHeadTypeModle head) {
            return R.layout.item_icon_arrow_head;
        }

        @Override
        public int type(BangumiModle.MoonHeadTypeModle head) {
            return R.layout.item_icon_moon_head;
        }

        @Override
        public int type(BangumiModle.ImageHeadTypeModle head) {
            return R.layout.item_image_head;
        }

        @Override
        public int type(BangumiModle.NewBangumiBodyTypeModle body) {
            return R.layout.item_new_bangumi_body;
        }

        @Override
        public int type(BangumiModle.MonthNewBangumiBodyTypeModle body) {
            return R.layout.item_moon_bangumi_body;
        }

        @Override
        public int type(BangumiRecommendedModle.BangumiRecommendBodyTypeModle body) {
            return R.layout.item_bangumi_recommend;
        }

        @Override
        public MyViewHolder onCreateViewHolder(View itemView, int viewType) {
            MyViewHolder viewHolder=null;
            switch (viewType){
                case R.layout.item_icon_arrow_head:
                    viewHolder=new IconHeadViewHolder(itemView);
                    break;
                case R.layout.item_icon_moon_head:
                    viewHolder=new MoonHeadViewHolder(itemView);
                    break;
                case R.layout.item_image_head:
                    viewHolder=new ImageHeadViewHolder(itemView);
                    break;

                case R.layout.item_new_bangumi_body:
                    viewHolder=new NewBangumiBodyViewHolder(itemView);
                    break;
                case R.layout.item_moon_bangumi_body:
                    viewHolder=new MonthNewBangumiBodyViewHolder(itemView);
                    break;
                case R.layout.item_bangumi_recommend:
                    viewHolder=new BangumiRecommendBodyViewHolder(itemView);
                    break;

                default:
                    break;
            }
            return viewHolder;
        }
    }

    class IconHeadViewHolder extends MyViewHolder{
        @BindView(R.id.iv_icon)ImageView iv_icon;
        @BindView(R.id.tv_left)TextView tv_left;
        @BindView(R.id.tv_right)TextView tv_right;

        public IconHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BangumiModle.IconHeadTypeModle modle= (BangumiModle.IconHeadTypeModle) visitable;
            BangumiModle.IconHeadModle data=modle.data;

            iv_icon.setImageResource(data.iconResId);
            tv_left.setText(data.leftText);
            tv_right.setText(data.rightText);

            itemView.setOnClickListener(data.listener);
        }
    }

    class MoonHeadViewHolder extends MyViewHolder{
        @BindView(R.id.iv_icon)ImageView iv_icon;
        @BindView(R.id.tv_left)TextView tv_left;
        @BindView(R.id.tv_right)TextView tv_right;

        public MoonHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BangumiModle.MoonHeadTypeModle modle= (BangumiModle.MoonHeadTypeModle) visitable;
            BangumiModle.ResultBean.PreviousBean data=modle.data;

            if(data.season>0 && data.season<=4){
                iv_icon.setImageResource(moonIcons[data.season-1]);
                tv_left.setText(moonTexts[data.season-1]);
            }

            tv_right.setText("分季列表");



        }
    }

    class ImageHeadViewHolder extends MyViewHolder{
        @BindView(R.id.iv_pic)ImageView iv_pic;

        public ImageHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BangumiModle.ImageHeadTypeModle modle= (BangumiModle.ImageHeadTypeModle) visitable;
            BangumiModle.ResultBean.AdBean.BodyBean data=modle.data;

            GlideUtils.loadDefaultImage(getActivity(),data.img,iv_pic);
        }
    }

    class NewBangumiBodyViewHolder extends MyViewHolder{
        @BindView(R.id.iv_pic)ImageView iv_pic;
        @BindView(R.id.tv_count)TextView tv_count;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_update)TextView tv_update;

        public NewBangumiBodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BangumiModle.NewBangumiBodyTypeModle modle= (BangumiModle.NewBangumiBodyTypeModle) visitable;
            final BangumiModle.ResultBean.SerializingBean data=modle.data;

            GlideUtils.loadDefaultImage(getActivity(),data.cover,iv_pic);
            tv_count.setText(data.watching_count + "人在看");
            tv_title.setText(data.title);
            tv_update.setText("更新至第" + data.newest_ep_index + "话");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BangumiDetailsActivity.launchActivity(getActivity(),data.season_id);
                }
            });
        }
    }

    class MonthNewBangumiBodyViewHolder extends MyViewHolder{
        @BindView(R.id.iv_pic)ImageView iv_pic;
        @BindView(R.id.tv_count)TextView tv_count;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_update)TextView tv_update;

        public MonthNewBangumiBodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BangumiModle.MonthNewBangumiBodyTypeModle modle= (BangumiModle.MonthNewBangumiBodyTypeModle) visitable;
            final BangumiModle.ResultBean.PreviousBean.ListBean data=modle.data;

            GlideUtils.loadDefaultImage(getActivity(),data.cover,iv_pic);
            tv_count.setText(NumberUtils.convertThousandsString(Integer.valueOf(data.favourites)) + "人追番");
            tv_title.setText(data.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BangumiDetailsActivity.launchActivity(getActivity(),data.season_id);
                }
            });
        }
    }

    class BangumiRecommendBodyViewHolder extends MyViewHolder{
        @BindView(R.id.iv_pic)ImageView iv_pic;
        @BindView(R.id.iv_new_text)ImageView iv_new_text;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_desc)TextView tv_desc;

        public BangumiRecommendBodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BangumiRecommendedModle.BangumiRecommendBodyTypeModle modle =
                    (BangumiRecommendedModle.BangumiRecommendBodyTypeModle) visitable;
            final BangumiRecommendedModle.ResultBean data=modle.data;

            GlideUtils.loadDefaultImage(getActivity(),data.cover,iv_pic);
            tv_title.setText(data.title);
            tv_desc.setText(data.desc);

            iv_new_text.setVisibility(data.is_new==1 ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.launchActivity(getActivity(),data.link,data.title);
                }
            });
        }
    }


    public interface Visitable {

        int type(TypeFactory typeFactory);

    }
}
