package com.lingong.mytest.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

}