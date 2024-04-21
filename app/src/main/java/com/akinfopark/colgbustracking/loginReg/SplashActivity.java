package com.akinfopark.colgbustracking.loginReg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.akinfopark.colgbustracking.AdminActivity;
import com.akinfopark.colgbustracking.MainActivity;
import com.akinfopark.colgbustracking.StudentActivity;
import com.akinfopark.colgbustracking.Utils.MyPrefs;
import com.akinfopark.colgbustracking.databinding.ActivityMainBinding;

public class SplashActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Activity activity;
    private static int SPLASH_SCREEN_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;


                if (MyPrefs.getInstance(getApplicationContext()).getString("login").equalsIgnoreCase("driver")) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else if (MyPrefs.getInstance(getApplicationContext()).getString("login").equalsIgnoreCase("student")) {
                    intent = new Intent(SplashActivity.this, StudentActivity.class);
                } else if (MyPrefs.getInstance(getApplicationContext()).getString("login").equalsIgnoreCase("admin")) {
                    intent = new Intent(SplashActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finishAffinity();
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
}