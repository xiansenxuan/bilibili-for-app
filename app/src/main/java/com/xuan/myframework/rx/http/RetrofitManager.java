package com.xuan.myframework.rx.http;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xuan.myframework.application.MyApplication;
import com.xuan.myframework.view.modle.response.BangumiCommentsModle;
import com.xuan.myframework.view.modle.response.BangumiDetailsRecommendedModle;
import com.xuan.myframework.view.modle.response.BangumiDetailModle;
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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by xuan on 2017/5/10.
 */

public class RetrofitManager {
    public static final String APP_BASE_URL = "http://app.bilibili.com/";
    public static final String LIVE_BASE_URL = "http://live.bilibili.com/";
    public static final String ACCOUNT_BASE_URL = "https://account.bilibili.com/";
    public static final String API_BASE_URL = "http://api.bilibili.cn/";
    public static final String BILI_GO_BASE_URL = "http://bilibili-service.daoapp.io/";
    public static final String BANGUMI_BASE_URL = "http://bangumi.bilibili.com/";
    public static final String USER_BASE_URL = "http://space.bilibili.com/";

    private final static int default_timeout = 5000;

    public static final String platform = "platform";

    private volatile static RetrofitManager instance;

    private Context context;
    private Retrofit.Builder retrofitBuilder;
    private Retrofit.Builder stringRetrofitBuilder;
    private CacheProviders cache;


    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    private RetrofitManager() {
        context = MyApplication.getInstance();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.e(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(default_timeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new HeadInterceptor());

        OkHttpClient client = builder.build();

        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client);

        stringRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client);


        cache = new RxCache.Builder()
                .persistence(context.getFilesDir(), new GsonSpeaker())
                .using(CacheProviders.class);


    }

    private <T> T createApi(Class<T> clazz, String baseUrl) {
        return retrofitBuilder.baseUrl(baseUrl).build().create(clazz);
    }

    private <T> T createApi(Class<T> clazz, String baseUrl, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(baseUrl).build().create(clazz);
    }

    private <T> void toSubscribe(Observable<T> observable, Consumer<T> consumer, Consumer<Throwable> throwable, LifecycleTransformer lifecycleTransformer) {
        observable.compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, throwable);

    }

