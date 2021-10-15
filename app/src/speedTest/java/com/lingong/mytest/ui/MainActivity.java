package com.lingong.mytest.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.databinding.ActivityMainBinding;
import com.lingong.mytest.util.SpeedTestTask;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new SpeedTestTask().execute();
    }

}