package com.xuan.myframework.rx.http;

import com.xuan.myframework.base.modle.BaseModle;
import com.xuan.myframework.view.modle.response.BangumiCommentsModle;
import com.xuan.myframework.view.modle.response.BangumiDetailsRecommendedModle;
import com.xuan.myframework.view.modle.response.BangumiDetailModle;
import com.xuan.myframework.view.modle.response.BangumiIndexModle;
import com.xuan.myframework.view.modle.response.BangumiModle;
import com.xuan.myframework.view.modle.response.BangumiRecommendedModle;
import com.xuan.myframework.view.modle.response.BroadcastTableModle;
import com.xuan.myframework.view.modle.response.ChaseBangumiModle;
import com.xuan.myframework.view.modle.response.CommentModle;
import com.xuan.myframework.view.modle.response.LiveDataModle;
import com.xuan.myframework.view.modle.response.RecommendBannerModle;
import com.xuan.myframework.view.modle.response.RecommendModle;
import com.xuan.myframework.view.modle.response.UserDetailsModle;
import com.xuan.myframework.view.modle.response.VideoDetailsModle;
import com.xuan.myframework.view.modle.response.VideoPlayerModle;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by xuan on 2017/5/10.
 */

public interface ApiServer {

//    @GET("AppIndex/home?" +
//                    "_device=android&_hwid=51e96f5f2f54d5f9&_ulv=10000&" +
//                    "access_key=563d6046f06289cbdcb472601ce5a761&appkey=c1b107428d337928&build=410000&" +
//                    "platform=android&scale=xxhdpi&sign=fbdcfe141853f7e2c84c4d401f6a8758")
//    Observable<LiveDataModle> queryLiveData();

    @GET("AppIndex/home?" +
                    "_device=android&_hwid=51e96f5f2f54d5f9&_ulv=10000&" +
                    "access_key=563d6046f06289cbdcb472601ce5a761&appkey=c1b107428d337928&build=410000&" +
                    "platform=android&scale=xxhdpi&sign=fbdcfe141853f7e2c84c4d401f6a8758")
    Observable<BaseModle<LiveDataModle>> queryLiveData();

    /**
     * 直播url
     */
    @GET("api/playurl?player=1&quality=0")
    Observable<String> queryLivePlayerData(@Query("cid") int cid);

    /**
     * 用户详情数据
     */
    @GET("api/member/getCardByMid")
    Observable<UserDetailsModle> queryUserDetails(@Query("mid") int mid);

    /**
     * 首页推荐banner
     */
    @GET("x/banner?plat=4&build=411007&channel=bilih5")
    Observable<RecommendBannerModle> queryRecommendBannerData();

    /**
     * 首页推荐数据
     */
    @GET("x/show/old?platform=android&device=&build=412001")
    Observable<RecommendModle> queryRecommendData();

    /**
     * 视频详情数据
     */
    @GET(
            "x/view?access_key=19946e1ef3b5cad1a756c475a67185bb&actionKey=appkey&appkey=27eb53fc9058f8c3&build=3940&device=phone&mobi_app=iphone&platform=ios&sign=1206255541e2648c1badb87812458046&ts=1478349831")
    Observable<BaseModle<VideoDetailsModle>> queryVideoDetails(@Query("aid") int aid);

    /**
     * 评论
     */
    @GET("feedback")
    Observable<CommentModle> queryCommentData(@Query("aid") int aid,
                                                         @Query("page") int page, @Query("pagesize") int pageSize, @Query("ver") int ver);

    /**
     * b站高清视频
     * quailty:清晰度(1~2，根据视频有不同)
     * type: 格式(mp4/flv)
     */
    @GET("/video/{cid}")
    Observable<VideoPlayerModle> queryVideoPlayerData(@Path("cid") int cid,
                                               @Query("quailty") int quailty,
                                               @Query("type") String type);

    /**
     * 首页番剧
     */
    @GET("api/app_index_page_v4?build=3940&device=phone&mobi_app=iphone&platform=ios")
    Observable<BangumiModle> queryBangumiData();

    /**
     * 首页番剧推荐
     */
    @GET(
            "api/bangumi_recommend?access_key=f5bd4e793b82fba5aaf5b91fb549910a&actionKey=appkey&appkey=27eb53fc9058f8c3&build=3470&cursor=0&device=phone&mobi_app=iphone&pagesize=10&platform=ios&sign=56329a5709c401d4d7c0237f64f7943f&ts=1469613558")
    Observable<BangumiRecommendedModle> queryBangumiRecommendedData();



    /**
     * 用户追番
     */
    @GET("ajax/Bangumi/getList")
    Observable<ChaseBangumiModle> queryChaseBangumiData(@Query("mid") int mid);

    /**
     * 番剧时间表
     */
    @GET(
            "api/timeline_v4?access_key=19946e1ef3b5cad1a756c475a67185bb&actionKey=appkey&appkey=27eb53fc9058f8c3&area_id=2&build=3940&device=phone&mobi_app=iphone&platform=ios&see_mine=0&sign=d8cbbacab2e5fd0196b4883201e2103e&ts=1477981526")
    Observable<BroadcastTableModle> queryBroadcastTableData();

    /**
     * 番剧索引
     */
    @GET(
            "api/bangumi_index_cond?access_key=19946e1ef3b5cad1a756c475a67185bb&actionKey=appkey&appkey=27eb53fc9058f8c3&build=3940&device=phone&mobi_app=iphone&platform=ios&sign=cfc6903a13ba89e81c904b4c589e5369&ts=1477974966&type=0")
    Observable<BangumiIndexModle> queryBangumiIndexData();



    /**
     * 番剧详情
     */
    @GET(
            "api/season_v4?access_key=19946e1ef3b5cad1a756c475a67185bb&actionKey=appkey&appkey=27eb53fc9058f8c3&build=3940&device=phone&mobi_app=iphone&platform=ios&season_id=5070&sign=3e5d4d7460961d9bab5da2341fd98dc1&ts=1477898526&type=bangumi")
    Observable<BangumiDetailModle> queryBangumiDetailsData();

    /**
     * 番剧详情番剧推荐
     */
    @GET(
            "api/season/recommend/5070.json?access_key=19946e1ef3b5cad1a756c475a67185bb&actionKey=appkey&appkey=27eb53fc9058f8c3&build=3940&device=phone&mobi_app=iphone&platform=ios&season_id=5070&sign=744e3a3f52829e4344c33908f7a0c1ef&ts=1477898527")
    Observable<BangumiDetailsRecommendedModle> queryBangumiDetailsRecommendData();


    /**
     * 番剧详情评论
     */
    @GET(
            "x/v2/reply?_device=iphone&_hwid=c84c067f8d99f9d3&_ulv=10000&access_key=19946e1ef3b5cad1a756c475a67185bb&appkey=27eb53fc9058f8c3&appver=3940&build=3940&nohot=0&oid=5189987&platform=ios&pn=1&ps=20&sign=c3b059e907f5c1d3416daa9fcc6396bf&sort=0&type=1")
    Observable<BangumiCommentsModle> queryBangumiDetailsCommentsData();

}
