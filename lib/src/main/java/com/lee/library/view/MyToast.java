package com.lee.library.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.library.R;

/**
 * Created by socrates on 2015/11/22.
 */
public class MyToast extends Toast {

    private TextView messageView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MyToast(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_my_toast, null);
        messageView = (TextView) view.findViewById(R.id.message);
        setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        setView(view);
    }

    public void setMessage(String message){
        messageView.setText(message);
    }
}
