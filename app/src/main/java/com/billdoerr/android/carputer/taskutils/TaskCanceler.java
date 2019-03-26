package com.billdoerr.android.carputer.taskutils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Cancels task after period of time.
 */
public class TaskCanceler implements Runnable {

    private static final String TAG = "TaskCanceler";

    private AsyncTask task;

    public TaskCanceler(AsyncTask task) {
        this.task = task;
    }

    //  TODO:  This is fucked up!
    @Override
    public void run() {

//        String msg = getString(R.string.msg_task_timeout) + FileStorageUtils.LINE_SEPARATOR;

        if (task.getStatus() == AsyncTask.Status.RUNNING) {     // FINISHED, PENDING, RUNNING
//            msg = msg + getString(R.string.msg_task_cancelled) + FileStorageUtils.LINE_SEPARATOR;
            Log.d(TAG, "TaskCanceler->running");
            task.cancel(true);
        } else {
//            msg = msg + getString(R.string.msg_task_already_canceled) + FileStorageUtils.LINE_SEPARATOR;
            Log.d(TAG, "TaskCanceler->not running");
        }

//        //  Dismiss progress dialog
//        if (progressDialog != null) {
//            progressDialog.hide();
//            progressDialog = null;
//        }
//
//        //  System log
//        updateConsoleAndSystemLog(TAG, msg);
    }

}