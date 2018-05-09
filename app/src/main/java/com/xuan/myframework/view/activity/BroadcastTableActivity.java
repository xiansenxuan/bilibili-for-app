package com.xuan.myframework.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.NumberUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.modle.response.BroadcastTableModle;
import com.xuan.myframework.view.presenterImpl.BroadcastTablePresenterImpl;
import com.xuan.myframework.view.view.BroadcastTableView;
import com.xuan.myframework.widget.EmptyView;
import com.xuan.myframework.widget.MySwipeRefreshLayout;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * com.xuan.myframework.view.activity
 * Created by xuan on 2017/8/3.
 * version
 * desc
 */

public class BroadcastTableActivity extends RxBaseActivity<BroadcastTablePresenterImpl> implements BroadcastTableView {
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
    private List<Visitable> visitableList;

    public static void launchActivity(Activity activity) {
        Intent intent = new Intent(activity, BroadcastTableActivity.class);
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

        mPresenter = new BroadcastTablePresenterImpl(this);

        mPresenter.queryBroadcastTableData(this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {
        toolbar.setTitle("放送表");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void loadDataSuccess(List<Visitable> data) {
        visitableList=data;

        adapter.refreshData(data);
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
            adapter = new MyRecyclerViewAdapter(new ItemTypeFactory());
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

            lAdapter.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    if(visitableList!=null && visitableList.size()>0){
                        Object item = visitableList.get(position);
                        if(item instanceof BroadcastTableModle.SeasonHeadTypeModle){
                            return 3;
                        }else if(item instanceof BroadcastTableModle.SeasonBodyTypeModle){
                            return 1;
                        }
                    }
                    return gridLayoutManager.getSpanCount();
                }
            });

            refresh_layout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPresenter.queryBroadcastTableData(BroadcastTableActivity.this.bindToLifecycle());
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


    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<BroadcastTableActivity.MyViewHolder> {


        private BroadcastTableActivity.TypeFactory mTypeFactory;
        private List<Visitable> visitableList;


        public MyRecyclerViewAdapter(BroadcastTableActivity.TypeFactory mTypeFactory) {
            this.mTypeFactory = mTypeFactory;
        }

        public void refreshData(List<Visitable> visitableList) {
            if (this.visitableList != null) {
                this.visitableList = null;
            }
            this.visitableList = visitableList;
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
            if (visitableList != null && visitableList.size() > 0) {
                return visitableList.size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return visitableList.get(position).type(mTypeFactory);
        }
    }

    public abstract class MyViewHolder<T extends BroadcastTableActivity.Visitable> extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindItem(T t);
    }

    public interface Visitable {

        int type(BroadcastTableActivity.TypeFactory typeFactory);

    }

    public interface TypeFactory {

        int type(BroadcastTableModle.SeasonHeadTypeModle head);

        int type(BroadcastTableModle.SeasonBodyTypeModle body);

        MyViewHolder onCreateViewHolder(View itemView, int viewType);

    }

    public class ItemTypeFactory implements TypeFactory {

        @Override
        public int type(BroadcastTableModle.SeasonHeadTypeModle head) {
            return R.layout.item_broadcast_table_head;
        }

        @Override
        public int type(BroadcastTableModle.SeasonBodyTypeModle body) {
            return R.layout.item_broadcast_table_body;
        }

        @Override
        public MyViewHolder onCreateViewHolder(View itemView, int viewType) {
            MyViewHolder viewHolder=null;
            switch (viewType){
                case R.layout.item_broadcast_table_head:
                    viewHolder=new BroadcastTableHeadViewHolder(itemView);
                    break;
                case R.layout.item_broadcast_table_body:
                    viewHolder=new BroadcastTableBodyViewHolder(itemView);
                    break;

                default:
                    break;
            }
            return viewHolder;
        }
    }


    class BroadcastTableHeadViewHolder extends MyViewHolder{
        @BindView(R.id.iv_icon)
        ImageView iv_icon;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_update)
        TextView tv_update;

        public BroadcastTableHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BroadcastTableModle.SeasonHeadTypeModle modle= (BroadcastTableModle.SeasonHeadTypeModle) visitable;
            BroadcastTableModle.SeasonModle data=modle.data;

            iv_icon.setImageResource(data.iconResId);
            tv_date.setText(data.weekTitle);

            String today=NumberUtils.longFormatString(new Date().getTime(),NumberUtils.simplePattern);
            if (data.time.equals(today)){
                tv_update.setText("今天");
                tv_update.setTextColor(ContextCompat.getColor(BroadcastTableActivity.this,R.color.base_pink));
                tv_date.setTextColor(ContextCompat.getColor(BroadcastTableActivity.this,R.color.base_pink));
                iv_icon.setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(BroadcastTableActivity.this,R.color.base_pink)));
            } else {
                tv_update.setText(data.time);
                tv_update.setTextColor(ContextCompat.getColor(BroadcastTableActivity.this,R.color.dark_black_text));
                tv_date.setTextColor(ContextCompat.getColor(BroadcastTableActivity.this,R.color.light_black_text));
                iv_icon.setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(BroadcastTableActivity.this,R.color.light_black_text)));
            }
        }
    }


    class BroadcastTableBodyViewHolder extends MyViewHolder{
        @BindView(R.id.iv_pic)
        ImageView iv_pic;
        @BindView(R.id.tv_time_line)
        TextView tv_time_line;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_update)
        TextView tv_update;


        public BroadcastTableBodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindItem(Visitable visitable) {
            BroadcastTableModle.SeasonBodyTypeModle modle= (BroadcastTableModle.SeasonBodyTypeModle) visitable;
            BroadcastTableModle.ResultBean data=modle.data;

            GlideUtils.loadDefaultImage(BroadcastTableActivity.this,data.cover,iv_pic);

            tv_time_line.setText(data.ontime);
            tv_title.setText(data.title);
            tv_update.setText("第" + data.ep_index + "话");
        }
    }

}
