package com.example.arabicyoungsterwriterapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DiaWait {

    public static AlertDialog pd;

    public static void alerDialogeWaiting(Context context, String message){


      AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Please Wait");
        builder.setMessage(message);
        builder.setCancelable(false);
        pd = builder.create();
        pd.show();
    }
    public static void closeDia(){
        if (pd.isShowing()){

            pd.dismiss();
        }
    }
}
