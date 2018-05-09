package com.xuan.myframework.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.inter.AppbarStatusListener;
import com.xuan.myframework.utils.SystemStatusBarUtil;
import com.xuan.myframework.view.modle.response.UserDetailsModle;
import com.xuan.myframework.view.presenterImpl.UserDetailsPresenterImpl;
import com.xuan.myframework.view.view.UserDetailsView;
import com.xuan.myframework.widget.CircleProgressDialog;

import butterknife.BindView;

/**
 * Created by xuan on 2017/6/12.
 */

public class UserDetailsActivity extends RxBaseActivity implements UserDetailsView {
    public static final String key_user_name = "user_Name";
    public static final String key_mid = "mid";
    public static final String key_avatar_url = "avatar_url";

    private String name;
    private int mid;
    private String avatar_url;

    private UserDetailsPresenterImpl presenter;

    @BindView(R.id.scroll_view)android.support.v4.widget.NestedScrollView scroll_view;
    @BindView(R.id.circle_progress)CircleProgressDialog circle_progress;

    @BindView(R.id.app_bar)AppBarLayout app_bar;
    @BindView(R.id.collapsing_toolbar)CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.toolbar)Toolbar toolbar;

    @BindView(R.id.iv_user_bg)ImageView iv_user_bg;
    @BindView(R.id.iv_header)ImageView iv_header;

    @BindView(R.id.tv_name)TextView tv_name;
    @BindView(R.id.iv_sex)ImageView iv_sex;
    @BindView(R.id.iv_level)ImageView iv_level;

    @BindView(R.id.tv_attention_count)TextView tv_attention_count;
    @BindView(R.id.tv_fans_count)TextView tv_fans_count;
    @BindView(R.id.tv_details)TextView tv_details;



    public static void launchActivity(Activity activity, String name, int mid, String avatar_url) {
        Intent intent = new Intent(activity, UserDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(key_user_name, name);
        intent.putExtra(key_mid, mid);
        intent.putExtra(key_avatar_url, avatar_url);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        name=getIntent().getStringExtra(key_user_name);
        mid=getIntent().getIntExtra(key_mid,0);
        avatar_url=getIntent().getStringExtra(avatar_url);

        presenter=new UserDetailsPresenterImpl(this);
        presenter.queryDetails(mid,this.bindToLifecycle());

        if (name != null) {
            tv_name.setText(name);
        }

        if (avatar_url != null) {
            GlideUtils.loadCircleImage(this,avatar_url,iv_header);
        }
    }

    @Override
    public void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置StatusBar透明
        SystemStatusBarUtil.immersiveStatusBar(this);
        SystemStatusBarUtil.setHeightAndPadding(this, toolbar);

        //设置AppBar展开折叠状态监听
        app_bar.addOnOffsetChangedListener(new AppbarStatusListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {

                if (state == State.EXPANDED) {
                    //展开状态
                    collapsing_toolbar.setTitle("");
//                    mLineView.setVisibility(View.VISIBLE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    collapsing_toolbar.setTitle(name);
//                    mLineView.setVisibility(View.GONE);
                } else {
                    collapsing_toolbar.setTitle("");
//                    mLineView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void loadDataSuccess(UserDetailsModle data) {
        //设置用户头像
        GlideUtils.loadCircleImage(this,data.card.face,iv_header);

        //设置粉丝和关注
        tv_name.setText(data.card.name);
        tv_attention_count.setText(data.card.attention+"  关注");
        tv_fans_count.setText(converString(data.card.fans)+"  粉丝");

        //设置用户等级
        int rank=Integer.valueOf(data.card.rank);
        if (rank == 0) {
            iv_level.setImageResource(R.drawable.level_0);
        } else if (rank == 1) {
            iv_level.setImageResource(R.drawable.level_1);
        } else if (rank == 200) {
            iv_level.setImageResource(R.drawable.level_2);
        } else if (rank == 1500) {
            iv_level.setImageResource(R.drawable.level_3);
        } else if (rank == 3000) {
            iv_level.setImageResource(R.drawable.level_4);
        } else if (rank == 7000) {
            iv_level.setImageResource(R.drawable.level_5);
        } else if (rank == 10000) {
            iv_level.setImageResource(R.drawable.level_6);
        }

        //设置用户性别
        switch (data.card.sex){
            case "男":
                iv_sex.setImageResource(R.drawable.sex_man);
                break;
            case "女":
                iv_sex.setImageResource(R.drawable.sex_women);
                break;
            default:
                iv_sex.setImageResource(R.drawable.sex_gay);
                break;
        }

        //设置用户签名信息
        if (!TextUtils.isEmpty(data.card.sign)){
            tv_details.setText(data.card.sign);
        } else {
            tv_details.setText("这个人懒死了,什么都没有写(・－・。)");
        }

    }

    public String converString(int num) {

        if (num < 100000) {
            return String.valueOf(num);
        }
        String unit = "万";
        double newNum = num / 10000.0;

        String numStr = String.format("%." + 1 + "f", newNum);
        return numStr + unit;
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

    }
}
