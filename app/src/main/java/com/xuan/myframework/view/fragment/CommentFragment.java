package com.xuan.myframework.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.view.CommonHeader;
import com.xuan.myframework.R;
import com.xuan.myframework.base.adapter.BaseRecyclerViewAdapter;
import com.xuan.myframework.base.fragment.RxBaseFragment;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.activity.VideoDetailsActivity;
import com.xuan.myframework.view.modle.response.CommentModle;
import com.xuan.myframework.view.presenterImpl.CommentPresenterImpl;
import com.xuan.myframework.view.view.CommentView;
import com.xuan.myframework.widget.EmptyView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.xuan.myframework.view.fragment
 * Created by xuan on 2017/7/6.
 * version
 * desc
 */

public class CommentFragment extends RxBaseFragment<CommentPresenterImpl> implements CommentView {

    @BindView(R.id.recycler_view)LRecyclerView recycler_view;
    @BindView(R.id.empty_view)EmptyView emptyView;
    private MyRecyclerViewAdapter adapter;
    private LRecyclerViewAdapter lAdapter;

    private CommentPresenterImpl presenter;
    private int aid;
    private int pageNum;
    private int pageSize;
    private int ver;
    private int loadState=0;
    public static final int onRefresh=0;
    public static final int onLoadMore=1;
    private boolean isMoreData=true;

