package com.lee.library.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by lee on 2015/11/12.
 */
public class RestfulConfig {

    public static final int DEFAULT_TIMEOUT = 30;//默认超时时间(秒)
//    public static final String BASE_URL = "http://101.37.39.146:8080/";
public static final String BASE_URL = "http://172.30.66.77:8080/";
    private static OkHttpClient httpClient = generatorHttpClient();
    public static final Retrofit retrofit = generatorRetrofit();

    private static OkHttpClient generatorHttpClient() {
        HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(logLevel);
        return new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
    }

    private static Retrofit generatorRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(SpecialConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
