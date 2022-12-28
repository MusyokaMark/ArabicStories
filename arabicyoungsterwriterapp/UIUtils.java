package com.example.arabicyoungsterwriterapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;


public class UIUtils {

    public static Context getAppContext() {
        return Configs.getInstance().getApplicationContext();
    }

    public static int getDimension(int resId) {
        return (int) getAppContext().getResources().getDimension(resId);
    }

    public static int getColor(int resId) {
        return ContextCompat.getColor(getAppContext(), resId);
    }

    public static String getString(int resId) {
        return getAppContext().getString(resId);
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) {
            return;
        }
        hideKeyboard(currentFocus);
    }
}
