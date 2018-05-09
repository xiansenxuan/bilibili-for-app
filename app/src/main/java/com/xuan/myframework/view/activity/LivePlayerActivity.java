package com.xuan.myframework.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.widget.LoveHeartView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by xuan on 2017/6/8.
 */

public class LivePlayerActivity extends RxBaseActivity<com.xuan.myframework.view.presenterImpl.LivePlayerPresenterImpl> implements com.xuan.myframework.view.view.LivePlayerView {

    public static final String key_cid = "cid";
    public static final String key_title = "title";
    public static final String key_online = "online";
    public static final String key_face = "face";
    public static final String key_name = "name";
    public static final String key_mid = "mid";

    private int cid;

    private String title;

    private int online;

    private String face;

    private String name;

    private int mid;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_video)
    FrameLayout fl_video;
    @BindView(R.id.video_surface)
    SurfaceView video_surface;

    @BindView(R.id.rl_loading)
    RelativeLayout rl_loading;
    @BindView(R.id.iv_anim_loading)
    ImageView iv_anim_loading;
    @BindView(R.id.tv_init)
    TextView tv_init;
    @BindView(R.id.iv_right_play)
    ImageView iv_right_play;

    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.iv_play_action)
    ImageView iv_play_action;
    @BindView(R.id.iv_gift)
    ImageView iv_gift;
    @BindView(R.id.iv_zoom)
    ImageView iv_zoom;

    @BindView(R.id.love_heart)
    LoveHeartView love_heart;

    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_live_count)
    TextView tv_live_count;
    @BindView(R.id.tv_attention)
    TextView tv_attention;

    private IjkMediaPlayer ijkMediaPlayer;
    private AnimationDrawable anim;

    private boolean isShowBottomView=true;
    private boolean isPlay = false;

    @OnClick(R.id.iv_gift)
    void addLoveHeart() {
        love_heart.addLove();
    }

    @OnClick(R.id.video_surface)void isShowBottomView(){
        if(isShowBottomView){
            isShowBottomView=false;
            rl_bottom.setVisibility(View.GONE);
            iv_right_play.setVisibility(View.GONE);
        }else{
            isShowBottomView=true;
            rl_bottom.setVisibility(View.VISIBLE);
            iv_right_play.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.iv_right_play)void rightPlay(){
        changePlayState();
    }

    @OnClick(R.id.iv_play_action)void actionPlay(){
        changePlayState();
    }

    @OnClick(R.id.iv_zoom)void zoomVideo(){
        Snackbar.make(fl_video,"敬请期待!",Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_header)void upUserDetails(){
        UserDetailsActivity.launchActivity(LivePlayerActivity.this, name, mid, face);
        changePlayState();
        iv_right_play.setVisibility(View.VISIBLE);
    }


    private void changePlayState() {
        if(ijkMediaPlayer==null) return;

        if (isPlay) {
            ijkMediaPlayer.pause();
            isPlay = false;
            iv_right_play.setImageResource(R.drawable.icon_play);
            iv_play_action.setImageResource(R.drawable.icon_play_start);
        } else {
            ijkMediaPlayer.start();
            isPlay = true;
            iv_right_play.setImageResource(R.drawable.icon_stop);
            iv_play_action.setImageResource(R.drawable.icon_play_stop);
        }
    }

    public static void launchActivity(Activity activity, int cid, String title, int online, String face, String name, int mid) {
        Intent mIntent = new Intent(activity, LivePlayerActivity.class);
        mIntent.putExtra(key_cid, cid);
        mIntent.putExtra(key_title, title);
        mIntent.putExtra(key_online, online);
        mIntent.putExtra(key_face, face);
        mIntent.putExtra(key_name, name);
        mIntent.putExtra(key_mid, mid);
        activity.startActivity(mIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_player;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            cid = intent.getIntExtra(key_cid, 0);
            title = intent.getStringExtra(key_title);
            online = intent.getIntExtra(key_online, 0);
            face = intent.getStringExtra(key_face);
            name = intent.getStringExtra(key_name);
            mid = intent.getIntExtra(key_mid, 0);
        }

        tv_name.setText(name);
        tv_live_count.setText(online+"");
        GlideUtils.loadCircleImage(this,face,iv_header);

        mPresenter = new com.xuan.myframework.view.presenterImpl.LivePlayerPresenterImpl(this);
        mPresenter.queryLivePlayerData(cid, this.bindToLifecycle());

    }

    @Override
    public void initToolBar() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loadDataSuccess(String data) {
        String uri = data.substring(data.indexOf("<url><![CDATA[") + 14, data.indexOf("]]></url>"));

        playVideo(uri);
    }

    private void playVideo(String uri) {
        if (ijkMediaPlayer == null)
            ijkMediaPlayer = new IjkMediaPlayer();

        try {
            ijkMediaPlayer.setDataSource(this, Uri.parse(uri));
            ijkMediaPlayer.setDisplay(video_surface.getHolder());
            video_surface.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    ijkMediaPlayer.setDisplay(holder);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });

            ijkMediaPlayer.setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
                @Override
                public boolean onNativeInvoke(final int i, Bundle bundle) {
                    Observable.just(i).subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(@NonNull Integer integer) throws Exception {
                                    if (i == IjkMediaPlayer.OnNativeInvokeListener.EVENT_DID_HTTP_OPEN) {
                                        stopAnim();
                                        isPlay = true;
                                        video_surface.setVisibility(View.VISIBLE);
                                        iv_right_play.setImageResource(R.drawable.icon_stop);
                                        iv_play_action.setImageResource(R.drawable.icon_play_stop);
                                    }
                                }
                            });
                    return false;
                }
            });


            ijkMediaPlayer.prepareAsync();
            ijkMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ijkMediaPlayer.setKeepInBackground(false);
    }

    @Override
    public void showProgress() {
        startAnim();
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ijkMediaPlayer!=null)
        ijkMediaPlayer.release();
    }

    @Override
    public void startAnim() {
        anim = (AnimationDrawable) iv_anim_loading.getBackground();
        anim.start();
    }

    @Override
    public void stopAnim() {
        anim.stop();
        iv_anim_loading.setVisibility(View.GONE);
        tv_init.setVisibility(View.GONE);
        rl_loading.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        changePlayState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_right_play.setVisibility(View.VISIBLE);
    }
}
