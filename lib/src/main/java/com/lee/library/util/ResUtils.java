package com.lee.library.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lee on 2015/11/12.
 */
public class ResUtils {
    private static Context mContext;
    private static Resources mResources;

    public static void init(Context context) {
        mContext = context;
    }

    public static String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    public static String getString(int resId, Object... formatArgs){
        return mContext.getResources().getString(resId, formatArgs);
    }

    public static int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }

    public static int getDimensionPixelSize(int resId) {
        return mContext.getResources().getDimensionPixelSize(resId);
    }

    public static int getDimensionPixelOffset(int resId) {
        return mContext.getResources().getDimensionPixelOffset(resId);
    }

    public static String[] getStringArray(int resId) {
        return mContext.getResources().getStringArray(resId);
    }

    /**
     * get an asset using ACCESS_STREAMING mode. This provides access to files that have been bundled with an
     * application as assets -- that is, files placed in to the "assets" directory.
     *
     * @param fileName The name of the asset to open. This name can be hierarchical.
     * @return
     */
    public static String geFileFromAssets(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(mContext.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