    public static CommentFragment newInstance(Bundle args){
        CommentFragment fragment=new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    public void initView(View view) {
        if(getArguments()!=null){
            aid=getArguments().getInt(VideoDetailsActivity.key_av_id);
        }
        ver=3;
        pageNum=1;
        pageSize=20;

        presenter = new CommentPresenterImpl(this);
    }

    @Override
    public void initData() {
        presenter.queryCommentData(aid,pageNum,pageSize,ver,this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void loadDataSuccess(CommentModle data) {
        if(data.list==null || data.list.size()==0){
            isMoreData=false;

        }else{
            isMoreData=true;

            if(loadState==onRefresh){
                adapter.clear();

                if(data.hotList!=null && data.hotList.size()>0)
                addHeaderView(data.hotList);

            }else if(loadState==onLoadMore){

            }

            adapter.addItem(data.list);
        }


        recycler_view.refreshComplete(data.page);
    }

    private void addHeaderView(ArrayList<CommentModle.HotComment> hotList) {
        lAdapter.removeHeaderView();

        CommonHeader view=new CommonHeader(getActivity(),R.layout.item_comment_header);

        RecyclerView headRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayout ll_more_hot_comment= (LinearLayout) view.findViewById(R.id.ll_more_hot_comment);

        MyRecyclerViewAdapter headAdapter=new MyRecyclerViewAdapter();
        headRecyclerView.setAdapter(headAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        headRecyclerView.setLayoutManager(layoutManager);

        headRecyclerView.setNestedScrollingEnabled(false);
        headRecyclerView.setHasFixedSize(false);

        headAdapter.addItem(hotList);

        lAdapter.addHeaderView(view);
    }

    @Override
    public void showProgress() {
        recycler_view.forceToRefresh();
    }

    @Override
    public void hideProgress() {
        recycler_view.refreshComplete(pageSize);
    }

    @Override
    public void showMsg(String message) {
        SnackbarUtils.showShortSnackbar(this.getView(),"数据加载失败,请重新加载或者检查网络是否链接");
    }

    @Override
    public void initRecyclerView() {
        adapter=new MyRecyclerViewAdapter();
        lAdapter=new LRecyclerViewAdapter(adapter);
        recycler_view.setAdapter(lAdapter);

        LinearLayoutManager gridLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(gridLayoutManager);

        recycler_view.setNestedScrollingEnabled(true);
        recycler_view.setHasFixedSize(true);
        recycler_view.setPullRefreshEnabled(false);
        recycler_view.setLoadMoreEnabled(true);

        recycler_view.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recycler_view.setArrowImageView(R.drawable.circle_refresh);
        recycler_view.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        //设置头部加载颜色
        recycler_view.setHeaderViewColor(R.color.base_pink, R.color.base_pink ,android.R.color.white);
        //设置底部加载颜色
        recycler_view.setFooterViewColor(R.color.base_pink, R.color.base_pink ,android.R.color.white);
        //设置底部加载文字提示
        recycler_view.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");

        DividerDecoration divider = new DividerDecoration.Builder(getContext())
                .setHeight(R.dimen.DividerDecoration_height)
                .setColorResource(R.color.base_white_line)
                .build();
        recycler_view.addItemDecoration(divider);

        recycler_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                lAdapter.notifyDataSetChanged();

                loadState=onRefresh;
                pageNum=1;
                presenter.queryCommentData(aid,pageNum,pageSize,ver,CommentFragment.this.bindToLifecycle());
            }
        });
        recycler_view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (isMoreData) {//有更多数据
                    loadState=onLoadMore;
                    pageNum++;
                    presenter.queryCommentData(aid,pageNum,pageSize,ver,CommentFragment.this.bindToLifecycle());
                } else {
                    //the end
                    recycler_view.setNoMore(true);
                }
            }
        });

    }

    @Override
    public void initEmptyView() {
        emptyView.setVisibility(View.GONE);
        emptyView.setEmptyImage(R.drawable.load_error);
        emptyView.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
    }

    @Override
    public void showEmptyView() {
        recycler_view.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        recycler_view.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyRecyclerViewAdapter.MyViewHolder>{
        private int[] levelIcons={R.drawable.level_0,R.drawable.level_1,R.drawable.level_2,R.drawable.level_3,
                R.drawable.level_4,R.drawable.level_5,R.drawable.level_6};

        @Override
        public void onBindItemViewHolder(MyViewHolder holder, int position) {
            if(itemList.get(position) instanceof CommentModle.HotComment){
                CommentModle.HotComment data= (CommentModle.HotComment) itemList.get(position);

                GlideUtils.loadCircleImage(getActivity(),data.face,holder.iv_header);
                if(data.level_info.current_level>=0 && data.level_info.current_level<=levelIcons.length-1){
                    holder.iv_level.setImageResource(levelIcons[data.level_info.current_level]);
                }

                holder.tv_user_name.setText(data.nick);
                if("男".equals(data.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_boy);
                }else if("女".equals(data.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_girl);
                }else{
                    holder.iv_user_sex.setImageResource(R.drawable.sex_double);
                }
                holder.tv_user_floor.setText("#"+data.lv);
                holder.tv_user_time.setText(data.create_at);

                holder.tv_feedback_count.setText(data.reply_count+"");
                holder.tv_up_count.setText(data.good+"");

                holder.tv_desc.setText(data.msg);
            }else if(itemList.get(position) instanceof CommentModle.NormalComment){
                CommentModle.NormalComment data= (CommentModle.NormalComment) itemList.get(position);

                GlideUtils.loadCircleImage(getActivity(),data.face,holder.iv_header);
                if(data.level_info.current_level>=0 && data.level_info.current_level<=levelIcons.length-1){
                    holder.iv_level.setImageResource(levelIcons[data.level_info.current_level]);
                }

                holder.tv_user_name.setText(data.nick);
                if("男".equals(data.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_boy);
                }else if("女".equals(data.sex)){
                    holder.iv_user_sex.setImageResource(R.drawable.sex_girl);
                }else{
                    holder.iv_user_sex.setImageResource(R.drawable.sex_double);
                }
                holder.tv_user_floor.setText("#"+data.lv);
                holder.tv_user_time.setText(data.create_at);

                holder.tv_feedback_count.setText(data.reply_count+"");
                holder.tv_up_count.setText(data.good+"");

                holder.tv_desc.setText(data.msg);

            }

        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_comment;
        }

        @Override
        public MyViewHolder onCreateViewHolder(View view) {
            return new MyViewHolder(view);
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.iv_header)ImageView iv_header;
            @BindView(R.id.iv_level)ImageView iv_level;

            @BindView(R.id.tv_user_name)TextView tv_user_name;
            @BindView(R.id.iv_user_sex)ImageView iv_user_sex;
            @BindView(R.id.tv_user_floor)TextView tv_user_floor;
            @BindView(R.id.tv_user_time)TextView tv_user_time;

            @BindView(R.id.tv_feedback_count)TextView tv_feedback_count;
            @BindView(R.id.tv_up_count)TextView tv_up_count;

            @BindView(R.id.tv_desc)TextView tv_desc;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
