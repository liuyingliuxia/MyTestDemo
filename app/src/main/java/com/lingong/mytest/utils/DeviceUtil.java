package com.lingong.mytest.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by ben_zhao on 2017/11/13.
 */

public class DeviceUtil {

  /**
   * eth0 MAC地址获取，适用api9 - api24
   */
  public static String getEth0Mac(String type) {

    String Mac = "";
    try {
      List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface nif : all) {
        LogUtil.d("getEth0Mac .... nif.getName() " + nif.getName());
        if (!nif.getName().equalsIgnoreCase(type)) continue;

        byte[] macBytes = nif.getHardwareAddress();
        LogUtil.e("macBytes size= " + macBytes.length + " macBytes = " +macBytes.toString());
        if (macBytes == null) {
          return "Didn\'t get "+type+ " MAC address";
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
      LogUtil.e("getEth0Mac ==== " +ex.getMessage());
    }
    return "Didn\'t get "+type+ " MAC address";
  }

  /**
   * Get IP address from first non-localhost interface
   *
   * @return address or empty string
   */
  public static String getIPAddress(boolean useIPv4) {
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress();
            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
            boolean isIPv4 = sAddr.indexOf(':') < 0;

            if (useIPv4) {
              if (isIPv4)
                return sAddr;
            } else {
              if (!isIPv4) {
                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
              }
            }
          }
        }
      }
    } catch (Exception ex) {
    } // for now eat exceptions
    return "";
  }

  public static String getAndroidId(Context context){

    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }
  public static String getTotalMemory(Context context) {
    String str1 = "/proc/meminfo";// 系统内存信息文件
    String str2;
    String[] arrayOfString;
    long initial_memory = 0;

    try {
      FileReader localFileReader = new FileReader(str1);
      BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
      str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统内存大小
      arrayOfString = str2.split("\\s+");
      for (String num : arrayOfString) {
        Log.i(str2, num + "\t");
      }
      initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位KB
      localBufferedReader.close();
    } catch (IOException e) {

    }
    return Formatter.formatFileSize(context, initial_memory);


  }
}
