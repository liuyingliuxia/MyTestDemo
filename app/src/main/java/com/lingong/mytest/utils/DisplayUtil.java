package com.lingong.mytest.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.WindowMetrics;

/**
 * @Author: Miracle.Lin
 * @Date:2021/10/22
 * @Desc:
 */
 public class DisplayUtil {
    static public int getWindowWidth(Activity activity){
        WindowMetrics metrics;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {//android 30
            metrics = activity.getWindowManager().getCurrentWindowMetrics();
            return  metrics.getBounds().width();
        }else {
           return getWindowWidth_(activity);
        }
    }


    static public int getWindowHeight(Activity activity){
        WindowMetrics metrics;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {//android 30
            metrics = activity.getWindowManager().getCurrentWindowMetrics();
            return  metrics.getBounds().height();
        }else {
            return getWindowHeight_(activity);
        }
    }

    static public int getWindowDensity(Context activity){
        DisplayMetrics dm;
        dm = activity.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }


    public static int getWindowWidth_(Context context) {
        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getWindowHeight_(Context context) {
        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

}
