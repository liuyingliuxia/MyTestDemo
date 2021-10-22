package com.lingong.mytest;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.lingong.mytest.utils.LogUtil;

/**
 * @Author: Miracle.Lin
 * @Date:2021/10/20
 * @Desc:
 */
public class CustomApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        LogUtil.d("CustomApplication build flavor = " + BuildConfig.FLAVOR + " version =" + BuildConfig.VERSION_NAME
                + " Android SDK_INT = " + Build.VERSION.SDK_INT );

    }

    public static Context getContext() {
        return context;
    }


}
