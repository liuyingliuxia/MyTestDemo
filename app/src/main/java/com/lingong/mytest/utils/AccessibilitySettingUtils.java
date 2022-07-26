package com.lingong.mytest.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class AccessibilitySettingUtils {
    public static void jumpToAccessibilitySetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        context.startActivity(intent);
    }

}
