package com.billdoerr.android.carputer.asynctaskutils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Cancels task after period of time.
 */
public class TaskTimeout implements Runnable {

    private static final String TAG = "TaskTimeout";

    private final AsyncTask task;

    public TaskTimeout(AsyncTask task) {
        this.task = task;
    }

    @Override
    public void run() {

        if (task.getStatus() == AsyncTask.Status.RUNNING) {     // FINISHED, PENDING, RUNNING
            task.cancel(true);
            Log.d(TAG, "Task timeout with status running.  Task status:  " + task.getStatus().toString());
        } else {
            Log.d(TAG, "Task timeout with with state either pending or finished.  Task status:  " + task.getStatus().toString());
        }

    }

}