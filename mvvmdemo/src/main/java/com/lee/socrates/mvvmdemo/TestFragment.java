package com.lee.socrates.mvvmdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.library.fragment.BaseFragment;

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

    }
}
