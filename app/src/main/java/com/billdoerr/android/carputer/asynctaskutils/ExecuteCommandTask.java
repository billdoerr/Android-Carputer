package com.billdoerr.android.carputer.asynctaskutils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;

import com.billdoerr.android.carputer.utils.NodeUtils;
import com.jcraft.jsch.JSchException;


/**
 * Async Task to perform ping command.
 * android.os.AsyncTask<Params, Progress, Result>.
 * https://www.tutorialspoint.com/android-asynctask-example-and-explanation
 *
 */
public class ExecuteCommandTask extends AsyncTask<TaskRequest, Void, TaskResult> {

    public TaskResponse delegate = null;   //  Call back interface
    public Handler handler;
    public Runnable runnable;

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
        TaskResult taskResults = new TaskResult();
        taskResults.request = taskParams;   //  Assigning TaskParams to TaskResult so we can use this info later in the UI.
        taskResults.exception = "";

        try {
            NodeUtils utils = new NodeUtils();
            utils.initialize(taskParams.node.getIp(), taskParams.node.getSSHPort(),
                    taskParams.node.getUser(), taskParams.node.getPassword());
            taskResults.response = utils.executeRemoteCommand(taskParams.cmd);
        } catch (JSchException e) {
            taskResults.exception = e.toString();
        }

        return taskResults;
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);
        if ( (result.response != null) && (!result.response.isEmpty()) ) {
            //  Need to cancel Handler if remote command fails before timeout.
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

