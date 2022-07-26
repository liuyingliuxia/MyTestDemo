package com.lingong.mytest.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.databinding.ActivityMainBinding;
import com.lingong.mytest.utils.AccessibilitySettingUtils;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnJumpToSettings.setOnClickListener(v -> {
            AccessibilitySettingUtils.jumpToAccessibilitySetting(this);
        });

        binding.btnTest.setOnClickListener(v->{
//            ClickUtils.INSTANCE.click();
            Toast.makeText(this, "I'm clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
