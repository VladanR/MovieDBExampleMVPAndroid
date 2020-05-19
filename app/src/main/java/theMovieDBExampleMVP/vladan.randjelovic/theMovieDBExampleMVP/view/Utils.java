/*
 * Copyright (C) 2016 The Android Open Source Project
 */

package popmovies.udacity.theMovieDBExampleMVP.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import popmovies.udacity.theMovieDBExampleMVP.presenter.Constants;

/**
 * Utility static methods
 */
public class Utils {

    /**
     * Saves given bitmap to a local JPEG file
     * @param context Context
     * @param fileName Desired filename
     * @param bitmap Bitmap to save
     */
    public static void saveBitmapToJpeg(Context context, String fileName, Bitmap bitmap) {
        String fullFileName = String.format("%s%s", fileName, Constants.JPEG_EXTENSION);
        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File outputFile = new File(directory, fullFileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes image file from disk
     * @param context Context
     * @param fileName File name
     */
    public static void deleteImageFromDisk(Context context, String fileName) {
        String fullFileName = String.format("%s%s", fileName, Constants.JPEG_EXTENSION);
        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File outputFile = new File(directory, fullFileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    /**
     * Returns full image path if it exists locally or null if not
     * @param context Context
     * @param fileName File name
     * @return Full image path if exists locally or null
     */
    public static String getFullImagePathIfExists(Context context, String fileName) {
        String fullFileName = String.format("%s%s", fileName, Constants.JPEG_EXTENSION);
        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File outputFile = new File(directory, fullFileName);
        if (outputFile.exists()) {
            return outputFile.getAbsolutePath();
        }

        return null;
    }
}
