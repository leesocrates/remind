package com.lee.socrates.mvvmdemo;

import android.databinding.ObservableField;
import android.view.View;

/**
 * Created by lee on 2018/3/16.
 */

public class MainActivityViewModel {

    public final ObservableField<String> title = new ObservableField<>();

    public void onClick(View view){
        title.set("after change");
    }
}
