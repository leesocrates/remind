package com.lee.library.util;

import java.util.List;

/**
 * Created by lee on 2015/11/12.
 */
public class CollectionUtils {

    public static int getSize(List list){
        return list == null ? 0 : list.size();
    }

    public static boolean isNull(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

}
