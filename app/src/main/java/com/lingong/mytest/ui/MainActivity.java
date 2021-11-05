package com.lingong.mytest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.databinding.ActivityMainBinding;
import com.lingong.mytest.inter.OnSoftKeyBoardChangeListener;
import com.lingong.mytest.inter.SoftKeyBoardListener;
import com.lingong.mytest.utils.DeviceIdUtil;
import com.lingong.mytest.utils.DisplayUtil;
import com.lingong.mytest.utils.LogUtil;
import com.lingong.mytest.utils.ShellUtils;
import com.lingong.mytest.utils.SnUtil;
import com.lingong.mytest.utils._ShellUtils;

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
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handle the splash screen transition.
//        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        return true;
                        // Check if the initial data is ready.
//                        if (mViewModel.isReady()) {
//                            // The content is ready; start drawing.
//                            content.getViewTreeObserver().removeOnPreDrawListener(this);
//                            return true;
//                        } else {
//                            // The content is not ready; suspend.
//                            return false;
//                        }
                    }
                });

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

        });

        binding.btnTest2.setOnClickListener(v -> {
        });


        LogUtil.d("Windows real width = " + DisplayUtil.getWindowWidth(this) +
                " real height = " + DisplayUtil.getWindowHeight(this) + " window density = " +
                DisplayUtil.getWindowDensity(this));

        LogUtil.d("loadFlashId = " + SnUtil.loadFlashId());
        LogUtil.d("get blkid ===== " + ShellUtils.execRootCmd("blkid\n") + " _Shell = " + _ShellUtils.execCommand("blkid\n" ,false));
//        binding.btnTest1.setText(SnUtil.loadFlashId());
//        binding.btnTest2.setText(_ShellUtils.execCommand("blkid\n" ,false).result);
        binding.tvDeviceId.setText(DeviceIdUtil.getDeviceId(this));


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
    protected void onResume() {
        super.onResume();

        //        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                // 判断辅助功能是否开启
////        String enableAccessibilityService = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
////        String accessibilityEnabled = Settings.Secure.getString(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
//
//                String enableAccessibilityService = ShellUtils.execRootCmd(GET_ACCESSIBILITY_SERVICE_CMD);
//                String accessibilityEnabled = ShellUtils.execRootCmd(GET_ACCESSIBILITY_ENABLED_CMD);
//
//                LogUtil.d("AvsSelfApp:  enableAccessibilityService: " + enableAccessibilityService + ", accessibilityEnabled: " + accessibilityEnabled);
//                if (enableAccessibilityService.isEmpty() || "0".equals(accessibilityEnabled)) {
//                    //use CMD open AccessibilityService
//                    ShellUtils.execRootCmd(ACCESSIBILITY_SERVICE_CMD);
//                    ShellUtils.execRootCmd(ACCESSIBILITY_ENABLED_CMD);
//
//                    LogUtil.d("AvsSelfApp2 :  enableAccessibilityService: " + enableAccessibilityService + ", accessibilityEnabled: " + accessibilityEnabled);
//
//                }
//            }
//        });

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


}