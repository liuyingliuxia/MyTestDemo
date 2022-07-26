package com.lingong.mytest.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class NetworkUtils {

    /**
     * 检测是否有网络
     */
    public static boolean isNetworkOpen(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            try {//志源 512M的板子会报没有网络权限，导致闪退
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public final static String ETH0_MAC_WRONG = "Didn't get eth0 MAC address";
    public final static String WLAN0_MAC_WRONG = "Didn't get Wlan0 MAC address";
    final static String DEFAULT_MAC = "00:30:1b:ba:02:db";

    /**
     * 取得 Mac-Address
     * 1. 先取得 eth0 Mac-Address
     * 2. 再取得 wlan0 Mac-Address
     * Android 11 以上的板子需要uid才能获取mac地址，或者降低target sdk
     * @return
     */
    public static String getDeviceMacAddress() {

        String mac = !getEth0Mac().equals(ETH0_MAC_WRONG) ? getEth0Mac() : getWlan0Mac();
        Log.d("Mac", "Mac eth0 =" + getEth0Mac());
        Log.d("Mac", "Mac wlan0 =" + getWlan0Mac());

        return mac;

    }

    /**
     * wlan0 MAC地址获取，适用api9 - api24
     */
    public static String getWlan0Mac() {

        String Mac = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return WLAN0_MAC_WRONG;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    Mac = res1.toString();
                }
                return Mac;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return WLAN0_MAC_WRONG;
    }

    /**
     * eth0 MAC地址获取，适用api9 - api24
     */
    public static String getEth0Mac() {

        String Mac = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("eth0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return ETH0_MAC_WRONG;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    Mac = res1.toString();
                }
                return Mac;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ETH0_MAC_WRONG;
    }

}
