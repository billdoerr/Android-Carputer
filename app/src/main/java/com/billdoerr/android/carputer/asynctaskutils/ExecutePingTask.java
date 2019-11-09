package com.billdoerr.android.carputer.asynctaskutils;

import android.os.AsyncTask;
import android.os.Handler;

import com.billdoerr.android.carputer.utils.NodeUtils;

/**
 * Async Task to perform ping command.
 * android.os.AsyncTask<Params, Progress, Result>.
 * https://www.tutorialspoint.com/android-asynctask-example-and-explanation
 */
public class ExecutePingTask extends AsyncTask<TaskRequest, Void, TaskResult> {

    public TaskResponse delegate = null;   //  Call back interface
    public Handler handler;
    public Runnable runnable;

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
        String currentNode = taskParams.node.getIp();

        //  Task results
        TaskResult taskResult = new TaskResult();
        taskResult.request = taskParams;   //  Assigning TaskParams to TaskResult so we can use this info later in the UI.
        taskResult.exception = "";

        try {
            taskResult.response = new NodeUtils().ping(currentNode);
        } catch (Exception e) {
            taskResult.exception = e.getMessage();
        }

        return taskResult;
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);

        //  Check if we have a return value
        if ( (result.response != null) && (!result.response.isEmpty()) ) {
            //   Cancel Handler if remote command fails before timeout.
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
//            delegate.hideProgress();
        }
        delegate.taskFinished(result);
    }

    @Override
    protected void onCancelled (TaskResult result) {
        delegate.taskCanceled(result);
    }

}

