package com.akinfopark.colgbustracking.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class MyPrefs implements CommonConstants {

    Context context;
    SharedPreferences sharedPreferences;

    public MyPrefs(Context context, String prefName) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(prefName, 0);
    }

    public static MyPrefs getInstance(Context context) {
        return new MyPrefs(context, context.getPackageName());
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        Log.i("Preference", key + ":" + value);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        Log.i("Preference", key + ":" + value);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public String getStringDefault(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public String getStringNum(String key) {
        return sharedPreferences.getString(key, "0");
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /*public void putString(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,"");
        editor.apply();
    }
*/
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public static int getInt(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static String getStringNum(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "0");
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }
}
