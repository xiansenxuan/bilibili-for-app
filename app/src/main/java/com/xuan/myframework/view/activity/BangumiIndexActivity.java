package com.xuan.myframework.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
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
import com.xuan.myframework.view.modle.response.BangumiIndexModle;
import com.xuan.myframework.view.presenterImpl.BangumiIndexPresenterImpl;
import com.xuan.myframework.view.view.BangumiIndexView;
import com.xuan.myframework.widget.EmptyView;
import com.xuan.myframework.widget.MySwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.xuan.myframework.view.activity
 * Created by xuan on 2017/8/3.
 * version
 * desc
 */

public class BangumiIndexActivity extends RxBaseActivity<BangumiIndexPresenterImpl> implements BangumiIndexView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    MySwipeRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)
    LRecyclerView recycler_view;
    @BindView(R.id.empty_view)
    EmptyView empty_view;

    MyRecyclerViewAdapter adapter;
    LRecyclerViewAdapter lAdapter;

    public static void launchActivity(Activity activity) {
        Intent intent = new Intent(activity, BangumiIndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

        mPresenter = new BangumiIndexPresenterImpl(this);

        mPresenter.queryBroadcastTableData(this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {
        toolbar.setTitle("番剧索引");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void loadDataSuccess(BangumiIndexModle data) {
        adapter.addItem(data.result.category);
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
        SnackbarUtils.showShortSnackbar(recycler_view, "数据加载失败,请重新加载或者检查网络是否链接");
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
        if (adapter == null) {
            adapter = new MyRecyclerViewAdapter();
            lAdapter = new LRecyclerViewAdapter(adapter);

            GridLayoutManager layoutManager = new GridLayoutManager(this,3,  GridLayoutManager.VERTICAL,false);
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
                    mPresenter.queryBroadcastTableData(BangumiIndexActivity.this.bindToLifecycle());
                }
            });


            addRecyclerViewHeadView();
        }
    }

    private void addRecyclerViewHeadView() {
        //添加头部UI
        View view=View.inflate(this,R.layout.item_bangumi_index_head,null);

        lAdapter.removeHeaderView();
        lAdapter.addHeaderView(view);
    }

    @Override
    public void initEmptyView() {
        empty_view.setVisibility(View.GONE);
        empty_view.setEmptyImage(R.drawable.load_error);
        empty_view.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
    }

    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyViewHolder>{

        @Override
        public void onBindItemViewHolder(MyViewHolder holder, int position) {
            BangumiIndexModle.ResultBean.CategoryBean data= (BangumiIndexModle.ResultBean.CategoryBean) itemList.get(position);

            GlideUtils.loadDefaultImage(BangumiIndexActivity.this,data.cover,holder.iv_pic);

            holder.tv_title.setText(data.tag_name);
        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_bangumi_index_body;
        }

        @Override
        public MyViewHolder onCreateViewHolder(View view) {
            return new MyViewHolder(view);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_pic)ImageView iv_pic;
        @BindView(R.id.tv_title)TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
