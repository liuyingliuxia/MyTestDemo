package com.lingong.mytest.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * android 12 上无法成功执行
 * @deprecated
 */
public class ShellUtils {

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    public static final String COMMAND_HDMI_STATE_1 = "echo 1 > /sys/devices/platform/wireless-wlan/hdmi_state \n";
    public static final String COMMAND_GET_ETH0_MAC = "cat /sys/class/net/eth0/address \n";

    public static void flip() {
        //String cmd_disable ="content insert --uri content://settings/system --bind name:s:accelerometer_rotation --bind value:i:0"; //diable auto
        String cmd = "echo 1 > /sys/devices/platform/wireless-wlan/hdmi_state";
        try {
            //java.lang.Process  proc = Runtime.getRuntime().exec(new String[]{"sh","-c",cmd_disable});
            //proc.waitFor();
            Process proc = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String execRootCmd(String shellCmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("/system/xbin/su");
//            Process p = Runtime.getRuntime().exec("sh");
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            dos.writeBytes(shellCmd);
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            while ((line = br.readLine()) != null) {
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String exeAdbShell(String cmd) {
        Runtime mRuntime = Runtime.getRuntime();
        try {
            //Process中封装了返回的结果和执行错误的结果
            Process mProcess = mRuntime.exec(cmd);
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            StringBuffer mRespBuff = new StringBuffer();
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = mReader.read(buff)) != -1) {
                mRespBuff.append(buff, 0, ch);
            }
            mReader.close();
            LogUtil.d("shell result = " + mRespBuff.toString());
            return mRespBuff.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String execCmd(String shellCmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
//            Process p = Runtime.getRuntime().exec("/system/xbin/su");
//            Process p = Runtime.getRuntime().exec("sh");
//            dos = new DataOutputStream(p.getOutputStream());
//            dis = new DataInputStream(p.getInputStream());

            dos.writeBytes(shellCmd);
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            while ((line = br.readLine()) != null) {
                result += line;
            }
//            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}