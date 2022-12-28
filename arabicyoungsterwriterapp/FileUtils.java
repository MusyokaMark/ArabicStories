package com.example.arabicyoungsterwriterapp;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static Bitmap getPictureFromPath(String currentPhotoPath, int requiredSize) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, o);
            int scale = 1;
            while (true) {
                if (o.outWidth / scale <= requiredSize && o.outHeight / scale <= requiredSize) {
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    return BitmapFactory.decodeFile(currentPhotoPath, o2);
                }
                scale *= 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeIntentData(Uri data, int requiredSize) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            ContentResolver cr = UIUtils.getAppContext().getContentResolver();
            BitmapFactory.decodeStream(cr.openInputStream(data), null, o);
            o.inSampleSize = calculateInSampleSize(o, requiredSize, requiredSize);
            o.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(cr.openInputStream(data), null, o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeByteArray(byte[] data, int requiredSize) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        UIUtils.getAppContext().getContentResolver();
        BitmapFactory.decodeByteArray(data, 0, data.length, o);
        o.inSampleSize = calculateInSampleSize(o, requiredSize, requiredSize);
        o.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, o);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static File createCachedFileToShare(Bitmap bitmap) {
        File imagesFolder = new File(UIUtils.getAppContext().getCacheDir(), "images");
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri getUri(File file) {
        Context context = UIUtils.getAppContext();
        if (file != null) {
            if (Build.VERSION.SDK_INT < 24) {
                return Uri.fromFile(file);
            }
            return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        }
        return null;
    }

    public static File createEmptyFile(String fileName, String extension) {
        File root = UIUtils.getAppContext().getExternalCacheDir();
        try {
            File myDir = File.createTempFile(fileName, extension, root);
            return myDir;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap processExif(String photoPath, Bitmap bitmap) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt("Orientation", 0);
            if (orientation == 3) {
                bitmap = rotateImage(bitmap, 180.0f);
            } else if (orientation == 6) {
                bitmap = rotateImage(bitmap, 90.0f);
            } else if (orientation == 8) {
                bitmap = rotateImage(bitmap, 270.0f);
            }
        } catch (IOException e) {
            Log.d("FileUtils", "Exif not found");
            e.printStackTrace();
        }
        return bitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
