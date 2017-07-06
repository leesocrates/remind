package com.lee.library.network

import com.lee.library.network.SpecialConverterFactory
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by lee on 2015/11/12.
 */
object RetrofitConfig {

    val DEFAULT_TIMEOUT = 30//默认超时时间(秒)
    //    public static final String BASE_URL = "http://101.37.39.146:8080/";
    val BASE_URL = "http://172.30.66.77:8080/"
    private val httpClient = generatorHttpClient()
    //这里不加@JvmField注解，在java中调用的时候只能通过getRetrofit（）调用，加了注解后可以直接通过属性名引用
    @JvmField
    val retrofit = generatorRetrofit()

    private fun generatorHttpClient(): OkHttpClient {
        val logLevel = HttpLoggingInterceptor.Level.BODY
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = logLevel
        return OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS).build()
    }

    private fun generatorRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(SpecialConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}
