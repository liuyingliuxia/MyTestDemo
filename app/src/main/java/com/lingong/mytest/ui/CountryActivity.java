package com.lingong.mytest.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.databinding.ActivityCountryBinding;

import java.util.Locale;

public class CountryActivity extends AppCompatActivity {


    private ActivityCountryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvText.setText("CountryCode = " +Locale.getDefault().getCountry() + " \n" +
                " LanguageCode = " + Locale.getDefault().getLanguage());

    }
}