package com.xuan.myframework.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.base.adapter.BaseRecyclerViewAdapter;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.modle.response.ChaseBangumiModle;
import com.xuan.myframework.view.presenterImpl.ChaseBangumiPresenterImpl;
import com.xuan.myframework.view.view.ChaseBangumiView;
import com.xuan.myframework.widget.EmptyView;
import com.xuan.myframework.widget.MySwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuan on 2017/6/21.
 */

public class ChaseBangumiActivity extends RxBaseActivity<ChaseBangumiPresenterImpl> implements ChaseBangumiView {
    public static final String key_mid="mid";

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.refresh_layout)MySwipeRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)LRecyclerView recycler_view;
    @BindView(R.id.empty_view)EmptyView empty_view;

    MyRecyclerViewAdapter adapter;
    LRecyclerViewAdapter lAdapter;

    private static final int MID = 9467159;

    public static void launchActivity(Activity activity,int mid){
        Intent intent = new Intent(activity, ChaseBangumiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(key_mid, mid);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chase_bangumi;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
        }

        initRecycler();
        initEmptyView();

        mPresenter=new ChaseBangumiPresenterImpl(this);

        mPresenter.queryChaseBangumiData(MID,this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {
        toolbar.setTitle("追番");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void loadDataSuccess(ChaseBangumiModle data) {
        if(data!=null && data.data!=null && data.data.result!=null && data.data.result.size()>0){
            adapter.addItem(data.data.result);
            recycler_view.scrollToPosition(0);
        }
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
            adapter=new MyRecyclerViewAdapter();
            lAdapter=new LRecyclerViewAdapter(adapter);

            LinearLayoutManager layoutManager=new LinearLayoutManager(this,GridLayoutManager.VERTICAL,false);
            recycler_view.setLayoutManager(layoutManager);

            recycler_view.setAdapter(lAdapter);

            recycler_view.setNestedScrollingEnabled(true);
            recycler_view.setHasFixedSize(true);
            recycler_view.setPullRefreshEnabled(false);
            recycler_view.setLoadMoreEnabled(false);

//            DividerDecoration divider = new DividerDecoration.Builder(getContext())
//                    .setHeight(R.dimen.DividerDecoration_height)
//                    .setColorResource(R.color.base_white_line)
//                    .build();
//            recycler_view.addItemDecoration(divider);

            refresh_layout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPresenter.queryChaseBangumiData(MID,ChaseBangumiActivity.this.bindToLifecycle());
                }
            });

        }
    }

    @Override
    public void initEmptyView() {
        empty_view.setVisibility(View.GONE);
        empty_view.setEmptyImage(R.drawable.load_error);
        empty_view.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
    }


    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyRecyclerViewAdapter.MyViewHolder> {

        @Override
        public void onBindItemViewHolder(MyViewHolder holder, int position) {
            ChaseBangumiModle.DataBean.ResultBean data= (ChaseBangumiModle.DataBean.ResultBean) itemList.get(position);

            GlideUtils.loadDefaultImage(ChaseBangumiActivity.this,data.cover,holder.iv_pic);
            holder.tv_title.setText(data.title);

            if (data.is_finish == 1) {
                holder.tv_update.setText(data.total_count + "话全");
                holder.tv_update.setTextColor(android.support.v4.content.ContextCompat
                        .getColor(ChaseBangumiActivity.this,R.color.base_black));
            } else {
                holder.tv_update.setText("更新至第" + data.total_count + "话");
                holder.tv_update.setTextColor(android.support.v4.content.ContextCompat
                        .getColor(ChaseBangumiActivity.this,R.color.base_pink));
            }

            holder.tv_subscribe.setText(data.favorites + "人订阅");

        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_chase_bangumi;
        }

        @Override
        public MyViewHolder onCreateViewHolder(View view) {
            return new MyViewHolder(view);
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.iv_pic)ImageView iv_pic;
            @BindView(R.id.tv_title)TextView tv_title;
            @BindView(R.id.tv_update)TextView tv_update;
            @BindView(R.id.tv_subscribe)TextView tv_subscribe;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
