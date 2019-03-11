package com.billdoerr.android.carputer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.billdoerr.android.carputer.R.string;

/**
 *
 */
public class ImageStorage {

    private static final String TAG = "ImageStorage";

    //  Returns Snapshot
    public Bitmap getSnapshot(Context context, String filename) {
        String path = context.getFilesDir().toString();
        File imgFile = new  File(path + "/" + filename);
        if(imgFile.exists()){
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        } else {
            return null;
        }
    }

    //

    /**
     *
     * @param context
     * @return List of snapshot files
     */
    public List<String> getSnapshotFileList(Context context) {
        String[] fileList1 = context.getFilesDir().list();
        return new ArrayList<String>(Arrays.asList(fileList1)); //new ArrayList is only needed if you absolutely need an ArrayList
    }

    /**
     * Save image
     * @param context
     * @param bitmap
     * @return
     * @throws FreeSpaceException
     */
    public String saveImage(Context context, Bitmap bitmap) throws FreeSpaceException {
        FileOutputStream outputStream;
        File path = context.getFilesDir();
        String url = "";
        //  First check if space available
        if (isSpaceAvailable(path, bitmap)) {
            try {
                String filename = generateImageFilename();
                outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);      // PNG is a lossless format, the compression factor (100) is ignored
                outputStream.close();
                url = path + "/" + filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //  Throw custom exception?  How?
            throw new FreeSpaceException(path + ": " + string.exception_no_free_space);
        }
        return url;
    }

    /**
     * Check if space available to save bitmap
     * @param path
     * @param bitmap
     * @return True if space available to save bitmap
     */

    private boolean isSpaceAvailable(File path, Bitmap bitmap) {
        //  Being conservative and looking for space 2x bitmap size
        return (path.getFreeSpace() - 2 * sizeOf(bitmap)) > 0;
    }

    /**
     * Get size of bitmap
     * @param data
     * @return Size of bitmap
     */
    private int sizeOf(Bitmap data) {
        return data.getByteCount();
    }

    /**
     * Generate file name
     * @return file name "yyyy-MM-dd'T'HH:mm:ss".jpg
     */
    private String generateImageFilename() {
        return getDateTime() + ".jpg";
    }

    /**
     * Generate date/time stamp that will be used to create a unique filename
     * @return date/time in format:  "yyyy-MM-dd'T'HH:mm:ss"
     */
    private String getDateTime() {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";       //  "yyyy-MM-dd HH:mm:ss"
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(c.getTime());
    }

    /**
     * Generate custom exception
     */
    public class FreeSpaceException extends Exception {
        public FreeSpaceException () {

        }

        /**
         *
         * @param message
         */
        FreeSpaceException(String message) {
            super (message);
        }

        /**
         *
         * @param cause
         */
        public FreeSpaceException (Throwable cause) {
            super (cause);
        }

        /**
         *
         * @param message
         * @param cause
         */
        public FreeSpaceException (String message, Throwable cause) {
            super (message, cause);
        }
    }

}

