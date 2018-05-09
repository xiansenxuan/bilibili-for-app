package com.xuan.myframework.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView;
import com.xuan.myframework.R;
import com.xuan.myframework.base.Fragmentation.RxSupportFragment;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.view.fragment.CacheFragment;
import com.xuan.myframework.view.fragment.MainFragment;
import com.xuan.myframework.view.fragment.VipFragment;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class MainActivity extends RxBaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.navigation_view)
    NavigationView navigation_view;

    private Fragment[] fragments;
    private int currentPosition = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initFragment(savedInstanceState);
        initNavigationView();
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle mainBundle = new Bundle();
            mainBundle.putString("content", "MainFragment");
            Bundle vipBundle = new Bundle();
            vipBundle.putString("content", "VipFragment");
            Bundle cacheBundle = new Bundle();
            cacheBundle.putString("content", "CacheFragment");

            fragments = new Fragment[]{MainFragment.newInstance(mainBundle),
                    VipFragment.newInstance(vipBundle),
                    CacheFragment.newInstance(cacheBundle)};

            loadMultipleRootFragment(R.id.fl_content, 0,
                    (RxSupportFragment) fragments[0]
                    , (RxSupportFragment) fragments[1]
                    , (RxSupportFragment) fragments[2]);

        }

    }

    public void toggleDrawerLayout() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            drawer_layout.openDrawer(GravityCompat.START);
        }
    }

    private void initNavigationView() {
        RxNavigationView.itemSelections(navigation_view)
                .subscribe(new Consumer<MenuItem>() {
                    @Override
                    public void accept(@NonNull MenuItem menuItem) throws Exception {

                        if (fragments == null || fragments.length == 0) return;

                        switch (menuItem.getItemId()) {
                            case R.id.item_home:

                                showHideFragment((RxSupportFragment) fragments[0]
                                        , (RxSupportFragment) fragments[currentPosition]);

                                currentPosition = 0;
                                break;
                            case R.id.item_vip:
                                showHideFragment((RxSupportFragment) fragments[1]
                                        , (RxSupportFragment) fragments[currentPosition]);

                                currentPosition = 1;
                                break;
                            case R.id.item_download:
                                showHideFragment((RxSupportFragment) fragments[2]
                                        , (RxSupportFragment) fragments[currentPosition]);

                                currentPosition = 2;
                                break;
                            default:

                                break;

                        }

                        drawer_layout.closeDrawer(GravityCompat.START);
                    }
                });

    }


    @Override
    public void initToolBar() {

    }

    @Override
    public void onBackPressedSupport() {
        if (fragments[0] != null && ((MainFragment)fragments[0]).isSearchOpen()) {
            ((MainFragment)fragments[0]).closeSearchView();
            return;
        } else {
            super.onBackPressedSupport();
        }

    }
}
/*      viewpager+bottomNavigation 模式

<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.design.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.design.widget.AppBarLayout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
            app:elevation="3dp">

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                style="@style/ToolBar"
                app:layout_scrollFlags="scroll|enterAlways"
                app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
                app:titleTextAppearance="@style/ToolBar.TitleText" />

            <com.flyco.tablayout.SlidingTabLayout
                android:id="@+id/tab_layout"
                android:layout_width="match_parent"
                android:layout_height="48dp"
                android:background="?attr/colorPrimary"
                android:paddingLeft="10dp"
                android:paddingRight="10dp"
                app:tl_indicator_corner_radius="1dp"
                app:tl_indicator_height="2dp"
                app:tl_indicator_width="40dp"
                app:tl_textAllCaps="true"
                app:tl_textBold="BOTH"
                app:tl_textsize="14sp"
                app:tl_tab_space_equal="true" />
        </android.support.design.widget.AppBarLayout>

        <android.support.v4.view.ViewPager
            android:id="@+id/view_pager"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1" />

        <android.support.design.widget.BottomNavigationView
            android:id="@+id/bottom_navigation_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:background="?android:attr/windowBackground"
            app:menu="@menu/menu_navigation" />

    </android.support.design.widget.CoordinatorLayout>
</FrameLayout>




    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tab_layout;
    @BindView(R.id.view_pager)
    android.support.v4.view.ViewPager view_pager;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottom_navigation_view;

        initFragment();
        initViewPager();
        initBottomNavigation();

        mainPresenter = new MainPresenterImpl(this);
        mainPresenter.queryDetails();


                          HomeFragment fragment1;
    HomeFragment fragment2;
    HomeFragment fragment3;

    private void initFragment() {
        fragments = new ArrayList<>();

        Bundle args1 = new Bundle();
        args1.putString("content", "测试页面1");
        Bundle args2 = new Bundle();
        args2.putString("content", "测试页面2");
        Bundle args3 = new Bundle();
        args3.putString("content", "测试页面3");
        fragment1 = HomeFragment.newInstance(args1);
        fragment2 = HomeFragment.newInstance(args2);
        fragment3 = HomeFragment.newInstance(args3);

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
    }


    private void initBottomNavigation() {
        RxBottomNavigationView.itemSelections(bottom_navigation_view)
                .subscribe(new Consumer<MenuItem>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull MenuItem menuItem) throws Exception {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_home:
                                view_pager.setCurrentItem(0);
                                break;
                            case R.id.navigation_dashboard:
                                view_pager.setCurrentItem(1);
                                break;
                            case R.id.navigation_notifications:
                                view_pager.setCurrentItem(2);
                                break;
                        }
                    }
                });
    }



    private void initViewPager() {

//        loadMultipleRootFragment(R.id.view_pager, 0,
//                fragment1, fragment2, fragment3
//        );

        String[] titles=new String[]{"直播","推荐","番剧"};

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);

        //关联ViewPager,用于连适配器都不想自己实例化的情况
//        tab_layout.setViewPager(view_pager,titles,this,fragments);

                }



    public class MyViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        private List<android.support.v4.app.Fragment> fragments;

        public MyViewPagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<android.support.v4.app.Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }

    }



    */