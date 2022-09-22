package com.lingong.mytest.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben_zhao on 2019/4/1.
 */

public class DeepLinkUtil {

    /**
     * 统一打开应用方式
     * @param context
     * @param deeplink
     */
    public static void dispatchDeeplink(Context context, String deeplink) {

        try {
            if (TextUtils.isEmpty(deeplink)) {
                Log.d("DeepLinkUtil", "deeplink toString is empty");
                return;
            }else {
                Log.d("DeepLinkUtil === ", deeplink);
            }
            Intent intent = new Intent();

            JSONObject object = new JSONObject(deeplink);
            if (object.has("action")) {
                String action = object.getString("action");
                if (!TextUtils.isEmpty(action)) {
                    intent.setAction(action);
                }
            }
            String pkgName = null;
            if (object.has("pkg"))
                pkgName = object.getString("pkg");
            if (TextUtils.isEmpty(pkgName)) {
                Log.d("DeepLinkUtil", "pkgName is null");
                return;
            }
            if (!isAvailable(context, pkgName)) {
                Log.d("DeepLinkUtil", "Application with package name '" + pkgName + "' is not installed");
                return;
            }
            String className = null;
            if (object.has("className"))
                className = object.getString("className");
            if (!TextUtils.isEmpty(className)) {
                intent.setClassName(pkgName, className);
            } else {
                intent.setClassName(pkgName, getClassName(context, pkgName));
            }
            if (object.has("data")) {
                JSONObject data = object.getJSONObject("data");
                if (null != data) {
                    String uriStr = data.getString("uri");
                    if (!TextUtils.isEmpty(uriStr)) {
                        intent.setData(Uri.parse(uriStr));
                    }
                }
            }
            if (object.has("extra")) {
                JSONArray extra = object.getJSONArray("extra");
                for (int i = 0; i < extra.length(); i++) {
                    String key = extra.getJSONObject(i).getString("key");
                    Serializable value = (Serializable) extra.getJSONObject(i).get("value");

                    Log.d("DeepLinkUtil", "value " + value.getClass());
                    intent.putExtra(key, value);
                }

            }
            if (object.has("flag")) {
                int flag = object.getInt("flag");
                if (flag != 0)
                    intent.setFlags(flag);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getClassName(Context context, String pkgName) {

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.setPackage(pkgName);
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String className = resolveinfo.activityInfo.name;
            if (!TextUtils.isEmpty(className))
                return className;
            else
                return "";
        }
        return "";
    }

    /**
     * 检查是否安装了指定的app
     */
    public static boolean isAvailable(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
