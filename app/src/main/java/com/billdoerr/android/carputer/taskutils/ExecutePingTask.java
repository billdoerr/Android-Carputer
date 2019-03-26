package com.billdoerr.android.carputer.taskutils;

import android.os.AsyncTask;
import android.os.Handler;

import com.billdoerr.android.carputer.utils.NodeUtils;

/**
 * Async Task to perform ping command.
 * android.os.AsyncTask<Params, Progress, Result>.
 * https://www.tutorialspoint.com/android-asynctask-example-and-explanation
 */
public class ExecutePingTask extends AsyncTask<TaskRequest, Void, TaskResult> {

    private static final String TAG = "ExecutePingTask";

    public AsyncTaskResponse delegate = null;   //  Call back interface

    public Handler h;
    public Runnable r;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected TaskResult doInBackground(TaskRequest... params) {

        TaskRequest taskParams = params[0];
        TaskResult taskResults = new TaskResult();
        taskResults.request = taskParams;
        taskResults.exception = "";

        //  Ping command will only have one node
        String currentNode = taskParams.nodes.get(0).getIp();

        //  Assigning TaskParams to TaskResult so we can use this info for logging.
        taskResults.request = taskParams;   //  Assigning TaskParams to TaskResult so we can use this info for logging.

        try {
            taskResults.cmdResult = new NodeUtils().ping(currentNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskResults;
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);

        //  Check if we have a return value
        if ( (result.cmdResult != null) && (!result.cmdResult.isEmpty()) ) {
            //  TODO:  I don't know if this is good technique.  Need to cancel Handler if remote command fails before timeout.
            if (h != null) {
                h.removeCallbacks(r);
            }
            //  Prepare system log and console message
//            String msg = getString(R.string.msg_ping_results) + FileStorageUtils.TABS + FileStorageUtils.LINE_SEPARATOR;
//            msg = msg + result.cmdResult + FileStorageUtils.LINE_SEPARATOR;
//            updateConsoleAndSystemLog(msg);

        } else {
            //  TODO:
        }

        delegate.processFinish(result);
    }

    @Override
    protected void onCancelled (TaskResult result) {
        //  System log
//        updateConsoleAndSystemLog(getString(R.string.msg_task_timeout) + FileStorageUtils.LINE_SEPARATOR);

        delegate.processFinish(result);
    }

}

