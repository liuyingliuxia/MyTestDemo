package com.lingong.mytest.util;

import android.os.AsyncTask;
import android.util.Log;

import com.lingong.mytest.utils.LogUtil;

import java.math.BigDecimal;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

/**
 * @Author: Miracle.Lin
 * @Date:2021/10/15
 * @Desc:
 */
public class SpeedTestTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {

        SpeedTestSocket speedTestSocket = new SpeedTestSocket();

        // add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is finished
                Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                Log.v("speedtest", "[COMPLETED] rate in M/s   : " + countSpeedUtilsFormat(report.getTransferRateBit().longValue()));
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                // called when a download/upload error occur
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
                Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                Log.v("speedtest", "[PROGRESS] rate in M/s   : " + countSpeedUtilsFormat(report.getTransferRateBit().longValue()));
            }
        });

        speedTestSocket.startDownload("https://uk-dev-resource.s3.amazonaws.com/apkfile/2021/4/26/com.zeasn.whale.open.launcher.technical/2217003/TV_OPEN_LAUNCHER_WHALE-chiptrip_rk3326_prod_[2.21.70.3]_sign.apk");
//        speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");
        return null;
    }

    /**
     * 单位换算
     */
    public String countSpeedUtilsFormat(long l) {
        Long speed_kb = l / 1024;
        Long speed_long = 0L;
        String speed;
        String utils;
        if (speed_kb < 1024) {
            speed_long = speed_kb;
            utils = "KB/s";
        } else {
            speed_long = speed_kb / 1024;
            utils = "MB/s";
        }
        BigDecimal b = new BigDecimal(speed_long);
        long l1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
        speed = l1 + utils;
        LogUtil.d("countSpeedUtilsFormat" + speed);
        return speed;
    }

}