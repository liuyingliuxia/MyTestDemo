package com.lingong.mytest.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;

public class ClickUtils {
    public static void click(AccessibilityService accessibilityService, Float x, Float y) {
        LogUtil.d("click: ($x, $y)");
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 1));
//        Builder gesture = builder.build();
//        accessibilityService.dispatchGesture(
//                gesture,
//                object :AccessibilityService.GestureResultCallback() {
//            override fun onCancelled(GestureDescription gestureDescription){
//                super.onCancelled(gestureDescription)
//            }
//
//            override fun onCompleted(gestureDescription:GestureDescription){
//                super.onCompleted(gestureDescription)
//            }
//        },
//        return null;
//        );
    }


}
