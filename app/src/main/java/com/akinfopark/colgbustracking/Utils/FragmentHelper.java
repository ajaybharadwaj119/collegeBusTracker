package com.akinfopark.colgbustracking.Utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentHelper {

    Activity activity;
    Context context;

    public static Fragment getFragmentFromActivity(Activity activity, int container) {
        return ((AppCompatActivity) activity).getSupportFragmentManager().findFragmentById(container);
    }

    public static Fragment getFragmentFromFragment(Fragment fragment, int container) {
        return fragment.getChildFragmentManager().findFragmentById(container);
    }

    public static FragmentTransaction getFragmentTransaction(Activity activity) {
        return ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction();
    }

    public static void addFragment(Activity activity, int container, Fragment fragment) {
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack(null).add(container, fragment).commit();
    }

    public static void removeFragment(Activity activity, Fragment fragment) {
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public static void replaceFragment(Activity activity, int container, Fragment fragment) {
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(container, fragment).commit();
    }

    public static void replaceFragmentAddToBack(Activity activity, int container, Fragment fragment) {
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack(null).add(container, fragment).commit();
    }

    public static void replaceFragmentFromFragment(Fragment fragment1, int container, Fragment fragment, String tag) {
        fragment1.getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(container, fragment, tag).commit();
    }

    public static void replaceFragmentFromFragmentAddToBack(Fragment fragment1, int container, Fragment fragment, String tag) {
        fragment1.getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).add(container, fragment, tag).addToBackStack(tag).commit();
    }

    public static void replaceFragmentNoAnim(Activity activity, int container, Fragment fragment) {
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();

    }

    public static void goBackChildFragment(Fragment fragment) {
        if (fragment.getParentFragment() != null) {
            fragment.getParentFragment().getChildFragmentManager().popBackStackImmediate();
        }
    }

    public static boolean checkInstanceActivity(Activity activity, int container, Class c) {
        Fragment fragmentCurrent = ((AppCompatActivity) activity).getSupportFragmentManager().findFragmentById(container);
        return c.isInstance(fragmentCurrent);
    }

    public static boolean checkInstanceFragment(Fragment fragment, int container, Class c) {
        Fragment fragmentCurrent = fragment.getChildFragmentManager().findFragmentById(container);
        return c.isInstance(fragmentCurrent);
    }

}
