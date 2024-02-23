package com.akinfopark.colgbustracking.loginReg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.akinfopark.colgbustracking.MainActivity;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                /*if (login) {

                    if (MyPrefs.getInstance(getApplicationContext()).getString(UserData.KEY_USER_TYPE).equalsIgnoreCase("Admin")) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, AgentMainActivity.class);
                    }

                } else {*/
                intent = new Intent(SplashActivity.this, MainActivity.class);
                //}
                startActivity(intent);
                finishAffinity();
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
}