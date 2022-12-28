package com.example.arabicyoungsterwriterapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Configs extends Application {
    private static Configs instance;
    public static Typeface osBold;
    public static Typeface osRegular;
    public static AlertDialog pd;


    public static Configs getInstance() {
        return instance;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        instance = this;


    }

    public static void showProgressDialoge(String mess, Context ctx) {
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.pd, (ViewGroup) null);
        TextView messTxt = (TextView) dialogView.findViewById(R.id.pdMessTxt);
        messTxt.setText(mess);
        db.setView(dialogView);
        db.setCancelable(true);
        AlertDialog create = db.create();
        pd = create;
        create.show();
    }

    public static void hideProgressDialoge() {
        pd.dismiss();
    }

    public static Bitmap scaleBitmapToMaxSize(int maxSize, Bitmap bm) {
        int outHeight;
        int outWidth;
        int inWidth = bm.getWidth();
        int inHeight = bm.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        return Bitmap.createScaledBitmap(bm, outWidth, outHeight, false);
    }


    public static void simpleAlert(String mess, Context activity) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(mess).setTitle(R.string.app_name).setPositiveButton("OK", (DialogInterface.OnClickListener) null).setIcon(R.mipmap.ic_launcher);
        alert.create().show();
    }

}
