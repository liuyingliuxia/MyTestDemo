package com.lingong.mytest.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.databinding.ActivityMainBinding;
import com.lingong.mytest.inter.OnSoftKeyBoardChangeListener;
import com.lingong.mytest.inter.SoftKeyBoardListener;
import com.lingong.mytest.utils.DeepLinkUtil;
import com.lingong.mytest.utils.DeviceUtil;
import com.lingong.mytest.utils.LogUtil;

import java.util.Random;

/**
 * 不忘初心
 * <p>
 * ┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐  ┌┐    ┌┐    ┌┐
 * │Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  └┘    └┘    └┘
 * └───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘
 * ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
 * │~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
 * ├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
 * │ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
 * ├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
 * │ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
 * ├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
 * │ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
 * ├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
 * │ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
 * └─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static final String TAG = "MainActivity";
    private final static int MSG_CLEAN_START = 99;
    private int currentProcess;
    private Handler handler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CLEAN_START:
                    if (!((Activity) binding.getRoot().getContext()).isFinishing()) {
                        LogUtil.d("ringPb currentProcess = " + currentProcess);
//                        binding.ringPb.setAlpha((float) currentProcess / 100.00f);
//                        binding.ringPb.setProgress(currentProcess);
//                        binding.umeRingPv.setProgress((float) currentProcess / 500.00f);
//                        binding.ringDemo.setProgress(currentProcess);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SoftKeyBoardListener.setListener(this, new OnSoftKeyBoardChangeListener() {
                    @Override
                    public void keyBoardShow() {
                        LogUtil.d("keyBoardShow ... ");
                    }

                    @Override
                    public void keyBoardHide() {
                        LogUtil.d("keyBoardHide ... ");
                    }
                }

        );
        binding.btnTest1.setOnClickListener(v -> {
            binding.btnTest1.setText(DeviceUtil.getEth0Mac("eth0"));
//            binding.btnTest1.setText(DeviceUtil.getEth0Mac("eth0"));
//            Intent intent = new Intent("com.jmgo.action.SHOW_TOU");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

//            Intent broadIntent = new Intent("com.jmgo.ota.firmware");
//            broadIntent.putExtra( "firmware_version","1.0.66");
//            sendBroadcast(broadIntent);
        });

//        binding.btnTest2.setText("JMGO");
//        adb shell am start -n com.zeasn.whale.open.launcher.technical/
//        com.zeasn.whale.open.launcher.technical.ui.NewAppsDetailActivity --es package_name  xxxxxx(改成打开应用的包名)
        String dpLink = "{\n" +
                "  \"pkg\": \"com.ixigua.android.tv.wasu\",\n" +
                "  \"className\": \"com.ixigua.android.business.tvbase.base.app.schema.AdsAppActivity\",\n" +
                "  \"data\": {\n" +
                "    \"uri\": \"snssdk1840://detail/enter_detail\"\n" +
                "  },\n" +
                "  \"flag\": \"0\",\n" +
                "  \"action\": \"android.intent.action.VIEW\",\n" +
                "  \"extra\": [\n" +
                "    {\n" +
                "      \"key\": \"album_id\",\n" +
                "      \"value\": \"7138316314279576078\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"key\": \"episode_id\",\n" +
                "      \"value\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"key\": \"enter_from\",\n" +
                "      \"value\": \"openapk_phi\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        binding.btnTest2.setOnClickListener(v -> {
            DeepLinkUtil.dispatchDeeplink(v.getContext(), dpLink);
//            startApp(this);

//            startActivityByPkgClass(v.getContext(), "com.zeasn.launcher.projector",
//                    "com.zeasn.launcher.ui.MainActivity");

//            Intent intent = new Intent();
//            intent.setClassName("com.zeasn.launcher.projector",  "com.zeasn.launcher.ui.MainActivity");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

//            binding.btnTest2.setText(DeviceUtil.getEth0Mac("wlan0"));
//            Intent intent = new Intent();
//            intent.setClassName("com.jmgo.luna", "com.jmgo.luna.ui.JmgoFunActivity");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            Intent intent = new Intent("com.jmgo.action.SHOW_TOU");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("TouUrlNameConst" ,2);//1 主协议 ；2 副协议
//            startActivity(intent);
        });
//        binding.ringPb.setMax(500);
//        binding.ringPb.setProgress(0);
//        startTesting();
//        binding.ringPb.setRingColor(getResources().getColor(R.color.blue0D81F3));
    }


    //    包名:com.ixigua.android.tv.wasu
//    类名：com.ixigua.android.business.tvbase.base.app.schema.AdsAppActivity
//    key：open_url
//    value：snssdk1840://detail/enter_detail?album_id=7137535517692068389&episode_id=0&enter_from=openapk
    void startApp(Context context) {
        try {
            Intent setting = new Intent();
            setting.setClassName("com.ixigua.android.tv.wasu",
                    "com.ixigua.android.business.tvbase.base.app.schema.AdsAppActivity");
            setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            setting.putExtra("open_url", "snssdk1840://detail/enter_detail?album_id=7137535517692068389&episode_id=0&enter_from=openapk");
            context.startActivity(setting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 只有UI 效果
     */
    private void startTesting() {
        Random random = new Random();

        Thread thread = new Thread(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(600);
                    if (currentProcess < 500) {
                        currentProcess += random.nextInt(50);
                        if (!((Activity) this).isFinishing()) {
                            handler.sendEmptyMessage(MSG_CLEAN_START);
                        }
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void sendVirtualKey(int keyCode) {
        Log.v(TAG, "sendVirtualKey...  keyCode: " + keyCode);
        try {
            String keyCommand = "input keyevent " + keyCode;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(keyCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据包名类名 打开一个第三方应用里的页面
     * 需要加uid
     *
     * @param pkg
     * @param className
     */
    public static void startActivityByPkgClass(Context context, String pkg, String className) {
        try {
            Intent intent = new Intent();
            intent.setClassName(pkg, className);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
//            exception.printStackTrace();
            LogUtil.d("startActivityByPkgClass 指定路径不存在~~~~~" + exception.getMessage());
        } catch (Exception e) {
            LogUtil.d("startActivityByPkgClass e = " + e.getMessage());
        }
    }


}