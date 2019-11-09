package com.billdoerr.android.carputer.asynctaskutils;

import android.os.AsyncTask;

/**
 * Cancels task after period of time.
 */
public class TaskTimeout implements Runnable {

    private final AsyncTask task;

    public TaskTimeout(AsyncTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        if (task.getStatus() == AsyncTask.Status.RUNNING) {     // FINISHED, PENDING, RUNNING
            task.cancel(true);
        }
    }

}