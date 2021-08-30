package com.lingong.mytest.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.databinding.ActivityMainBinding;
import com.lingong.mytest.inter.OnSoftKeyBoardChangeListener;
import com.lingong.mytest.inter.SoftKeyBoardListener;
import com.lingong.mytest.utils.LogUtil;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static final String TAG = "MainActivity";
    String url = "https://www.baidu.cn/";
    String testUrl = "https://m.speedtest.cn/";
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initWebSetting();
//        binding.webView.setWebViewClient(new SslWebViewClient(this, webView, certFilePath) {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                binding.webView.setVisibility(View.VISIBLE);
//            }
//        });

        binding.webView.addJavascriptInterface(this, "android");
        binding.webView.loadUrl(testUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting() {
        // webSettings
        WebSettings webSettings =  binding.webView.getSettings();
        // 允许JS代码
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 设置5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        // 允许Dom缓存
        webSettings.setDomStorageEnabled(true);
        // 允许App缓存
        webSettings.setAppCacheEnabled(true);
        // 允许访问File
        webSettings.setAllowFileAccess(true);
        // 允许自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 设置布局算法
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        // 是否允许自动播放音视频
        webSettings.setMediaPlaybackRequiresUserGesture(false);
    }

//
//    @JavascriptInterface
//    public String getAndroidInfo() {
//        LogUtil.d("getAndroidInfo...");
//        AndroidInfo androidInfo = new AndroidInfo();
//        androidInfo.setCategoryid("1");
//        androidInfo.setMac("wl:wl:wl:wl:wl:wl");
//        androidInfo.setCountry(Locale.getDefault().getCountry());
//        androidInfo.setDeviceid("1");
//        androidInfo.setProfileid("1");
//        Gson gson = new Gson();
//        String json = gson.toJson(androidInfo);
//        LogUtil.d("json: " + json);
//        return json;
//    }

    @JavascriptInterface
    public void exitApp() {
        LogUtil.d("exitApp...");
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            LogUtil.d("dispatchKeyEvent...  keyCode: " + keyCode);
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                sendVirtualKey(KeyEvent.KEYCODE_DEL);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void sendVirtualKey(int keyCode) {
        LogUtil.d("sendVirtualKey...  keyCode: " + keyCode);
        try {
            String keyCommand = "input keyevent " + keyCode;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(keyCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}