///////////////////////////////////////////////////////////////////////////////////////////


    public void queryLiveData(boolean evict, Consumer<LiveDataModle> consumer,
                              Consumer<Throwable> throwable,
                              LifecycleTransformer lifecycleTransformer) {

        toSubscribe(
                cache.queryLiveData(createApi(ApiServer.class, LIVE_BASE_URL).queryLiveData()
                                .map(new SeparationResultFunction<LiveDataModle>())
                        , new EvictProvider(evict))
                , consumer, throwable
                , lifecycleTransformer);

    }

    public void queryLivePlayerData(int cid, boolean evict, Consumer<String> consumer,
                                    Consumer<Throwable> throwable
            , LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryLivePlayerData(createApi(ApiServer.class, LIVE_BASE_URL, stringRetrofitBuilder)
                        .queryLivePlayerData(cid)
                , new EvictProvider(evict))
                , consumer, throwable, lifecycleTransformer);

    }

    public void queryUserDetails(int mid, boolean evict, Consumer<UserDetailsModle> consumer,
                                 Consumer<Throwable> throwable,
                                 LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryUserDetails(createApi(ApiServer.class,ACCOUNT_BASE_URL).queryUserDetails(mid),
                new EvictProvider(evict)),
                consumer,throwable,lifecycleTransformer);
    }

    public Observable<RecommendBannerModle> queryRecommendBannerData(boolean evict) {
        return cache.queryRecommendBannerData(createApi(ApiServer.class,APP_BASE_URL).queryRecommendBannerData(),
                new EvictProvider(evict));

    }

    public Observable<RecommendModle> queryRecommendedData(boolean evict) {

        return cache.queryRecommendData(createApi(ApiServer.class,APP_BASE_URL).queryRecommendData(),
                new EvictProvider(evict));
    }

    public void queryVideoDetails(int aid, boolean evict, Consumer<VideoDetailsModle> consumer,
                                 Consumer<Throwable> throwable,
                                 LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryVideoDetails(createApi(ApiServer.class, APP_BASE_URL).queryVideoDetails(aid)
                .map(new SeparationResultFunction<VideoDetailsModle>()),
                new EvictProvider(evict)),
                consumer, throwable, lifecycleTransformer);
    }

    public void queryCommentData(int aid,int pageNum,int pageSize,int ver, boolean evict, Consumer<CommentModle> consumer,
                                 Consumer<Throwable> throwable,
                                 LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryCommentData(createApi(ApiServer.class, API_BASE_URL).queryCommentData(aid,pageNum,pageSize,ver),
                new EvictProvider(evict)),
                consumer, throwable, lifecycleTransformer);
    }

    public void queryVideoPlayerData(int cid,int quailty,String type,boolean evict, Consumer<VideoPlayerModle> consumer,
                                 Consumer<Throwable> throwable,
                                 LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryVideoPlayerData(createApi(ApiServer.class, BILI_GO_BASE_URL).queryVideoPlayerData(cid,quailty,type),
                new EvictProvider(evict)),
                consumer, throwable, lifecycleTransformer);
    }

    public Observable<BangumiModle> queryBangumiData(boolean evict,
                              LifecycleTransformer lifecycleTransformer) {

        return cache.queryBangumiData(createApi(ApiServer.class, BANGUMI_BASE_URL).queryBangumiData()
                , new EvictProvider(evict)).compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public Observable<BangumiRecommendedModle> queryBangumiRecommendedData(boolean evict,
                              LifecycleTransformer lifecycleTransformer) {

        return cache.queryBangumiRecommendedData(createApi(ApiServer.class, BANGUMI_BASE_URL).queryBangumiRecommendedData()
                , new EvictProvider(evict)).compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public void queryChaseBangumiData(int mid,boolean evict, Consumer<ChaseBangumiModle> consumer,
                                     Consumer<Throwable> throwable,
                                     LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryChaseBangumiData(createApi(ApiServer.class, USER_BASE_URL).queryChaseBangumiData(mid),
                new EvictProvider(evict)),
                consumer, throwable, lifecycleTransformer);
    }

    public void queryBroadcastTableData(boolean evict, Consumer<BroadcastTableModle> consumer,
                                     Consumer<Throwable> throwable,
                                     LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryBroadcastTableData(createApi(ApiServer.class, BANGUMI_BASE_URL).queryBroadcastTableData(),
                new EvictProvider(evict)),
                consumer, throwable, lifecycleTransformer);
    }

    public void queryBangumiIndexData(boolean evict, Consumer<BangumiIndexModle> consumer,
                                     Consumer<Throwable> throwable,
                                     LifecycleTransformer lifecycleTransformer) {
        toSubscribe(cache.queryBangumiIndexData(createApi(ApiServer.class, BANGUMI_BASE_URL).queryBangumiIndexData(),
                new EvictProvider(evict)),
                consumer, throwable, lifecycleTransformer);
    }


    public Observable<BangumiDetailModle> queryBangumiDetailsData(boolean evict,
                                                                  LifecycleTransformer lifecycleTransformer) {

        return cache.queryBangumiDetailsData(createApi(ApiServer.class, BANGUMI_BASE_URL).queryBangumiDetailsData()
                , new EvictProvider(evict)).compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<BangumiDetailsRecommendedModle> queryBangumiDetailsRecommendData(boolean evict,
                                                                                       LifecycleTransformer lifecycleTransformer) {

        return cache.queryBangumiDetailsRecommendData(createApi(ApiServer.class, BANGUMI_BASE_URL).queryBangumiDetailsRecommendData()
                , new EvictProvider(evict)).compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<BangumiCommentsModle> queryBangumiDetailsCommentsData(boolean evict,
                                                                            LifecycleTransformer lifecycleTransformer) {

        return cache.queryBangumiDetailsCommentsData(createApi(ApiServer.class, API_BASE_URL).queryBangumiDetailsCommentsData()
                , new EvictProvider(evict)).compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<ClassificationModle> queryClassificationData(boolean evict,Observable<ClassificationModle> o,
                                                                            LifecycleTransformer lifecycleTransformer) {

        return cache.queryClassificationData(o
                , new EvictProvider(evict)).compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

///////////////////////////////////////////////////////////////////////////////////////////
}


class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authorised = originalRequest.newBuilder()
                .header(RetrofitManager.platform, "Android")//设备
                .build();


        return chain.proceed(authorised);
    }
}