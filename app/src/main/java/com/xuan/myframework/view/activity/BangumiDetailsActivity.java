package com.xuan.myframework.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.view.CommonHeader;
import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.base.adapter.BaseRecyclerViewAdapter;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.NumberUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.utils.SystemStatusBarUtil;
import com.xuan.myframework.view.modle.response.BangumiCommentsModle;
import com.xuan.myframework.view.modle.response.BangumiDetailModle;
import com.xuan.myframework.view.modle.response.BangumiDetailsRecommendedModle;
import com.xuan.myframework.view.presenterImpl.BangumiDetailPresenterImpl;
import com.xuan.myframework.view.view.BangumiDetailView;
import com.xuan.myframework.widget.CircleProgressDialog;
import com.xuan.myframework.widget.EmptyView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by xuan on 2017/6/12.
 */

public class BangumiDetailsActivity extends RxBaseActivity<BangumiDetailPresenterImpl> implements BangumiDetailView {
    public static final String SEASON_ID="seasonId";

    @BindView(R.id.nested_scroll_view)android.support.v4.widget.NestedScrollView scroll_view;
    @BindView(R.id.circle_progress)CircleProgressDialog circle_progress;
    @BindView(R.id.fl_content)FrameLayout fl_content;
    @BindView(R.id.toolbar)Toolbar toolbar;

