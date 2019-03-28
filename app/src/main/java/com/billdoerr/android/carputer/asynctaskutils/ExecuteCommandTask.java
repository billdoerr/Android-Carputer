package com.billdoerr.android.carputer.asynctaskutils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.billdoerr.android.carputer.utils.FileStorageUtils;
import com.billdoerr.android.carputer.utils.NodeUtils;
import com.jcraft.jsch.JSchException;

/**
 * Async Task to perform ping command.
 * android.os.AsyncTask<Params, Progress, Result>.
 * https://www.tutorialspoint.com/android-asynctask-example-and-explanation
 *
 */
public class ExecuteCommandTask extends AsyncTask<TaskRequest, Void, TaskResult> {

    private static final String TAG = "ExecuteCommandTask";

    public TaskResponse delegate = null;   //  Call back interface
    public Handler handler;
    public Runnable runnable;

    private TaskResult mTaskResults;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        delegate.showProgress();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected TaskResult doInBackground(TaskRequest... params) {

        //  Task parameters
        TaskRequest taskParams = params[0];

        //  Task results
        mTaskResults = new TaskResult();
        mTaskResults.request = taskParams;   //  Assigning TaskParams to TaskResult so we can use this info later in the UI.
        mTaskResults.exception = "";

        //  Loop through nodes
        for (int i = 0; i < taskParams.nodes.size(); i++) {
            try {
                NodeUtils utils = new NodeUtils();
                utils.initialize(taskParams.nodes.get(i).getIp(), taskParams.nodes.get(i).getSSHPort(),
                        taskParams.nodes.get(i).getUser(), taskParams.nodes.get(i).getPassword());
                mTaskResults.response = utils.executeRemoteCommand(taskParams.cmd);

                Log.d(TAG, "doInBackground:  Looping through nodes.");
                if (mTaskResults.response != null) {
                    Log.d(TAG, "doInBackground:  Task Results:\t" + FileStorageUtils.TABS + mTaskResults.response);
                } else {
                    Log.d(TAG, "doInBackground:  Task Results:\t" + FileStorageUtils.TABS + "No result returned.");
                }

            } catch (JSchException e) {
                mTaskResults.exception = e.toString();
                Log.e(TAG, e.getMessage());
            }

        }

        Log.d(TAG,"doInBackground:  completed");

        return mTaskResults;
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);

        Log.d(TAG, "onPostExecute:  Begin");

        if ( (result.response != null) && (!result.response.isEmpty()) ) {

            Log.d(TAG, "onPostExecute:  Returning result.");
            Log.d(TAG, result.response);

            //  Need to cancel Handler if remote command fails before timeout.
            if (handler != null) {
                Log.d(TAG, "onPostExecute:  Removing handler callbacks.");
                handler.removeCallbacks(runnable);
            }

//            delegate.hideProgress();

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

