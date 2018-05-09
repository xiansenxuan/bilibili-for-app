package com.xuan.myframework.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.inter.ConfigInter;
import com.xuan.myframework.view.modle.response.VideoPlayerModle;
import com.xuan.myframework.view.presenterImpl.VideoPlayerPresenterImpl;
import com.xuan.myframework.view.view.VideoPlayerView;
import com.xuan.myframework.widget.CircleProgressDialog;
import com.xuan.myframework.widget.video.DanmukuDownloadUtils;
import com.xuan.myframework.widget.video.DanmukuSwitchListener;
import com.xuan.myframework.widget.video.MediaController;
import com.xuan.myframework.widget.video.MediaPlayerView;
import com.xuan.myframework.widget.video.VideoBackListener;

import java.util.HashMap;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * com.xuan.myframework.view.fragment
 * Created by xuan on 2017/7/17.
 * version
 * desc
 */

public class VideoPlayerActivity extends RxBaseActivity<VideoPlayerPresenterImpl> implements VideoPlayerView {

    @BindView(R.id.player_view)MediaPlayerView player_view;
    @BindView(R.id.danmaku_view)DanmakuView danmaku_view;
    @BindView(R.id.circle_progress)CircleProgressDialog circle_progress;
    @BindView(R.id.rl_start_info)RelativeLayout rl_start_info;
    @BindView(R.id.iv_anim)ImageView iv_anim;
    @BindView(R.id.tv_start_info)TextView tv_start_info;


    public static final String key_cid="KEYCID";
    public static final String key_title="KEYTITLE";

    int cid;
    String title;

    private String startText = "初始化播放器...";
    private AnimationDrawable animDrawable;

    private DanmakuContext danmakuContext;

    private int LastPosition = 0;

    private VideoPlayerPresenterImpl presenter;

    public static void launchActivity(Activity activity, int cid, String title){
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(key_cid, cid);
        intent.putExtra(key_title, title);
        activity.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_video_player;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        cid=getIntent().getIntExtra(key_cid,-1);
        title=getIntent().getStringExtra(key_title);

        presenter=new VideoPlayerPresenterImpl(this);
        presenter.queryVideoPlayerData(cid,4, ConfigInter.VIDEO_TYPE_MP4,this.bindToLifecycle());

        initLoadingAnimation();
        initPlayerView();
    }

    /**
     * 初始化加载动画
     */
    private void initLoadingAnimation() {

        rl_start_info.setVisibility(View.VISIBLE);
        startText = startText + "【完成】\n解析视频地址...【完成】\n全舰弹幕填装...";
        tv_start_info.setText(startText);

        animDrawable = (AnimationDrawable) iv_anim.getBackground();
        animDrawable.start();
    }

    private void initPlayerView() {
        //配置播放器
        MediaController controller = new MediaController(this);
        controller.setTitle(title);

        player_view.setMediaController(controller);
        //设置缓冲加载进度
        player_view.setMediaBufferingIndicator(rl_start_info);
        player_view.requestFocus();
        player_view.setOnInfoListener(onInfoListener);
        player_view.setOnSeekCompleteListener(onSeekCompleteListener);
        player_view.setOnCompletionListener(onCompletionListener);
        player_view.setOnControllerEventsListener(onControllerEventsListener);
        //设置弹幕开关监听
        controller.setDanmakuSwitchListener(switchListener);
        //设置返回键监听
        controller.setVideoBackEvent(backListener);

        //配置弹幕库
        danmaku_view.enableDanmakuDrawingCache(true);
        //设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        //滚动弹幕最大显示5行
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5);
        //设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        //设置弹幕样式
        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setScaleTextSize(0.8f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
    }


    private DanmukuSwitchListener switchListener=new DanmukuSwitchListener() {
        @Override
        public void setDanmakushow(boolean isShow) {
            if (danmaku_view != null) {
                if (isShow) {
                    danmaku_view.show();
                } else {
                    danmaku_view.hide();
                }
            }
        }
    };

    private VideoBackListener backListener=new VideoBackListener() {
        @Override
        public void back() {
            onBackPressed();
        }
    };

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();

