package com.akinfopark.colgbustracking.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResizeImage implements CommonConstants {

    public static String getResizedImage(String filePath, int maxWidthHeight) {

        try {

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
          //  bmOptions.inJustDecodeBounds = true;
            bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            bmOptions.inDither = true;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);

            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap imageRotate = rotateBitmap(bitmap, orientation);
            int originalWidth = imageRotate.getWidth();
            int originalHeight = imageRotate.getHeight();
            int newWidth, newHeight;

            double factor = 1;

            if (originalWidth > originalHeight) {
                factor = (double) originalHeight / maxWidthHeight;
                newHeight = maxWidthHeight;
                newWidth = (int) Math.round(originalWidth / factor);
            } else {
                factor = (double) originalWidth / maxWidthHeight;
                newWidth = maxWidthHeight;
                newHeight = (int) Math.round(originalHeight / factor);
            }

            if (factor >= 1) {
                imageRotate = Bitmap.createScaledBitmap(imageRotate, newWidth, newHeight, true);

            } else {
                imageRotate = Bitmap.createScaledBitmap(imageRotate, originalWidth, originalHeight, true);

            }
           /* newHeight = originalHeight / factor;
            newWidth = originalWidth / factor;*/
           /* String extr = App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
            Log.i("Directory", extr);
            File mFolder = new File(extr + FOLDER_NAME);
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }*/
            File mFolder = App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
            //String s = "Resize_" + dateFormat.format(calendar.getTime()) + ".jpg";
            String s = "." + maxWidthHeight + new File(filePath).getName();
            File f = new File(mFolder.getAbsolutePath(), s);

            String resizedImageFile = f.getAbsolutePath();
            FileOutputStream fos = null;

            fos = new FileOutputStream(f);
            imageRotate.compress(Bitmap.CompressFormat.JPEG, 99, fos);
            fos.flush();
            fos.close();

            return resizedImageFile;
        } catch (Exception e) {
            e.printStackTrace();

            return filePath;
        }
        //return filePath;
    }



    public static String getResizedImageDocuments(String filePath, int maxWidthHeight) {

        try {

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);

            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap imageRotate = rotateBitmap(bitmap, orientation);
            int originalWidth = imageRotate.getWidth();
            int originalHeight = imageRotate.getHeight();
            int newWidth, newHeight;

            double factor = 1;

            if (originalWidth > originalHeight) {
                factor = (double) originalHeight / maxWidthHeight;
                newHeight = maxWidthHeight;
                newWidth = (int) Math.round(originalWidth / factor);
            } else {
                factor = (double) originalWidth / maxWidthHeight;
                newWidth = maxWidthHeight;
                newHeight = (int) Math.round(originalHeight / factor);
            }

            if (factor >= 1) {
                imageRotate = Bitmap.createScaledBitmap(imageRotate, newWidth, newHeight, true);

            } else {
                imageRotate = Bitmap.createScaledBitmap(imageRotate, originalWidth, originalHeight, true);

            }
           /* newHeight = originalHeight / factor;
            newWidth = originalWidth / factor;*/
           /* String extr = MobileApp.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
            Log.i("Directory", extr);
            File mFolder = new File(extr + FOLDER_NAME);
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }*/


/*           File mFolder = App.getInstance().getFilesDir();
           File file1= new File(mFolder,"Rubber");
           if(!file1.exists()){
               file1.mkdir();
           }
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
            //String s = "Resize_" + dateFormat.format(calendar.getTime()) + ".jpg";
            String s = "." + maxWidthHeight + new File(filePath).getName();
            File f = new File(file1.getAbsolutePath(), s);*/
            File mFolder = App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
            //String s = "Resize_" + dateFormat.format(calendar.getTime()) + ".jpg";
            String s = "." + maxWidthHeight + new File(filePath).getName();
            File f = new File(mFolder.getAbsolutePath(), s);



            String resizedImageFile = f.getAbsolutePath();
            FileOutputStream fos = null;

            fos = new FileOutputStream(f);
            imageRotate.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            return resizedImageFile;
        } catch (Exception e) {

            e.printStackTrace();
            return filePath;
        }
        //return filePath;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap getBitmapFromFile(String filePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        Bitmap imageRotate;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            imageRotate = rotateBitmap(bitmap, orientation);
            /*int originalWidth = imageRotate.getWidth();
            int originalHeight = imageRotate.getHeight();
            int newWidth, newHeight;
            int maxWidthHeight = 1200;
            double factor = 1;

            if (originalWidth > originalHeight) {
                factor = (double) originalWidth / maxWidthHeight;
                newWidth = maxWidthHeight;
                newHeight = (int) Math.round(originalHeight / factor);
            } else {
                factor = (double) originalHeight / maxWidthHeight;
                newHeight = maxWidthHeight;
                newWidth = (int) Math.round(originalWidth / factor);
            }
            ShowInfo.showLog("Size", factor + " " + originalWidth + " " + originalHeight + " " + newWidth + " " + newHeight);

            if (factor >= 1) {
                imageRotate = Bitmap.createScaledBitmap(imageRotate, newWidth, newHeight, true);

            } else {
                imageRotate = Bitmap.createScaledBitmap(imageRotate, originalWidth, originalHeight, true);

            }*/
        } catch (IOException e) {
            imageRotate = bitmap;
            e.printStackTrace();
        }
        return imageRotate;
    }

    public static String saveBitmapToFile(Bitmap bitmap, int maxWidthHeight) {
        String resizedImageFile;
        String extr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth, newHeight;
        double factor = 1;

        if (originalWidth > originalHeight) {
            factor = (double) originalWidth / maxWidthHeight;
            newWidth = maxWidthHeight;
            newHeight = (int) Math.round(originalHeight / factor);
        } else {
            factor = (double) originalHeight / maxWidthHeight;
            newHeight = maxWidthHeight;
            newWidth = (int) Math.round(originalWidth / factor);
        }

        if (factor >= 1) {
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        } else {
            bitmap = Bitmap.createScaledBitmap(bitmap, originalWidth, originalHeight, true);

        }
        File mFolder = new File(extr + FOLDER_NAME);
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        String s = "Resize_" + dateFormat.format(calendar.getTime()) + ".jpg";

        File f = new File(mFolder.getAbsolutePath(), s);

        resizedImageFile = f.getAbsolutePath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {

            Log.d("Image", e.getMessage());
        } catch (Exception e) {

            e.printStackTrace();
        }

        return resizedImageFile;
    }

    public static String bitmapToBase64(Bitmap bitmap, int maxWidthHeight) {
        String base64Encoded = "";
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth, newHeight;
        double factor = 1;

        if (originalWidth > originalHeight) {
            factor = (double) originalWidth / maxWidthHeight;
            newWidth = maxWidthHeight;
            newHeight = (int) Math.round(originalHeight / factor);
        } else {
            factor = (double) originalHeight / maxWidthHeight;
            newHeight = maxWidthHeight;
            newWidth = (int) Math.round(originalWidth / factor);
        }

        if (factor >= 1) {
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        } else {
            bitmap = Bitmap.createScaledBitmap(bitmap, originalWidth, originalHeight, true);

        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 99, baos);
        byte[] b = baos.toByteArray();
        base64Encoded = Base64.encodeToString(b, Base64.DEFAULT);
        return base64Encoded;
    }


}
