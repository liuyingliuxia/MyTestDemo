package com.lingong.mytest.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author Ben
 * @description:
 * @date :2021/10/28 4:12 PM
 */
public class SnUtil {

    static String TAG = "SnUtil";

    _ShellUtils.CommandResult commandResult;

    private static final String path = "cat /sys/bus/mmc/devices/";

    private static final String awkCmd = "ls /sys/bus/mmc/devices | awk '{print $0}' | head -n 1";

    private static final String busyboxCmd = "ls /sys/bus/mmc/devices | busybox awk '{print $0}' | head -n 1";

    private static final String STATUS_ERROR = "STATUS_ERROR";

    /**
     * 定位到flash dir的路径
     *
     * @return dir or STATUS_ERROR
     */
    private static String loadSerialDir() {
       _ShellUtils.CommandResult commandResult = null;
        String[] cmds = new String[]{awkCmd, busyboxCmd};

        for (String cmd : cmds) {
            commandResult = _ShellUtils.execCommand(cmd, false);
            Log.d(TAG, cmd);
            if (null != commandResult) {
                if (!TextUtils.isEmpty(commandResult.successMsg)) {
                    return commandResult.successMsg;
                }
            }
        }
        return STATUS_ERROR;
    }

    /**
     * 获取Flash id
     *
     * @return
     */
    public static String loadFlashId() {
       _ShellUtils.CommandResult commandResult = null;

        String serialDir = loadSerialDir();
        if (STATUS_ERROR.equals(serialDir)) {
            return STATUS_ERROR;
        }

        String catCmd = path + serialDir + "/cid";
        LogUtil.d("loadFlashId catCmd = " + catCmd);
        commandResult =_ShellUtils.execCommand(catCmd, false);

        if (null != commandResult) {
            return commandResult.successMsg;
        }
        return STATUS_ERROR;
    }


}
