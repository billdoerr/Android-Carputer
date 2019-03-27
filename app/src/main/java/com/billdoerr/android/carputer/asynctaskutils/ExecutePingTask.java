package com.billdoerr.android.carputer.asynctaskutils;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.billdoerr.android.carputer.utils.NodeUtils;

/**
 * Async Task to perform ping command.
 * android.os.AsyncTask<Params, Progress, Result>.
 * https://www.tutorialspoint.com/android-asynctask-example-and-explanation
 */
public class ExecutePingTask extends AsyncTask<TaskRequest, Void, TaskResult> {

    private static final String TAG = "ExecutePingTask";

    public TaskResponse delegate = null;   //  Call back interface
    public Handler handler;
    public Runnable runnable;

    private TaskResult mTaskResult;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        delegate.showProgress();
    }

    @Override
    protected TaskResult doInBackground(TaskRequest... params) {

        //  Task parameters
        TaskRequest taskParams = params[0];

        //  Ping command will only have one node
        String currentNode = taskParams.nodes.get(0).getIp();

        //  Task results
        mTaskResult = new TaskResult();
        mTaskResult.request = taskParams;   //  Assigning TaskParams to TaskResult so we can use this info later in the UI.
        mTaskResult.exception = "";


        try {
            Log.d(TAG, "doInBackground: Performing ping.");
            mTaskResult.response = new NodeUtils().ping(currentNode);
        } catch (Exception e) {
            mTaskResult.exception = e.getMessage();
            Log.e(TAG, e.getMessage());
        }

        return mTaskResult;
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);


        Log.d(TAG, "onPostExecute:  Begin");

        //  Check if we have a return value
        if ( (result.response != null) && (!result.response.isEmpty()) ) {

            Log.d(TAG, "onPostExecute:  Returning result.");
            Log.d(TAG, result.response);

            //  TODO:  I don't know if this is good technique.
            //   Cancel Handler if remote command fails before timeout.
            if (handler != null) {
                Log.d(TAG, "onPostExecute:  Removing handler callbacks.");
                handler.removeCallbacks(runnable);
            }

            //  Hide progress
            delegate.hideProgress();

        } else {
            Log.d(TAG, "onPostExecute:  No result.");
        }

        delegate.taskFinished(result);
    }

    @Override
    protected void onCancelled (TaskResult result) {
        Log.d(TAG, "onCancelled");
        delegate.taskCanceled(result);
    }

}

