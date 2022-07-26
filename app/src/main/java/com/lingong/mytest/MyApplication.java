package com.lingong.mytest;

import android.app.Application;

import com.lingong.mytest.utils.LogUtil;
import com.lingong.mytest.utils.NetworkUtils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("MAC === " + NetworkUtils.getDeviceMacAddress());
    }
}
