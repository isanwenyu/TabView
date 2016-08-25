package com.isanwenyu.tabview;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * <pre>
 * 适配工具类
 * Copyright (c) 2016 isanwenyu@163.com. All rights reserved.
 * </pre>
 */
public class Util {

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

}