        if(danmaku_view!=null){
            danmaku_view.release();
            danmaku_view=null;
        }
        if(animDrawable!=null){
            animDrawable.stop();
            animDrawable=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (danmaku_view != null && danmaku_view.isPrepared() && danmaku_view.isPaused()) {
            danmaku_view.seekTo((long) LastPosition);
        }
        if (player_view != null && !player_view.isPlaying()) {
            player_view.seekTo(LastPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (player_view != null) {
            LastPosition = player_view.getCurrentPosition();
            player_view.pause();
        }

        if (danmaku_view != null && danmaku_view.isPrepared()) {
            danmaku_view.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player_view != null && player_view.isDrawingCacheEnabled()) {
            player_view.destroyDrawingCache();
        }
        if (danmaku_view != null && danmaku_view.isPaused()) {
            danmaku_view.release();
            danmaku_view = null;
        }
        if (animDrawable != null) {
            animDrawable.stop();
            animDrawable = null;
        }
    }

    /**
     * 视频缓冲事件回调
     */
    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {

            if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                if (danmaku_view != null && danmaku_view.isPrepared()) {
                    danmaku_view.pause();

                    showProgress();
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                if (danmaku_view != null && danmaku_view.isPaused()) {
                    danmaku_view.resume();
                }

                hideProgress();
            }
            return true;
        }
    };

    /**
     * 视频跳转事件回调
     */
    private IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener
            = new IMediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(IMediaPlayer mp) {

            if (danmaku_view != null && danmaku_view.isPrepared()) {
                danmaku_view.seekTo(mp.getCurrentPosition());
            }
        }
    };

    /**
     * 视频播放完成事件回调
     */
    private IMediaPlayer.OnCompletionListener onCompletionListener
            = new IMediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(IMediaPlayer mp) {

            if (danmaku_view != null && danmaku_view.isPrepared()) {
                danmaku_view.seekTo((long) 0);
                danmaku_view.pause();
            }
            player_view.pause();
        }
    };

    /**
     * 控制条控制状态事件回调
     */
    private MediaPlayerView.OnControllerEventsListener onControllerEventsListener
            = new MediaPlayerView.OnControllerEventsListener() {

        @Override
        public void onVideoPause() {

            if (danmaku_view != null && danmaku_view.isPrepared()) {
                danmaku_view.pause();
            }
        }


        @Override
        public void OnVideoResume() {

            if (danmaku_view != null && danmaku_view.isPaused()) {
                danmaku_view.resume();
            }
        }
    };

    @Override
    public void initToolBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void loadDataSuccess(VideoPlayerModle data) {
        String url="http://vod2017.cnlive.com/3/vod/2017/0613/3_d127164bcdb04c7fbb10deede8e9b9b5/ff8080815bf6b453015ca03a84b32164_1500.m3u8";

        Observable.just(Uri.parse(url))
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Uri, Observable<BaseDanmakuParser>>() {
                    @Override
                    public Observable<BaseDanmakuParser> apply(@NonNull Uri uri) throws Exception {
                        player_view.setVideoURI(uri);
                        player_view.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(IMediaPlayer iMediaPlayer) {
                                animDrawable.stop();
                                startText = startText + "【完成】\n视频缓冲中...";
                                tv_start_info.setText(startText);
                                rl_start_info.setVisibility(View.GONE);
                            }
                        });

                        String url = "http://comment.bilibili.com/" + cid + ".xml";
                        return DanmukuDownloadUtils.downloadXML(url);
                    }
                })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<BaseDanmakuParser>() {
            @Override
            public void accept(@NonNull BaseDanmakuParser baseDanmakuParser) throws Exception {
                danmaku_view.prepare(baseDanmakuParser, danmakuContext);
                danmaku_view.showFPS(false);
                danmaku_view.enableDanmakuDrawingCache(false);
                danmaku_view.setCallback(new DrawHandler.Callback() {

                    @Override
                    public void prepared() {

                        danmaku_view.start();
                    }


                    @Override
                    public void updateTimer(DanmakuTimer danmakuTimer) {

                    }


                    @Override
                    public void danmakuShown(BaseDanmaku danmaku) {

                    }


                    @Override
                    public void drawingFinished() {

                    }
                });

                player_view.start();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();

                startText = startText + "【失败】\n视频缓冲中...";
                tv_start_info.setText(startText);
                startText = startText + "【失败】\n" + throwable.getMessage();
                tv_start_info.setText(startText);
            }
        });

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