    @BindView(R.id.iv_bg)ImageView iv_bg;
    @BindView(R.id.iv_pic)ImageView iv_pic;

    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.tv_update)TextView tv_update;
    @BindView(R.id.tv_play)TextView tv_play;

    @BindView(R.id.ll_share)LinearLayout ll_share;
    @BindView(R.id.ll_follow)LinearLayout ll_follow;
    @BindView(R.id.ll_download)LinearLayout ll_download;

    @BindView(R.id.tv_update_number)TextView tv_update_number;
    @BindView(R.id.tv_desc)TextView tv_desc;

    @BindView(R.id.flow_layout)TagFlowLayout flow_layout;

    @BindView(R.id.rv_seasons)
    RecyclerView rv_seasons;
    @BindView(R.id.rv_select_number)
    RecyclerView rv_select_number;
    @BindView(R.id.rv_recommended)
    RecyclerView rv_recommended;
    @BindView(R.id.rv_comment)
    LRecyclerView rv_comment;
    @BindView(R.id.empty_view)
    EmptyView empty_view;

    private SeasonsRecyclerViewAdapter seasonsAdapter;
    private SelectNumberRecyclerViewAdapter selectNumberAdapter;
    private RecommendedRecyclerViewAdapter recommendedAdapter;

    private CommentRecyclerViewAdapter commentAdapter;
    private LRecyclerViewAdapter lAdapter;

    public static void launchActivity(Activity activity, int seasonId) {
        Intent intent = new Intent(activity, BangumiDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SEASON_ID,seasonId);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bangumi_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initEmptyView();
        initBangumiDetailsRecycler();
        initBangumiDetailsRecommendedRecycler();
        initBangumicommentsRecycler();

        mPresenter=new BangumiDetailPresenterImpl(this);
        mPresenter.queryBangumiDetailsData(this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {
        toolbar.setTitle("番剧详情");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置Toolbar的透明度
        toolbar.setBackgroundColor(Color.argb(0, 251, 114, 153));
       //设置StatusBar透明
        SystemStatusBarUtil.immersiveStatusBar(this);
        SystemStatusBarUtil.setHeightAndPadding(this, toolbar);

        //获取顶部image高度和toolbar高度
        final float imageHeight = 250;
        final float toolBarHeight = 48;

        scroll_view.setNestedScrollingEnabled(true);
        scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //根据NestedScrollView滑动改变Toolbar的透明度
                float offsetY = toolBarHeight - imageHeight;
                //计算滑动距离的偏移量
                float offset = 1 - Math.max((offsetY - scrollY) / offsetY, 0f);
                float absOffset = Math.abs(offset);
                //如果滑动距离大于1就设置完全不透明度
                if (absOffset >= 1) {
                    absOffset = 1;
                }
                toolbar.setBackgroundColor(Color.argb((int) (absOffset * 255), 251, 114, 153));
            }
        });

    }

    @Override
    public void loadDataSuccess(BangumiCommentsModle data) {
        //不使用
    }

    @Override
    public void showProgress() {
        circle_progress.setVisibility(View.VISIBLE);
        circle_progress.spin();

    }

    @Override
    public void hideProgress() {
        circle_progress.setVisibility(View.GONE);
        circle_progress.stopSpinning();

    }

    @Override
    public void showMsg(String message) {
        SnackbarUtils.showShortSnackbar(scroll_view,"数据加载失败,请重新加载或者检查网络是否链接");
    }

    @Override
    public void showEmptyView() {
        fl_content.setVisibility(View.GONE);
        empty_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        fl_content.setVisibility(View.VISIBLE);
        empty_view.setVisibility(View.GONE);
    }
    @Override
    public void initEmptyView() {
        empty_view.setVisibility(View.GONE);
        empty_view.setEmptyImage(R.drawable.load_error);
        empty_view.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");

        fl_content.setVisibility(View.GONE);
    }

    @Override
    public void initBangumiDetailsRecycler() {
        if (seasonsAdapter == null) {
            seasonsAdapter = new SeasonsRecyclerViewAdapter();

            LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            rv_seasons.setLayoutManager(layoutManager);

            rv_seasons.setAdapter(seasonsAdapter);

            rv_seasons.setNestedScrollingEnabled(true);
            rv_seasons.setHasFixedSize(true);
        }

        if (selectNumberAdapter == null) {
            selectNumberAdapter = new SelectNumberRecyclerViewAdapter();

            LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
            rv_select_number.setLayoutManager(layoutManager);

            rv_select_number.setAdapter(selectNumberAdapter);

            rv_select_number.setNestedScrollingEnabled(true);
            rv_select_number.setHasFixedSize(true);
        }
    }

    public class SeasonsRecyclerViewAdapter extends BaseRecyclerViewAdapter<SeasonsViewHolder> {
        private int seasonsOnClickPosition;

        @Override
        public void onBindItemViewHolder(SeasonsViewHolder holder, int position) {
            BangumiDetailModle.ResultBean.SeasonsBean data= (BangumiDetailModle.ResultBean.SeasonsBean) itemList.get(position);
            holder.tv_seasons.setText(data.title);

            if(position==seasonsOnClickPosition){
                holder.card_view.setBackground(
                        android.support.v4.content.ContextCompat.getDrawable(BangumiDetailsActivity.this,R.color.base_pink));
                holder.tv_seasons.setTextColor(
                        android.support.v4.content.ContextCompat.getColor(BangumiDetailsActivity.this,R.color.base_white));
            }else{
                holder.card_view.setBackground(
                        android.support.v4.content.ContextCompat.getDrawable(BangumiDetailsActivity.this,R.color.base_white));
                holder.tv_seasons.setTextColor(
                        android.support.v4.content.ContextCompat.getColor(BangumiDetailsActivity.this,R.color.light_black_text));
            }

        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_bangumi_details_seasons;
        }

        @Override
        public SeasonsViewHolder onCreateViewHolder(View view) {
            return new SeasonsViewHolder(view);
        }

        public void notifyItemForeground(int i) {
            seasonsOnClickPosition=i;
        }
    }

    public class SeasonsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.card_view)CardView card_view;
        @BindView(R.id.tv_seasons)TextView tv_seasons;

        public SeasonsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    public class SelectNumberRecyclerViewAdapter extends BaseRecyclerViewAdapter<SelectNumberViewHolder> {
        private int selectNumberOnClickPosition;

        @Override
        public void onBindItemViewHolder(SelectNumberViewHolder holder, int position) {
            final BangumiDetailModle.ResultBean.EpisodesBean data= (BangumiDetailModle.ResultBean.EpisodesBean) itemList.get(position);

            holder.tv_index.setText("第 " + data.index + " 话");
            holder.tv_title.setText(data.index_title);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoDetailsActivity.launchActivity(BangumiDetailsActivity.this,
                            Integer.parseInt(data.av_id),data.cover);
                }
            });

            if(position==selectNumberOnClickPosition){
                holder.card_view.setBackground(
                        android.support.v4.content.ContextCompat.getDrawable(BangumiDetailsActivity.this,R.color.base_pink));
                holder.tv_index.setTextColor(
                        android.support.v4.content.ContextCompat.getColor(BangumiDetailsActivity.this,R.color.base_white));
                holder.tv_title.setTextColor(
                        android.support.v4.content.ContextCompat.getColor(BangumiDetailsActivity.this,R.color.base_white));
            }else{
                holder.card_view.setBackground(
                        android.support.v4.content.ContextCompat.getDrawable(BangumiDetailsActivity.this,R.color.base_white));
                holder.tv_index.setTextColor(
                        android.support.v4.content.ContextCompat.getColor(BangumiDetailsActivity.this,R.color.dark_black_text));
                holder.tv_title.setTextColor(
                        android.support.v4.content.ContextCompat.getColor(BangumiDetailsActivity.this,R.color.light_black_text));
            }

        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_bangumi_details_select_number;
        }

        @Override
        public SelectNumberViewHolder onCreateViewHolder(View view) {
            return new SelectNumberViewHolder(view);
        }

        public void notifyItemForeground(int i) {
            selectNumberOnClickPosition=i;
        }
    }


    public class SelectNumberViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.card_view)CardView card_view;
        @BindView(R.id.tv_index)TextView tv_index;
        @BindView(R.id.tv_title)TextView tv_title;

        public SelectNumberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    @Override
    public void initBangumiDetailsRecommendedRecycler() {
        if (recommendedAdapter == null) {
            recommendedAdapter = new RecommendedRecyclerViewAdapter();

            GridLayoutManager layoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
            rv_recommended.setLayoutManager(layoutManager);

            rv_recommended.setAdapter(recommendedAdapter);

            rv_recommended.setNestedScrollingEnabled(true);
            rv_recommended.setHasFixedSize(true);
        }
    }

    public class RecommendedRecyclerViewAdapter extends BaseRecyclerViewAdapter<RecommendedViewHolder> {
        @Override
        public void onBindItemViewHolder(RecommendedViewHolder holder, int position) {
            BangumiDetailsRecommendedModle.ResultBean.ListBean data= (BangumiDetailsRecommendedModle.ResultBean.ListBean)
                    itemList.get(position);

            GlideUtils.loadDefaultImage(BangumiDetailsActivity.this,data.cover,holder.iv_pic);

            holder.tv_title.setText(data.title);

            holder.tv_follow.setText(NumberUtils.convertThousandsString(Integer.valueOf(data.follow))+"人追番");
        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_bangumi_details_recommended;
        }

        @Override
        public RecommendedViewHolder onCreateViewHolder(View view) {
            return new RecommendedViewHolder(view);
        }

    }


    public class RecommendedViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_pic)ImageView iv_pic;
        @BindView(R.id.tv_follow)TextView tv_follow;
        @BindView(R.id.tv_title)TextView tv_title;

        public RecommendedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    @Override
    public void initBangumicommentsRecycler() {
        commentAdapter=new CommentRecyclerViewAdapter();
        lAdapter=new LRecyclerViewAdapter(commentAdapter);
        rv_comment.setAdapter(lAdapter);

        LinearLayoutManager gridLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_comment.setLayoutManager(gridLayoutManager);

        rv_comment.setNestedScrollingEnabled(false);
        rv_comment.setHasFixedSize(false);
        rv_comment.setPullRefreshEnabled(false);
        rv_comment.setLoadMoreEnabled(false);

//        rv_comment.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        rv_comment.setArrowImageView(R.drawable.circle_refresh);
//        rv_comment.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//
//        //设置头部加载颜色
//        rv_comment.setHeaderViewColor(R.color.base_pink, R.color.base_pink ,android.R.color.white);
//        //设置底部加载颜色
//        rv_comment.setFooterViewColor(R.color.base_pink, R.color.base_pink ,android.R.color.white);
//        //设置底部加载文字提示
//        rv_comment.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.DividerDecoration_height)
                .setColorResource(R.color.base_white_line)
                .build();
        rv_comment.addItemDecoration(divider);
    }

    public class CommentRecyclerViewAdapter extends BaseRecyclerViewAdapter<CommentRecyclerViewAdapter.CommentViewHolder>{
        private int[] levelIcons={R.drawable.level_0,R.drawable.level_1,R.drawable.level_2,R.drawable.level_3,
                R.drawable.level_4,R.drawable.level_5,R.drawable.level_6};

        @Override
        public void onBindItemViewHolder(CommentViewHolder holder, int position) {
            if(itemList.get(position) instanceof BangumiCommentsModle.HotsBean){
                BangumiCommentsModle.HotsBean data= (BangumiCommentsModle.HotsBean) itemList.get(position);

                GlideUtils.loadCircleImage(BangumiDetailsActivity.this,data.member.avatar,holder.iv_header);
                if(data.member.level_info.current_level>=0 && data.member.level_info.current_level<=levelIcons.length-1){
                    holder.iv_level.setImageResource(levelIcons[data.member.level_info.current_level]);
                }

                holder.tv_user_name.setText(data.member.uname);
                if("男".equals(data.member.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_boy);
                }else if("女".equals(data.member.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_girl);
                }else{
                    holder.iv_user_sex.setImageResource(R.drawable.sex_double);
                }
                holder.tv_user_floor.setText("#"+data.floor);
                holder.tv_user_time.setText(NumberUtils.longFormatString(data.ctime,NumberUtils.simplePattern));

                holder.tv_feedback_count.setText(data.count+"");
                holder.tv_up_count.setText(data.like+"");

                holder.tv_desc.setText(data.content.message);
            }else if(itemList.get(position) instanceof BangumiCommentsModle.RepliesBean){
                BangumiCommentsModle.RepliesBean data= (BangumiCommentsModle.RepliesBean) itemList.get(position);

                GlideUtils.loadCircleImage(BangumiDetailsActivity.this,data.member.avatar,holder.iv_header);
                if(data.member.level_info.current_level>=0 && data.member.level_info.current_level<=levelIcons.length-1){
                    holder.iv_level.setImageResource(levelIcons[data.member.level_info.current_level]);
                }

                holder.tv_user_name.setText(data.member.uname);
                if("男".equals(data.member.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_boy);
                }else if("女".equals(data.member.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_girl);
                }else{
                    holder.iv_user_sex.setImageResource(R.drawable.sex_double);
                }
                holder.tv_user_floor.setText("#"+data.floor);
                holder.tv_user_time.setText(NumberUtils.longFormatString(data.ctime,NumberUtils.simplePattern));

                holder.tv_feedback_count.setText(data.count+"");
                holder.tv_up_count.setText(data.like+"");

                holder.tv_desc.setText(data.content.message);

            }

        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_comment;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(View view) {
            return new CommentViewHolder(view);
        }

        class CommentViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.iv_header)ImageView iv_header;
            @BindView(R.id.iv_level)ImageView iv_level;

            @BindView(R.id.tv_user_name)TextView tv_user_name;
            @BindView(R.id.iv_user_sex)ImageView iv_user_sex;
            @BindView(R.id.tv_user_floor)TextView tv_user_floor;
            @BindView(R.id.tv_user_time)TextView tv_user_time;

            @BindView(R.id.tv_feedback_count)TextView tv_feedback_count;
            @BindView(R.id.tv_up_count)TextView tv_up_count;

            @BindView(R.id.tv_desc)TextView tv_desc;

            public CommentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    @Override
    public void loadBangumiDetailsSuccess(BangumiDetailModle data) {
        GlideUtils.loadDefaultImage(this,data.result.cover,iv_pic);
        RequestOptions options=new RequestOptions()
                .centerCrop()
                .error(GlideUtils.error)
                .fallback(GlideUtils.fallback)
                .priority(Priority.HIGH)
                .transform(new BlurTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        GlideUtils.loadDefaultImage(this,data.result.cover,iv_bg,options);

        tv_title.setText(data.result.title);
        //设置番剧更新状态
        if (data.result.is_finish.equals("0")) {
            tv_update.setText("更新至第" + data.result.newest_ep_index + "话");
            tv_update_number.setText("连载中");
        } else {
            tv_update.setText(data.result.newest_ep_index + "话全");
            tv_update_number.setText("已完结" + data.result.newest_ep_index + "话全");
        }

        //设置番剧播放和追番数量
        tv_play.setText("播放：" + NumberUtils.convertThousandsString(Integer.valueOf(data.result.play_count))
                + "  " + "追番：" + NumberUtils.convertThousandsString(Integer.valueOf(data.result.favorites)));

        //设置番剧简介
        tv_desc.setText(data.result.evaluate);

        //设置标签布局
        List<BangumiDetailModle.ResultBean.TagsBean> tags = data.result.tags;
        flow_layout.setAdapter(new TagAdapter<BangumiDetailModle.ResultBean.TagsBean>(tags) {

            @Override
            public View getView(FlowLayout parent, int position, BangumiDetailModle.ResultBean.TagsBean data) {

                TextView tv_tag = (TextView) LayoutInflater.from(BangumiDetailsActivity.this)
                        .inflate(R.layout.item_tag_flow_layout, parent, false);
                tv_tag.setText(data.tag_name);

                return tv_tag;
            }
        });

        //设置recycleview
        seasonsAdapter.addItem(data.result.seasons);
        for (int i = 0, size = data.result.seasons.size(); i < size; i++) {
            if (data.result.seasons.get(i).season_id.equals(data.result.season_id)) {
                seasonsAdapter.notifyItemForeground(i);
            }
        }

        selectNumberAdapter.addItem(data.result.episodes);
        selectNumberAdapter.notifyItemForeground(data.result.episodes.size() - 1);
        rv_select_number.scrollToPosition(data.result.episodes.size() - 1);
    }

    @Override
    public void loadBangumiDeatilsRecommendedSuccess(BangumiDetailsRecommendedModle data) {
        recommendedAdapter.addItem(data.result.list);
    }

    @Override
    public void loadBangumicommentsSuccess(BangumiCommentsModle data) {
        //添加评论头部
        if(data.data.hots!=null && data.data.hots.size()>0)
        addCommentHeader(data.data.hots);
        //添加尾部
        commentAdapter.addItem(data.data.replies);
//        //设置评论数量
//        tv.setText("评论 第1话(" + mPageInfo.getAcount() + ")");

    }

    private void addCommentHeader(List<BangumiCommentsModle.HotsBean> data) {
        lAdapter.removeHeaderView();

        CommonHeader view=new CommonHeader(this,R.layout.item_comment_header);

        RecyclerView headRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayout ll_more_hot_comment= (LinearLayout) view.findViewById(R.id.ll_more_hot_comment);

        CommentRecyclerViewAdapter headAdapter=new CommentRecyclerViewAdapter();
        headRecyclerView.setAdapter(headAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        headRecyclerView.setLayoutManager(layoutManager);

        headRecyclerView.setNestedScrollingEnabled(false);
        headRecyclerView.setHasFixedSize(false);

        headAdapter.addItem(data);

        lAdapter.addHeaderView(view);
    }
}
