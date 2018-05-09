package com.xuan.myframework.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.xuan.myframework.R;
import com.xuan.myframework.base.adapter.BaseViewPagerAdapter;
import com.xuan.myframework.base.fragment.RxBaseFragment;
import com.xuan.myframework.view.activity.MainActivity;
import com.xuan.myframework.view.presenterImpl.MainPresenterImpl;
import com.xuan.myframework.view.view.MainView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by xuan on 2017/5/26.
 */

public class MainFragment extends RxBaseFragment<MainPresenterImpl> implements MainView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tab_layout;
    @BindView(R.id.view_pager)
    android.support.v4.view.ViewPager view_pager;

    @BindView(R.id.ll_navigation)LinearLayout ll_navigation;
    @BindView(R.id.iv_drawer_layout)ImageView iv_drawer_layout;
    @BindView(R.id.iv_head)ImageView iv_head;
    @BindView(R.id.tv_name)TextView tv_name;

    @BindView(R.id.search_view)MaterialSearchView search_view;

    String mTitles[]={"直播","推荐","番剧","分区","关注","发现"};
    ArrayList<Fragment> fragments;


    public static Fragment newInstance(Bundle args) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view) {

        //显示菜单必须true
        setHasOptionsMenu(true);

        initFragment();
        initTabLayout();
        initSearchView();
    }

    private void initTabLayout() {
        BaseViewPagerAdapter adapter=new BaseViewPagerAdapter(getFragmentManager(),fragments);
        view_pager.setAdapter(adapter);
        tab_layout.setViewPager(view_pager,mTitles);
        view_pager.setOffscreenPageLimit(fragments.size());
    }

    private void initFragment() {
        if(fragments==null)
        fragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle args = new Bundle();
            args.putString("content", mTitles[i]);

            if(i==0){
                fragments.add(HomeFragment.newInstance(args));
            }else if(i==1){
                fragments.add(RecommendFragment.newInstance(args));
            }else if(i==2){
                fragments.add(BangumiFragment.newInstance(args));
            }else{
                fragments.add(ClassificationFragment.newInstance(args));
            }
        }
    }


    @Override
    public void initData() {
        mPresenter = new MainPresenterImpl(this);
        mPresenter.queryDetails();
    }

    @Override
    public void initToolBar() {
        //设置左上角打开抽屉按钮
        RxView.clicks(ll_navigation)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)//2秒内只取第一次事件
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Activity activity=getActivity();
                        if(activity!=null && activity instanceof MainActivity){
                            ((MainActivity) activity).toggleDrawerLayout();
                        }
                    }
                });

        Activity activity=getActivity();
        if(activity!=null && activity instanceof MainActivity){
            toolbar.setTitle("");
            ((MainActivity) activity).setSupportActionBar(toolbar);
        }
    }

    private void initSearchView() {

        //初始化SearchBar
        search_view.setVoiceSearch(false);
        search_view.setCursorDrawable(R.drawable.custom_cursor);
        search_view.setEllipsize(true);
        search_view.setSuggestions(getResources().getStringArray(R.array.query_suggestions));//查询关键字

        search_view.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        search_view.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }


    public boolean isSearchOpen() {
        return search_view.isSearchOpen();
    }


    public void closeSearchView(){
        search_view.closeSearch();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.item_action_search);
        search_view.setMenuItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_action_game:

                break;
            case R.id.item_action_download:

                break;
            case R.id.item_action_search:


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadDataSuccess(String data) {
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showMsg(String message) {
    }


}
