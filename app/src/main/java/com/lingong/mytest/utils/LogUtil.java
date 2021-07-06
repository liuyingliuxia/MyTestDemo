package com.lingong.mytest.utils;

import android.util.Log;

/**
 * Author:Miracle.lin
 * Date:2021/7/5 11:06
 * Descri:
 */
public class LogUtil {

    public static String TAG = "lin_gong:";
    public static boolean LOG_DEBUG = true;

    public static void d(String msg) {
        if (LOG_DEBUG) Log.d(TAG, msg);
    }

    public static void d(String head, String msg) {
        if (LOG_DEBUG) Log.d(TAG, head + " " + msg);
    }

    public static void e(String msg) {
        if (LOG_DEBUG) Log.e(TAG, msg);
    }

    public static void e(String head, String msg) {
        if (LOG_DEBUG) Log.e(TAG, head + " " + msg);
    }
}