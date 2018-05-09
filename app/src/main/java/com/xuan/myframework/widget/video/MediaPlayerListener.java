package com.xuan.myframework.widget.video;

/**
 * com.xuan.myframework.view.widget.video
 * Created by xuan on 2017/7/17.
 * version
 * desc 视频控制回调接口
 */
public interface MediaPlayerListener {

  void start();

  void pause();

  int getDuration();

  int getCurrentPosition();

  void seekTo(long pos);

  boolean isPlaying();

  int getBufferPercentage();

  boolean canPause();
}
