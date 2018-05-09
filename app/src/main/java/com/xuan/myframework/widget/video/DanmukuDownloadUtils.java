package com.xuan.myframework.widget.video;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * com.xuan.myframework.widget.video
 * Created by xuan on 2017/7/20.
 * version
 * desc
 */

public class DanmukuDownloadUtils {
    public static Observable<BaseDanmakuParser> downloadXML(final String uri) {
        return Observable.create(new ObservableOnSubscribe<BaseDanmakuParser>() {
            @Override
            public void subscribe(final ObservableEmitter<BaseDanmakuParser> emitter) throws Exception {
                if (TextUtils.isEmpty(uri)) {
                    emitter.onNext(new BaseDanmakuParser() {
                        @Override
                        protected IDanmakus parse() {
                            return new Danmakus();
                        }
                    });
                }


                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Logger.e(message);
                    }
                });
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(5000, TimeUnit.MILLISECONDS)
                        .retryOnConnectionFailure(true)
                        .addInterceptor(loggingInterceptor);

                OkHttpClient client = builder.build();

                Request request = new Request.Builder()
                        .url(uri)
                        .get()
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {


                            InputStream stream = new ByteArrayInputStream(BiliDanmukuCompressionTools.
                                    decompressXML(response.body().bytes()));

                            ILoader loader = DanmakuLoaderFactory.
                                    create(DanmakuLoaderFactory.TAG_BILI);
                            loader.load(stream);

                            BaseDanmakuParser parser = new BiliDanmukuParser();
                            assert loader != null;
                            IDataSource<?> dataSource = loader.getDataSource();
                            parser.load(dataSource);
                            emitter.onNext(parser);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        }).subscribeOn(Schedulers.io());

    }
}
