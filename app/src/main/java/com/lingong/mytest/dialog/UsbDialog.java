package com.lingong.mytest.dialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.lingong.mytest.R;
import com.lingong.mytest.ui.MainActivity;
import com.lingong.mytest.utils.FastBlurUtil;
import com.lingong.mytest.utils.LogUtil;


public class UsbDialog extends Dialog {
    //    private DialogUsbBinding binding;
    String mUsbName;
    View view;
    Context mContext;
    LayoutInflater inflater;

    public UsbDialog(@NonNull Context context, String usbPath) {
        super(context, R.style.custom_dialog);
        this.mContext = context;
        this.mUsbName = usbPath;
        this.inflater = ((Activity) context).getLayoutInflater();
        initView();
    }


    public UsbDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog);
        this.mContext = context;
        this.inflater = ((Activity) context).getLayoutInflater();
        initView();
    }

//    @BindView(R.id.ivBg)
//    ImageView ivBg;

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_usb, null);

        setContentView(view);
        setFullScreen();
//        btnOpenUsb.requestFocus();
//        tvUsbName.setText(" " + mUsbName + " ");
//
//        UtilBitmap.blurImageView(mContext, flUsbbg, 1);
//        Drawable drawable = new BitmapDrawable(FastBlurUtil.getInstance().getBlurBackgroundDrawer((MainActivity) mContext));
//        flUsbbg.setBackground(drawable);

    }

    /**
     * 非全屏时约束布局设定的宽高比无法生效,会无法显示
     */
    protected void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int Width = displayMetrics.widthPixels;
        int Height = displayMetrics.heightPixels;
        LogUtil.d("width = " + Width + " height = "  +Height);
        LogUtil.d("display.getHeight() = "  +display.getHeight() + " display.getWidth() =" + display.getWidth());
        lp.height = (Width); //设置宽度
        lp.width = (Height); //设置宽度
        getWindow().setAttributes(lp);
    }

    public void showDialog() {
        Drawable drawable = new BitmapDrawable(FastBlurUtil.getInstance().getBlurBackgroundDrawer((MainActivity) mContext));
//        flUsbbg.setBackground(drawable);//todo
        show();
    }
}



