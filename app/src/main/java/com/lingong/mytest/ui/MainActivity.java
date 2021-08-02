package com.lingong.mytest.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lingong.mytest.R;
import com.lingong.mytest.databinding.ActivityMainBinding;
import com.lingong.mytest.dialog.UsbDialog;
import com.lingong.mytest.inter.OnSoftKeyBoardChangeListener;
import com.lingong.mytest.inter.SoftKeyBoardListener;
import com.lingong.mytest.utils.LogUtil;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static final String TAG = "MainActivity";
    EditText editText;
    UsbDialog mUsbDialog;

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

            Intent intent = new Intent("com.jmgo.action.SHOW_TOU");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

//            Intent broadIntent = new Intent("com.jmgo.ota.firmware");
//            broadIntent.putExtra( "firmware_version","1.0.66");
//            sendBroadcast(broadIntent);
        });

//        binding.btnTest2.setText("JMGO");
        binding.btnTest2.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.setClassName("com.jmgo.luna", "com.jmgo.luna.ui.JmgoFunActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            Intent intent = new Intent("com.jmgo.action.SHOW_TOU");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("TouUrlNameConst" ,2);//1 主协议 ；2 副协议
//            startActivity(intent);
        });

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