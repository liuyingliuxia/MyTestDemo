package com.lingong.mytest.inter;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class SoftKeyBoardListener {
    //activity的根视图
    private View rootView;
    //纪录根视图的显示高度
    private int rootViewVisibleHeight;
    //接口回调
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    private Activity context;

    private SoftKeyBoardListener(Activity context) {
        this.context = context;
        init(context);
    }

    private void init(Activity activity) {
        rootView = activity.getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int visibleHeight = r.height();

            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight;
                return;
            }
            //作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return;
            }

            if (rootViewVisibleHeight - visibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener.keyBoardShow();
                }
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            if (visibleHeight - rootViewVisibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener.keyBoardHide();
                }
                rootViewVisibleHeight = visibleHeight;
                return;
            }

        });
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }


    public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(activity);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }

}
