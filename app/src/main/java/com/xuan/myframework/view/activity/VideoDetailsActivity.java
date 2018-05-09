package com.xuan.myframework.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.base.adapter.BaseViewPagerAdapter;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.inter.AppbarStatusListener;
import com.xuan.myframework.utils.DisplayUtils;
import com.xuan.myframework.utils.SystemStatusBarUtil;
import com.xuan.myframework.view.fragment.CommentFragment;
import com.xuan.myframework.view.fragment.VideoIntroductionFragment;
import com.xuan.myframework.view.modle.response.VideoDetailsModle;
import com.xuan.myframework.view.presenterImpl.VideoDetailsPresenterImpl;
import com.xuan.myframework.view.view.VideoDetailsView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuan on 2017/6/21.
 */

public class VideoDetailsActivity extends RxBaseActivity<VideoDetailsPresenterImpl> implements VideoDetailsView {
    public static final String key_av_id="KEYAVID";
    public static final String key_image_url="KEYIMAGEURL";

    @BindView(R.id.app_bar)AppBarLayout app_bar;
    @BindView(R.id.collapsing_toolbar)CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.toolbar)Toolbar toolbar;

    @BindView(R.id.iv_video_preview)ImageView iv_video_preview;
    @BindView(R.id.tv_name)TextView tv_name;
    @BindView(R.id.tv_play)TextView tv_play;

    @BindView(R.id.tab_layout)SlidingTabLayout tab_layout;
    @BindView(R.id.view_pager)ViewPager view_pager;

    @BindView(R.id.fab)FloatingActionButton fab;

    private ArrayList<Fragment> fragments;
    private String[] mTitles={"简介","评论(0)"};
    private VideoDetailsPresenterImpl presenter;

    private int avId;
    private String imageUrl;
    private int avCount=0;
    private VideoDetailsModle data;

    @OnClick(R.id.fab)void playerVideo(){
        if(data!=null)
        VideoPlayerActivity.launchActivity(this,data.pages.get(0).cid,data.title);
    }

    public static void launchActivity(Activity activity,int avId,String imageUrl){
        Intent intent = new Intent(activity, VideoDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(key_av_id, avId);
        intent.putExtra(key_image_url, imageUrl);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            avId = intent.getIntExtra(key_av_id, -1);
            imageUrl = intent.getStringExtra(key_image_url);
        }

        initFragment();
        initTabLayout();
        initFab();

        presenter=new VideoDetailsPresenterImpl(this);
        presenter.queryVideoDetails(avId,this.bindToLifecycle());
    }

    private void initFab() {
        fab.setClickable(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.gray_light)));
        fab.setTranslationY(-getResources().getDimension(R.dimen.fab_translationY));
    }

    private void initTabLayout() {
        BaseViewPagerAdapter adapter=new BaseViewPagerAdapter(getSupportFragmentManager(),fragments);
        view_pager.setAdapter(adapter);
        tab_layout.setViewPager(view_pager,mTitles);
        view_pager.setOffscreenPageLimit(fragments.size());
    }

    private void initFragment() {
        if(fragments==null)
            fragments = new ArrayList<>();

        Bundle args = new Bundle();
        args.putInt(key_av_id, avId);

        fragments.add(VideoIntroductionFragment.newInstance(args));
        fragments.add(CommentFragment.newInstance(args));
    }


    @Override
    public void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置还没收缩时状态下字体颜色
        collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT);
        //设置收缩后Toolbar上字体的颜色
        collapsing_toolbar.setCollapsedTitleTextColor(Color.WHITE);

        //设置StatusBar透明
        SystemStatusBarUtil.immersiveStatusBar(this);
        SystemStatusBarUtil.setHeightAndPadding(this, toolbar);

        tv_name.setText("av" + avCount);

        initAppBar();
    }

    private void initAppBar() {
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                setFabViewsTranslation(verticalOffset);
            }
        });

        app_bar.addOnOffsetChangedListener(new AppbarStatusListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {

                if (state == State.EXPANDED) {
                    //展开状态
                    tv_play.setVisibility(View.GONE);
                    tv_name.setVisibility(View.VISIBLE);
           /*         contentInsetLeft、contentInsetRight、contentInsetStart、contentInsetEnd：Toolbar的左右两侧都是默认有16dp的padding的，
                    如果你需要让Toolbar上的内容与左右两侧的距离有变化，便可以通过以上四个属性来进行相应的设置。
                    比如要让内容紧贴左侧或起始侧便可以将contentInsetLeft或contentInsetStart设为0。
                    对应方法：setContentInsetsRelative(int,int)——对应start和end
                    setContentInsetsAbsolute(int,int)——对应left和right*/
                    toolbar.setContentInsetsRelative(DisplayUtils.dp2px(VideoDetailsActivity.this, 15), 0);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    tv_play.setVisibility(View.VISIBLE);
                    tv_name.setVisibility(View.GONE);
                    toolbar.setContentInsetsRelative(DisplayUtils.dp2px(VideoDetailsActivity.this, 150), 0);
                } else {
                    tv_play.setVisibility(View.GONE);
                    tv_name.setVisibility(View.VISIBLE);
                    toolbar.setContentInsetsRelative(DisplayUtils.dp2px(VideoDetailsActivity.this, 15), 0);
                }
            }
        });
    }

    private void setFabViewsTranslation(int target) {

        fab.setTranslationY(target);
        if (target == 0) {
            showFab();
        } else if (target < 0) {
            hideFAB();
        }
    }

    private void hideFAB() {
        fab.animate().scaleX(0f).scaleY(0f)
                .setInterpolator(new OvershootInterpolator())
                .start();

        fab.setClickable(false);
    }

    private void showFab(){
        fab.animate().scaleX(1f).scaleY(1f)
                .setInterpolator(new OvershootInterpolator())
                .start();

        fab.setClickable(true);
    }

    @Override
    public void loadDataSuccess(VideoDetailsModle data) {
        this.data=data;

        fab.setClickable(true);
        fab.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this,R.color.base_pink)));

        GlideUtils.loadDefaultImage(this,imageUrl,iv_video_preview);

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

    public void setTabLayoutText(String text) {
        TextView view=tab_layout.getTitleView(1);
        view.setText("评论("+text+")");
    }
}
