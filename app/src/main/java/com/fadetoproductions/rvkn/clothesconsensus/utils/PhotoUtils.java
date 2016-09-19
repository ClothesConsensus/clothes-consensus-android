package com.fadetoproductions.rvkn.clothesconsensus.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rnewton on 9/4/16.
 */
public class PhotoUtils {

    // TODO putting these here for now, they should probably go into constants or something
    public static final String APP_TAG = "ClothesConsenus";
    public static String PHOTO_FILE_NAME = "temp_photo.jpg";

    public static String getFilePathString(Context context, String fileName) {
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return mediaStorageDir.getPath() + File.separator + fileName;
        }
        return null;
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public static Uri getPhotoFileUri(Context context, String fileName) {
            File file = new File(getFilePathString(context, fileName));
            return Uri.fromFile(file);
    }

    // Returns true if external storage for photos is available
    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public static File newEmptyFile(Context context) {
        // creates a file in the directory for our new photo

        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + PHOTO_FILE_NAME);
    }

    public static void deleteStoredFile(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        File file = new File(mediaStorageDir.getPath() + File.separator + PHOTO_FILE_NAME);
        file.delete();
    }

    public static String encodeBitmapToSendableString(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
        byte[] myByteArray = out.toByteArray();
        String encodedImage = Base64.encodeToString(myByteArray, Base64.DEFAULT);
        return encodedImage;
    }

    public static void resizeStoredImage(Context context) {
        Uri takenPhotoUri = getPhotoFileUri(context, PhotoUtils.PHOTO_FILE_NAME);
        Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
        Bitmap resizedBitmap = Bitmap.createBitmap(rawTakenImage, 0, 0, 480, 480);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        Uri resizedUri = getPhotoFileUri(context, PHOTO_FILE_NAME);

        deleteStoredFile(context);

        File resizedFile = new File(resizedUri.getPath());
        try {
            resizedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(resizedFile);
            fos.write(bytes.toByteArray());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        Matrix matrix = new Matrix();

        // TODO need to figure out why reading the exif wasn't giving us the correct rotation
        matrix.setRotate(90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    public static Bitmap resizeAndRotateAndGetCurrentImage(Context context) {
        // This is doing too much stuff, which should be refactored later
        PhotoUtils.resizeStoredImage(context);
        String photoPath = PhotoUtils.getFilePathString(context, PhotoUtils.PHOTO_FILE_NAME);
        Bitmap lookImage = PhotoUtils.rotateBitmapOrientation(photoPath);
        return lookImage;
    }
}
