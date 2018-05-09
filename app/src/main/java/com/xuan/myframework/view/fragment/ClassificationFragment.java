package com.xuan.myframework.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.myframework.R;
import com.xuan.myframework.base.adapter.BaseRecyclerViewAdapter;
import com.xuan.myframework.base.adapter.BaseViewHolder;
import com.xuan.myframework.base.fragment.RxBaseFragment;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.modle.response.ClassificationModle;
import com.xuan.myframework.view.presenterImpl.ClassificationPresenterImpl;
import com.xuan.myframework.view.view.ClassificationView;
import com.xuan.myframework.widget.EmptyView;
import com.xuan.myframework.widget.MySwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuan on 2017/9/7.
 * 分类
 */
public class ClassificationFragment extends RxBaseFragment<ClassificationPresenterImpl> implements ClassificationView {
    @BindView(R.id.refresh_layout)MySwipeRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)RecyclerView recycler_view;
    @BindView(R.id.empty_view)EmptyView empty_view;

    MyRecyclerViewAdapter adapter;

    private String[] itemNames = new String[] {
            "直播", "番剧", "动画",
            "音乐", "舞蹈", "游戏",
            "科技", "生活", "鬼畜",
            "时尚", "广告", "娱乐",
            "电影", "电视剧", "游戏中心",
    };

    private int[] itemIcons = new int[] {
            R.drawable.ic_category_live, R.drawable.ic_category_t13,
            R.drawable.ic_category_t1, R.drawable.ic_category_t3,
            R.drawable.ic_category_t129, R.drawable.ic_category_t4,
            R.drawable.ic_category_t36, R.drawable.ic_category_t160,
            R.drawable.ic_category_t119, R.drawable.ic_category_t155,
            R.drawable.ic_category_t165, R.drawable.ic_category_t5,
            R.drawable.ic_category_t23, R.drawable.ic_category_t11,
            R.drawable.ic_category_game_center
    };

    public static ClassificationFragment newInstance(Bundle args) {
        ClassificationFragment fragment = new ClassificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classification;
    }

    @Override
    public void initView(View view) {
        mPresenter =new ClassificationPresenterImpl(this);

        initRecycler();
    }

    @Override
    public void initData() {
    }

    @Override
    public void initToolBar() {
    }

    @Override
    public void loadDataSuccess(ClassificationModle data) {
        if(data!=null && data.data!=null && data.data.size()>0){
            adapter.addItem(data.data);
            adapter.notifyDataSetChanged();
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
        SnackbarUtils.showShortSnackbar(this.getView(),"数据加载失败,请重新加载或者检查网络是否链接");
    }


    @Override
    public void initEmptyView() {
        empty_view.setVisibility(View.GONE);
        empty_view.setEmptyImage(R.drawable.load_error);
        empty_view.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
    }

    @Override
    public void showEmptyView() {
        recycler_view.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        recycler_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void initRecycler() {
        //设置adapter
        if(adapter==null){
            adapter=new MyRecyclerViewAdapter();

            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(), 3,GridLayoutManager.VERTICAL,false);
            recycler_view.setLayoutManager(gridLayoutManager);

            recycler_view.setAdapter(adapter);

            recycler_view.setNestedScrollingEnabled(true);
            recycler_view.setHasFixedSize(true);
        }
    }

    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<ClassificationFragment.MyViewHolder> {
        @Override
        public int getItemCount() {
            return itemIcons.length;
        }

        @Override
        public void onBindItemViewHolder(MyViewHolder holder, int position) {
            holder.tv_title.setText(itemNames[position]);
            holder.iv_icon.setImageResource(itemIcons[position]);
        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_classification;
        }

        @Override
        public MyViewHolder onCreateViewHolder(View view) {
            return new MyViewHolder(view);
        }
    }

    public class MyViewHolder extends BaseViewHolder{
        @BindView(R.id.iv_icon)ImageView iv_icon;
        @BindView(R.id.tv_title)TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
