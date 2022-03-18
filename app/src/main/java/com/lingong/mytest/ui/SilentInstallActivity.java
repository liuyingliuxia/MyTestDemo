package com.lingong.mytest.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.databinding.ActivityCountryBinding;
import com.lingong.mytest.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SilentInstallActivity extends AppCompatActivity {
    private ActivityCountryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AndroidPAppInstaller(this ,"/storage/emulated/0/cn.jj.tv.apk" );

    }


    /**
     * 适配AndroidP 的静默安装
     */

    /**
     * 适配AndroidP 的静默安装
     */
    public static void AndroidPAppInstaller(Context context, String filePath) {
        LogUtil.d("AndroidPAppInstaller start ...  !");
        File apkFile = new File(filePath);
        long apkFileLength = apkFile.length();

        PackageManager pm = context.getPackageManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final PackageInstaller packageInstaller;
            packageInstaller = pm.getPackageInstaller();
            packageInstaller.registerSessionCallback(new PackageInstaller.SessionCallback() {
                @Override
                public void onCreated(int sessionId) {
                    LogUtil.d("Install Start sessionId-> " + sessionId);
                }

                @Override
                public void onBadgingChanged(int sessionId) {
                }

                @Override
                public void onActiveChanged(int sessionId, boolean active) {
                }

                @Override
                public void onProgressChanged(int sessionId, float progress) {
                }

                @Override
                public void onFinished(int sessionId, boolean success) {

                    packageInstaller.unregisterSessionCallback(this);
//                    int resultCode = success ? 1 : -1;
                    LogUtil.d("AndroidPAppInstaller = sessionId:" + sessionId + " resultCode:" + success);

//                    sendInstallState(context, resultCode, pkgName, "");
                }
            }, new Handler(Looper.myLooper()));

            int count;
            int sessionId;
            byte[] buffer = new byte[65536];

            InputStream inputStream;
            OutputStream outputStream;
            PackageInstaller.Session session = null;
            PackageInstaller.SessionParams sessionParams;

            sessionParams = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            try {
                sessionId = packageInstaller.createSession(sessionParams);
                session = packageInstaller.openSession(sessionId);
                inputStream = new FileInputStream(apkFile);
                outputStream = session.openWrite(apkFile.getName(), 0, apkFileLength);

                while ((count = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                    float progress = ((float) count / (float) apkFileLength);
                    session.setStagingProgress(progress);  //传入进度数据
                }
                session.fsync(outputStream);    //把outStream的数据都传入session中

                inputStream.close();
                outputStream.flush();
                outputStream.close();

                Intent intent = new Intent();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                session.commit(pendingIntent.getIntentSender());
            } catch (IOException e) {
                if (session != null) {
                    session.abandon();  //安装结束，弃用session
                }
                e.printStackTrace();
            } finally {

            }
        }
    }


}
