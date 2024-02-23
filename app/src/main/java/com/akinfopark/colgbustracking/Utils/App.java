package com.akinfopark.colgbustracking.Utils;

import android.app.Application;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class App extends Application {

    public SQLiteDatabase sqLiteDatabase;
    private static App app;

    public static Gson gson = new Gson();
    public IntentFilter intentFilter = new IntentFilter(), intentFilterPayment = new IntentFilter();
    public MyPrefs myPrefs;

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    public static OkHttpClient okHttpClient;
    public static long TIMEOUT = 60;

    @Override
    public void onCreate() {
        super.onCreate();


        app = this;
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //  Places.initialize(getApplicationContext(), BuildConfig.DIRK);

        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT * 5, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT * 5, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

    }


    public static boolean isAboveQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
}
