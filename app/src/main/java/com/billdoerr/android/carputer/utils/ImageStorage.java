package com.billdoerr.android.carputer.utils;

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

    //  Return list of snapshot files
    public List<String> getSnapshotFileList(Context context) {
        String[] fileList1 = context.getFilesDir().list();
        return new ArrayList<String>(Arrays.asList(fileList1)); //new ArrayList is only needed if you absolutely need an ArrayList
    }

//    //  Return list of snapshot files
//    public List<String> getSnapshotFileList(Context context) {
//        File[] fileList = context.getFilesDir().listFiles();
//        List<String> stringList = new ArrayList<String>(Arrays.asList(fileList)); //new ArrayList is only needed if you absolutely need an ArrayList
//    }

    //  Save image
    public String saveImage(Context context, Bitmap bitmap) {
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
            //  TODO : Generate custom exception?  How?
        }
        return url;
    }

    //  Check if space available to save bitmap
    private boolean isSpaceAvailable(File path, Bitmap bitmap) {
        //  Being conservative and looking for space 2x bitmap size
        return (path.getFreeSpace() - 2 * sizeOf(bitmap)) > 0;
    }

    //  Get size of bitmap
    private int sizeOf(Bitmap data) {
        return data.getByteCount();
    }

    //  Generate file name
    private String generateImageFilename() {
        return getDateTime() + ".jpg";
    }

    //  Generate date/time stamp that will be used to create a unique filename
    private String getDateTime() {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";       //  "yyyy-MM-dd HH:mm:ss"
//        String date = "";
//         try {
             Calendar c = Calendar.getInstance();
             SimpleDateFormat df = new SimpleDateFormat(dateFormat);
//             String date = df.format(c.getTime());
//             Date d = df.parse(formattedDate.replaceAll("Z$", "+0000"));
//            date =  df.format(d).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return df.format(c.getTime());
    }

}

