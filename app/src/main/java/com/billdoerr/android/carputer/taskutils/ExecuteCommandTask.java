package com.billdoerr.android.carputer.taskutils;

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

    TaskResult taskResults;

    public AsyncTaskResponse delegate = null;   //  Call back interface

    public Handler h;
    public Runnable r;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //  TODO:  This needs work.
    @SuppressLint("StaticFieldLeak")
    @Override
    protected TaskResult doInBackground(TaskRequest... params) {

        TaskRequest taskParams = params[0];
        taskResults = new TaskResult();
        taskResults.request = taskParams;
        taskResults.exception = "";

        //  Loop through nodes
        for (int i = 0; i < taskParams.nodes.size(); i++) {
            try {
                NodeUtils utils = new NodeUtils();
                utils.initialize(taskParams.nodes.get(i).getIp(), taskParams.nodes.get(i).getSSHPort(),
                        taskParams.nodes.get(i).getUser(), taskParams.nodes.get(i).getPassword());
                taskResults.cmdResult = utils.executeRemoteCommand(taskParams.cmd);
                FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + "Loop through nodes");
                if (taskResults.cmdResult != null) {
                    FileStorageUtils.writeSystemLog("Task Results:\t" + FileStorageUtils.TABS + taskResults.cmdResult);
                } else {
                    FileStorageUtils.writeSystemLog("Task Results:\t" + FileStorageUtils.TABS + "No result returned.");
                }


            } catch (JSchException e) {
                Log.d(TAG, e.toString());
                taskResults.cmdResult = e.toString();
                taskResults.exception = e.toString();
                FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + "JSchException");
                FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + e.toString());
            }

        }

        FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + "Do in background completed");
        FileStorageUtils.writeSystemLog("Do in background completed" + FileStorageUtils.TABS + "Do in background completed");

        return taskResults;
    }

    //  TODO:  This needs work.
    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);

        Log.d(TAG, "onPostExecute");
        FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + "Start:  onPostExecute");

        if ( (result.cmdResult != null) && (!result.cmdResult.isEmpty()) ) {
            //  TODO:  I don't know if this is good technique.  Need to cancel Handler if remote command fails before timeout.
            if (h != null) {
                h.removeCallbacks(r);
            }

            //  Prepare system log and console message
//            String msg = getString(R.string.msg_execute_command_results) + FileStorageUtils.TABS + FileStorageUtils.LINE_SEPARATOR;
//            msg = msg + result.request.taskName + FileStorageUtils.LINE_SEPARATOR;
//            msg = msg + result.cmdResult + FileStorageUtils.LINE_SEPARATOR;
//            updateConsoleAndSystemLog(msg);
            Log.i(TAG, TAG + "->onPostExecute->hide");

        } else {
            //  TODO
        }

        FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + "End:  onPostExecute");

        delegate.processFinish(result);

    }

    //  TODO:  This needs work.
    @Override
    protected void onCancelled (TaskResult result) {

        FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + "onCancelled");

        //  Prepare system log and console message
//        String msg = getString(R.string.msg_task_timeout) + FileStorageUtils.LINE_SEPARATOR;
//        msg = msg + getString(R.string.msg_task_cancelled) + FileStorageUtils.LINE_SEPARATOR;
        //  TODO:  accessing params resulting in exception
//            msg = msg + getString(R.string.msg_on_node) + result.params.nodes.toString();
//            msg = msg + FileStorageUtils.LINE_SEPARATOR;
//        updateConsoleAndSystemLog("ScoobyDoo:  onCancelled");

        Log.i(TAG, TAG + ":onCancelled");

        result = new TaskResult();

        delegate.processFinish(result);

    }

}

