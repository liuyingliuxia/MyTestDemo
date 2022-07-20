package com.lingong.mytest.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.lingong.mytest.utils.LogUtil;

public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtil.d("#### AccessibilityEvent == " + event.getEventType());
    }

    @Override
    public void onInterrupt() {
        LogUtil.d("#### AccessibilityEvent onInterrupt == ");
    }
}
