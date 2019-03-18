package com.billdoerr.android.carputer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.billdoerr.android.carputer.R.string;

/**
 *  File storage utilities.
 */
public class FileStorageUtils {

    private static final String TAG = "FileStorageUtils";

    private static final String mLineSeparator = System.getProperty("line.separator");
    private static final String mTabs = "\t\t";

    private static Context mContext;
    private static String mFilename;

    //

    /**
     * Returns bitmap of save image (snapshot)
     * @param context Context:  Application context.
     * @param filename String: Filename of image.
     * @return Bitmap:  Returns bitmap of requested filename.
     */
    public static Bitmap getSnapshot(Context context, String filename) {
        String path = context.getFilesDir().toString();
        File imgFile = new  File(path + "/" + filename);
        if(imgFile.exists()){
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        } else {
            return null;
        }
    }

    /**
     * Returns list of saved images (snapshots).
     * @param context Context:  Application context.
     * @return List<String>:  List of snapshot files.
     */
    public static List<String> getSnapshotFileList(Context context) {
        String[] fileList1 = context.getFilesDir().list();
        return new ArrayList<String>(Arrays.asList(fileList1)); //new ArrayList is only needed if you absolutely need an ArrayList
    }

    /**
     * Save image to local storage.
     * @param context Context:  Application context.
     * @param bitmap Bitmap:  Bitmap of imaged that will be saved to local storage.
     * @return String:  Url string of resource that was saved to internal storage.
     * @throws FreeSpaceException: Custom exception thrown is space not available to save image to local storage.
     */
    public static String saveImage(Context context, Bitmap bitmap) throws FreeSpaceException {
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
     * Check if space available to save bitmap.
     * @param path File:  Path that is of type File where image will be saved to local storage.
     * @param bitmap Bitmap:  Bitmap of image being saved to local storage.
     * @return boolean:  True if space available to save bitmap.
     */

    public static boolean isSpaceAvailable(File path, Bitmap bitmap) {
        //  Being conservative and looking for space 2x bitmap size
        return (path.getFreeSpace() - 2 * sizeOf(bitmap)) > 0;
    }

    /**
     * Get size of bitmap.
     * @param data Bitmap:  Bitmap object
     * @return int:  Size of bitmap.
     */
    public static int sizeOf(Bitmap data) {
        return data.getByteCount();
    }

    /**
     * Generate file name.
     * @return String:  Filename "yyyy-MM-dd'T'HH:mm:ss".jpg.
     */
    public static String generateImageFilename() {
        return getDateTime() + ".jpg";
    }

    /**
     * Generate date/time stamp that will be used to create a unique filename.
     * @return String:  date/time in format:  "yyyy-MM-dd'T'HH:mm:ss".
     */
    public static String getDateTime() {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";       //  "yyyy-MM-dd HH:mm:ss"
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(c.getTime());
    }

    /**
     * Generate date/time stamp that will be used to for system log entries.
     * @return String:  date/time in format:  "dd MMM yyyy HH:mm:ss".
     */
    public static String getDateTime2() {
        String dateFormat = "dd MMM yyyy HH:mm:ss";
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(c.getTime());
    }

    /**
     * Generate custom exception.
     */
    public static class FreeSpaceException extends Exception {
        public FreeSpaceException () {

        }

        /**
         *
         * @param message String:  Custom message for customer FreeSpaceException.
         */
        FreeSpaceException(String message) {
            super (message);
        }

        /**
         *
         * @param cause Throwable:  Throwable object containing cause of custom exception.
         */
        public FreeSpaceException (Throwable cause) {
            super (cause);
        }

        /**
         *
         * @param message String:  Message of type String for custom FreeSpaceException.
         * @param cause Throwable:  Throwable object containing cause of custom exception.
         */
        public FreeSpaceException (String message, Throwable cause) {
            super (message, cause);
        }
    }

    /**
     *
     * @param context Context:  Application context.
     * @param data String:  Data to be written to file.
     * @param filename String: The name of the file to open; can not contain path separators.
     * @param mode int: File creation mode
     */
    public static void writeToFile(Context context, String data, String filename, int mode) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, mode));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(context.getString(string.exception_tag), context.getString(string.exception_file_write_failed) + " " + e.toString());
        }
    }

    /**
     * Read data from file.
     * @param context Context:  Application context.
     * @param filename String: The name of the file to open; can not contain path separators.
     * @return String:  Contents of file being read.
     */
    public static String readFromFile(Context context, String filename) {

        final String lineSeparator = System.getProperty("line.separator");
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append(lineSeparator);
                }

                inputStream.close();
                ret = stringBuilder.toString();

            }
        }
        catch (FileNotFoundException e) {
            Log.e(context.getString(string.exception_tag), context.getString(string.exception_file_not_found) + " " + e.toString());
        } catch (IOException e) {
            Log.e(context.getString(string.exception_tag), context.getString(string.exception_cannot_read_file) + " " + e.toString());
        }

        return ret;
    }

    /**
     * Sets application context and system log filename.
     * Required to be called prior to calling writeSystemLog() or readSystemLog().
     * @param context Context:  Application context.
     * @param filename String:  System log filename.
     */
    public static void initializeSystemLog(Context context, String filename) {
        mContext = context;
        mFilename = filename;
    }

    /**
     * Writes to the system log.  Each entry is preceded with a date/time stamp.
     * @param entry String:  Data to be written to log file.
     */
    public static void writeSystemLog(String entry) {
        final int mode = Context.MODE_PRIVATE | Context.MODE_APPEND;
        String output = getDateTime2() + mTabs + entry + mLineSeparator + mLineSeparator;
        writeToFile(mContext, output, mFilename, mode);
    }

    /**
     * Read the system log.
     * @return String:  Contents of system log.
     */
    public static String readSystemLog() {
        return readFromFile(mContext, mFilename);
    }

    /**
     * Clears the contents of the system log.
     */
    public static void clearSystemLog() {
        final int mode = Context.MODE_PRIVATE;
        writeToFile(mContext, "", mFilename, mode);
    }

}

