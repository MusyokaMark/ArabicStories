package com.example.arabicyoungsterwriterapp;

import android.app.Activity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MarshMallowPermission {
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForLocation() {
        int result = ContextCompat.checkSelfPermission(this.activity, "android.permission.ACCESS_FINE_LOCATION");
        if (result == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForRecord() {
        int result = ContextCompat.checkSelfPermission(this.activity, "android.permission.RECORD_AUDIO");
        if (result == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadExternalStorage() {
        int result = ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_EXTERNAL_STORAGE");
        if (result == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForWriteExternalStorage() {
        int result = ContextCompat.checkSelfPermission(this.activity, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (result == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(this.activity, "android.permission.CAMERA");
        if (result == 0) {
            return true;
        }
        return false;
    }

    public void requestPermissionForLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.ACCESS_FINE_LOCATION")) {
            Configs.simpleAlert("Location permission is needed to search Ads nearby your location. Please enable Location in App Settings, otherwise you'll get Ads only from New York City.", this.activity);
        } else {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        }
    }

    public void requestPermissionForRecord() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.RECORD_AUDIO")) {
            Toast.makeText(this.activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", 1).show();
        } else {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.RECORD_AUDIO"}, 2);
        }
    }

    public void requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_EXTERNAL_STORAGE")) {
            Toast.makeText(this.activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", 1).show();
        } else {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 3);
        }
    }

    public void requestPermissionForWriteExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Toast.makeText(this.activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", 1).show();
        } else {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
        }
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.CAMERA")) {
            Toast.makeText(this.activity, "Camera permission needed. Please allow in App Settings for additional functionality.", 1).show();
        } else {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.CAMERA"}, 5);
        }
    }
}
