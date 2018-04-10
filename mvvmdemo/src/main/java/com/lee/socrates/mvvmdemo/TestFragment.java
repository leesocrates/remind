package com.lee.socrates.mvvmdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lee.library.fragment.BaseFragment;
import com.lee.library.view.banner.Banner;
import com.lee.library.view.banner.BannerConfig;
import com.lee.library.view.banner.loader.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lee on 2018/4/3.
 */

public class TestFragment extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_bitmap;
    }

    @Override
    public void initView() {
        Banner banner = rootView.findViewById(R.id.banner);
        String[] urls = new String[]{"http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg",
                "http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg"};
        List list = Arrays.asList(urls);
        banner.setIndicatorGravity(BannerConfig.CENTER)
                .setImages(list)
                .setImageLoader(new PicassoImageLoader())
                .start();
    }

    private class PicassoImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Picasso.with(context).load(Uri.parse((String) path)).into(imageView);
        }
    }

}
