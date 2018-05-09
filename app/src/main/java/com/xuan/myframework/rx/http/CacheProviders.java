package com.xuan.myframework.rx.http;

import com.xuan.myframework.view.modle.response.BangumiCommentsModle;
import com.xuan.myframework.view.modle.response.BangumiDetailModle;
import com.xuan.myframework.view.modle.response.BangumiDetailsRecommendedModle;
import com.xuan.myframework.view.modle.response.BangumiIndexModle;
import com.xuan.myframework.view.modle.response.BangumiModle;
import com.xuan.myframework.view.modle.response.BangumiRecommendedModle;
import com.xuan.myframework.view.modle.response.BroadcastTableModle;
import com.xuan.myframework.view.modle.response.ChaseBangumiModle;
import com.xuan.myframework.view.modle.response.ClassificationModle;
import com.xuan.myframework.view.modle.response.CommentModle;
import com.xuan.myframework.view.modle.response.LiveDataModle;
import com.xuan.myframework.view.modle.response.RecommendBannerModle;
import com.xuan.myframework.view.modle.response.RecommendModle;
import com.xuan.myframework.view.modle.response.UserDetailsModle;
import com.xuan.myframework.view.modle.response.VideoDetailsModle;
import com.xuan.myframework.view.modle.response.VideoPlayerModle;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;

/**
 * Created by xuan on 2017/5/10.
 */

public interface CacheProviders {
    long duration=3;

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<LiveDataModle> queryLiveData(Observable<LiveDataModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<String> queryLivePlayerData(Observable<String> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<UserDetailsModle> queryUserDetails(Observable<UserDetailsModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<RecommendBannerModle> queryRecommendBannerData(Observable<RecommendBannerModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<RecommendModle> queryRecommendData(Observable<RecommendModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<VideoDetailsModle> queryVideoDetails(Observable<VideoDetailsModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<CommentModle> queryCommentData(Observable<CommentModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<VideoPlayerModle> queryVideoPlayerData(Observable<VideoPlayerModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<BangumiRecommendedModle> queryBangumiRecommendedData(Observable<BangumiRecommendedModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<BangumiModle> queryBangumiData(Observable<BangumiModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<ChaseBangumiModle> queryChaseBangumiData(Observable<ChaseBangumiModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<BroadcastTableModle> queryBroadcastTableData(Observable<BroadcastTableModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<BangumiIndexModle> queryBangumiIndexData(Observable<BangumiIndexModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<BangumiDetailModle> queryBangumiDetailsData(Observable<BangumiDetailModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<BangumiDetailsRecommendedModle> queryBangumiDetailsRecommendData(Observable<BangumiDetailsRecommendedModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<BangumiCommentsModle> queryBangumiDetailsCommentsData(Observable<BangumiCommentsModle> o, EvictProvider provider);

    @LifeCache(duration = duration, timeUnit = TimeUnit.MINUTES)
    Observable<ClassificationModle> queryClassificationData(Observable<ClassificationModle> o, EvictProvider provider);
}
