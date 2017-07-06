package com.lee.library.network;

/**
 * Created by lee on 2015/11/12.
 *
 */
public class NetService<T> {

    private T mNetApi;

    public NetService(Class<T> tClass) {
        mNetApi = RetrofitConfig.retrofit.create(tClass);
    }

    public T getNetApi() {
        return mNetApi;
    }


}
