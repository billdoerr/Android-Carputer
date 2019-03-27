package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.billdoerr.android.carputer.settings.Node;
import com.billdoerr.android.carputer.asynctaskutils.TaskResponse;
import com.billdoerr.android.carputer.asynctaskutils.ExecuteCommandTask;
import com.billdoerr.android.carputer.asynctaskutils.ExecutePingTask;
import com.billdoerr.android.carputer.asynctaskutils.TaskTimeout;
import com.billdoerr.android.carputer.asynctaskutils.TaskRequest;
import com.billdoerr.android.carputer.asynctaskutils.TaskResult;
import com.billdoerr.android.carputer.utils.FileStorageUtils;
import com.billdoerr.android.carputer.utils.WiFiUtils;

/**
 *  Child fragment of CarputerFragmentMgmt.
 *  Use to perform simple remote operations on configured nodes.
 */
public class SSHFragment extends Fragment implements TaskResponse {

    private static final String TAG = "SSHFragment";
    private static final long TIMEOUT = 30*1000;     //  Time in milliseconds

    private GlobalVariables mGlobalVariables;

    private List<Node> mNodes = new ArrayList<Node>();
    private WiFiUtils mWiFiUtils;
    private static TextView sTxtReply;

    private ProgressDialog progressDialog;

    //  AsyncTask
    private ExecuteCommandTask mExecuteCommandTask = new ExecuteCommandTask();
    private ExecutePingTask mExecutePingTask = new ExecutePingTask();

    //  Indicate syncDateAll has been called
    private static boolean sDateSynced = false;

    public static SSHFragment newInstance() {
        return new SSHFragment();
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Enable options menu
        setHasOptionsMenu(true);

        //  This to set delegate/listener back to this class
        mExecuteCommandTask.delegate = this;
        mExecutePingTask.delegate = this;

        // Calling Application class (see application tag in AndroidManifest.xml)
        mGlobalVariables = (GlobalVariables) getActivity().getApplicationContext();

        //  Get devices
        mNodes = mGlobalVariables.getNodes();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        EditText txtExecuteCommand;
        Spinner spinnerNodes;

        View view = inflater.inflate(R.layout.fragment_ssh, container, false);

        //  Restore state
        if (savedInstanceState != null) {
            sDateSynced = savedInstanceState.getBoolean("DateSynced");
        }

        //  TextView:  Reply
        sTxtReply = (TextView) view.findViewById(R.id.txtReply);

        // Creating adapter for spinner
        spinnerNodes = (Spinner) view.findViewById(R.id.spinnerNodes);
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < mNodes.size(); i++) {
            nodes.add(mNodes.get(i).getIp());
        }
        ArrayAdapter<String> dataAdapterNodes = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nodes);

        // Drop down layout style
        dataAdapterNodes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerNodes.setAdapter(dataAdapterNodes);

        //  EditText:  Execute Command
        txtExecuteCommand = (EditText) view.findViewById(R.id.txtExecuteCommand);
        txtExecuteCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txtExecuteCommand.setInputType(InputType.TYPE_CLASS_TEXT);  //  Show keyboard.  Execute command would have hidden keyboard.
            }
        });

        //  Button:  Ping
        Button btnPing = (Button) view.findViewById(R.id.btnPing);
        btnPing.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                //  Prepare system log and console message
                String msg = getString(R.string.msg_executing_ping) + getString(R.string.msg_on_node) + spinnerNodes.getSelectedItem().toString() + FileStorageUtils.LINE_SEPARATOR;
                updateConsoleAndSystemLog(msg);

                //  Get node from dropdown
                int index = spinnerNodes.getSelectedItemPosition();
                TaskRequest p = new TaskRequest();
                p.nodes = new ArrayList<>();
                p.nodes.add(mNodes.get(index));     //  List<Node> will be of size one, since only pinging a single node.
                p.cmd = "";     //  Ping does not send a command
                p.taskName = getString(R.string.msg_executing_ping) + getString(R.string.msg_on_node) + spinnerNodes.getSelectedItem().toString();;

                // Perform ping
                executePingTask(p);
            }
        });

        //  Button:  PowerOff (Single)
        Button btnPowerOffSingle = (Button) view.findViewById(R.id.btnPoweroffSingle);
        btnPowerOffSingle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                final String cmd = "sudo shutdown -h 0";
                txtExecuteCommand.setText(cmd);     //  Display command

                //  Prepare system log and console message
                String msg = getString(R.string.msg_executing_command) + FileStorageUtils.TABS + cmd + FileStorageUtils.LINE_SEPARATOR;
                msg = msg + getString(R.string.msg_power_off_single_node) + spinnerNodes.getSelectedItem().toString() + FileStorageUtils.LINE_SEPARATOR;
                updateConsoleAndSystemLog(msg);

                //  Get node from dropdown
                int index = spinnerNodes.getSelectedItemPosition();
                TaskRequest request = new TaskRequest();
                request.nodes = new ArrayList<>();
                request.nodes.add(mNodes.get(index));
                request.cmd = cmd;
                request.taskName = getString(R.string.msg_power_off_single_node) + spinnerNodes.getSelectedItem().toString();

                //  Execute command
                executeCommandTask(request);
            }
        });

        //  Button:  PowerOff (All)
        Button btnPowerOffAll = (Button) view.findViewById(R.id.btnPoweroffAll);
        btnPowerOffAll.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                final String cmd = "sudo shutdown -h 0";
                txtExecuteCommand.setText(cmd);     //  Display command

                //  Prepare system log and console message
                String msg = getString(R.string.msg_executing_command) + FileStorageUtils.TABS + cmd + FileStorageUtils.LINE_SEPARATOR;
                msg = msg + getString(R.string.msg_power_off_all_nodes) + FileStorageUtils.LINE_SEPARATOR;
                updateConsoleAndSystemLog(msg);

                //  All nodes
                TaskRequest request = new TaskRequest();
                request.nodes = mNodes;
                request.cmd = cmd;
                request.taskName = getString(R.string.msg_power_off_all_nodes);

                //  Execute command
                executeCommandTask(request);
            }
        });

        //  Button:  Execute command
        Button btnExecuteCommand = (Button) view.findViewById(R.id.btnExecuteCommand);
        btnExecuteCommand.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                String cmd = txtExecuteCommand.getText().toString();
                txtExecuteCommand.setText(cmd);     //  Update text view with command

                //  Prepare system log and console message
                String msg = getString(R.string.msg_executing_command) + FileStorageUtils.TABS + cmd + FileStorageUtils.LINE_SEPARATOR;
                msg = msg + getString(R.string.msg_on_node) + spinnerNodes.getSelectedItem().toString() + FileStorageUtils.LINE_SEPARATOR;
                updateConsoleAndSystemLog(msg);

                //  Hide soft keyboard
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                //  Get node from dropdown
                int index = spinnerNodes.getSelectedItemPosition();
                TaskRequest request = new TaskRequest();
                request.nodes = new ArrayList<>();
                request.nodes.add(mNodes.get(index));
                request.cmd = cmd;
                request.taskName = msg;

                //  Execute command
                executeCommandTask(request);
            }
        });

        //  Button:  SyncDate - Single Node
        Button btnSyncDateSingle = (Button) view.findViewById(R.id.btnSyncDateSingle);
        btnSyncDateSingle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                String date = getDateTime();
                //  Sync Android date/time with Pi.  Follow with 'date' command to view system date/time.
                final String cmd = "sudo date -s \"" + date + "\" ;date";
                txtExecuteCommand.setText(cmd);

                //  Prepare system log and console message
                String msg = getString(R.string.msg_executing_command) + FileStorageUtils.TABS + cmd + FileStorageUtils.LINE_SEPARATOR;
                msg = msg + getString(R.string.msg_sync_date_single_node) + spinnerNodes.getSelectedItem().toString() + FileStorageUtils.LINE_SEPARATOR;
                updateConsoleAndSystemLog(msg);

                //  Get node from dropdown
                int index = spinnerNodes.getSelectedItemPosition();
                TaskRequest request = new TaskRequest();
                request.nodes = new ArrayList<>();
                request.nodes.add(mNodes.get(index));
                request.cmd = cmd;
                request.taskName = getString(R.string.msg_sync_date_single_node) + spinnerNodes.getSelectedItem().toString();

                //  Execute command
                executeCommandTask(request);

            }
        });

        //  Button:  SyncDate - All Nodes
        Button btnSyncDateAll = (Button) view.findViewById(R.id.btnSyncDateAll);
        btnSyncDateAll.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                //  Execute the command
                syncDateAll();
            }
        });

        //  Command history
        final Spinner spinnerCommandHistory = (Spinner) view.findViewById(R.id.spinnerCommandHistory);
        spinnerCommandHistory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cmd;
                cmd = parent.getItemAtPosition(position).toString();
                txtExecuteCommand.setText(cmd);     //  Update display with the selected command
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*
         *  Spinner:  Command History
         */
        List<String> commandHistory = new ArrayList<>();
        commandHistory.add("date");
        commandHistory.add("df -handler /dev/sda1");
        commandHistory.add("cd /mnt/motioneye/Front; ls -l");
        commandHistory.add("cd /mnt/motioneye/Rear; ls -l");
        commandHistory.add("cd /mnt/motioneye/Rear-PiCam; ls -l");
        commandHistory.add("top -n1 -b");
        commandHistory.add("ps -eaf");
        commandHistory.add("sudo reboot");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterCmd = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, commandHistory);

        // Drop down layout style - list view with radio button
        dataAdapterCmd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCommandHistory.setAdapter(dataAdapterCmd);

        // App startup calls
        startUp();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_options_ssh_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            //  Clear console
            case R.id.action_clear_console:
                sTxtReply.setText("");
            default:
                break;

        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("DateSynced", sDateSynced);
    }

    //  This override the implemented method from asyncTask
    public void taskFinished(TaskResult result){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.

        //  Dismiss the progress dialog
        if (progressDialog !=null) {
            progressDialog.hide();
            progressDialog = null;
        }

        Log.d(TAG, getString(R.string.msg_task_finished));
        writeTaskResult(result,getString(R.string.msg_task_finished) );

    }

    public void hideProgress() {

        //  Dismiss the progress dialog
        if (progressDialog !=null) {
            progressDialog.hide();
            progressDialog = null;
        }

        Log.d(TAG, "hideProgress");
    }

    public void showProgress() {
        Log.d(TAG, "showProgress");
    }

    public void taskTimeout(TaskResult result) {
        Log.d(TAG, getString(R.string.msg_task_timeout));
        writeTaskResult(result,getString(R.string.msg_task_timeout) );
    }

    public void taskCanceled(TaskResult result) {

        //  Dismiss the progress dialog
        if (progressDialog !=null) {
            progressDialog.hide();
            progressDialog = null;
        }

        Log.d(TAG, getString(R.string.msg_task_cancelled));
        writeTaskResult(result,getString(R.string.msg_task_cancelled) );
    }

    private void writeTaskResult(TaskResult result, String tag) {

        String msg = tag + FileStorageUtils.LINE_SEPARATOR;

        if ( (result.response == null) || (result.response.isEmpty()) ) {
            msg = msg + result.request.taskName + getString(R.string.msg_no_task_results) + FileStorageUtils.LINE_SEPARATOR;
        } else {
            msg = msg + result.request.taskName + getString(R.string.msg_task_results) + FileStorageUtils.LINE_SEPARATOR;
            updateConsoleAndSystemLog(msg);
            updateConsoleAndSystemLog(result.response);
        }

        if ( (result.exception == null) || (result.exception.isEmpty()) ) {
            msg = msg + result.request.taskName + getString(R.string.msg_no_task_exception) + FileStorageUtils.LINE_SEPARATOR;
        } else {
            msg = msg + result.request.taskName + getString(R.string.msg_task_exception) + FileStorageUtils.LINE_SEPARATOR;
            updateConsoleAndSystemLog(msg);
            updateConsoleAndSystemLog(result.exception);
        }

    }

    /**
     * Wraps AsyncTask in a handler to the request can be canceled after period of time.
     * @param request TaskRequest:
     */
    private void executeCommandTask(TaskRequest request) {

        //  Using progress dialog even though is has been depreciated.
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.msg_executing_command));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        ExecuteCommandTask task = new ExecuteCommandTask();
        task.delegate = this;   //  Callback

        //  Wrap task in handler so that we can timeout task
        Handler handler = new Handler();
        TaskTimeout taskTimeout;
        taskTimeout = new TaskTimeout(task);
        handler.postDelayed(taskTimeout, TIMEOUT);

        //  Need to cancel Handler if remote command fails before timeout.
        task.handler = handler;
        task.runnable = taskTimeout;

        //  Execute task
        task.execute(request);
    }

    /**
     * Wraps AsynTask in a handler to the request can be canceled after period of time.
     * @param request TaskRequest:
     */
    private void executePingTask(TaskRequest request) {

        //  Using progress dialog even though is has been depreciated.
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.msg_executing_ping));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        ExecutePingTask task = new ExecutePingTask();
        task.delegate = this;   //  Callback

        //  Wrap task in handler so that we can timeout task
        Handler handler = new Handler();
        TaskTimeout taskTimeout;
        taskTimeout = new TaskTimeout(task);
        handler.postDelayed(taskTimeout, TIMEOUT);

        //  Need to cancel Handler if remote command fails before timeout.
        task.handler = handler;
        task.runnable = taskTimeout;

        //  Execute task
        task.execute(request);
    }


    /**
     * Sync Android date/time with Pi.  Follow with 'date' command to view system date/time.
     */
    private void syncDateAll() {
        final String date = getDateTime();
        final String cmd = "sudo date -s \"" + date + "\" ;date";
        String msg = "";

        //  Prepare system log and console message
        msg = getString(R.string.msg_executing_command) + FileStorageUtils.TABS + cmd + FileStorageUtils.LINE_SEPARATOR;
        msg = msg + getString(R.string.msg_sync_date_all_nodes) + FileStorageUtils.LINE_SEPARATOR;
        updateConsoleAndSystemLog(msg);

        //  All nodes
        TaskRequest request = new TaskRequest();
        request.nodes = mNodes;
        request.cmd = cmd;
        request.taskName = getString(R.string.msg_sync_date_all_nodes);

        //  Execute command
        executeCommandTask(request);

        //  Indicate that date synced so that if we return to this fragment syncDateAll is not called again.
        sDateSynced = true;

    }

    /**
     * Steps performed when app is launched.
     */
    private void startUp() {
        String msg = "";

        //  Networking
        mWiFiUtils = WiFiUtils.getInstance(getActivity());

        // Sync dates
        if (mWiFiUtils.isConnected()) {
            //  Check if syncDateAll already run on fragment create/resume.
            if (!sDateSynced) {
                msg = getString(R.string.msg_network_connected_date_sync) + FileStorageUtils.LINE_SEPARATOR;
                updateConsoleAndSystemLog(msg);

                //  Sync dates
                syncDateAll();
            } else {
                msg = getString(R.string.msg_sync_date_already_performed) + FileStorageUtils.LINE_SEPARATOR;
                updateConsoleAndSystemLog(msg);
            }
        }

    }

    /**
     * Generate date/time stamp.
     * https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
     * "Thu Jan 17 03:19:37 PST 2019"
     * @return String: Formatted date/time:  Ex.  Thu Jan 17 03:19:37 PST 2019"
     */
    private String getDateTime() {
        String dateFormat = "EEE MMM dd hh:mm:ss z yyyy";
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(c.getTime());
    }

    /**
     * Command history to EditText.
     * @param msg String: Message that will be added to command history.
     */
    private void updateConsoleAndSystemLog(String msg) {
        //  Output to system log
        FileStorageUtils.writeSystemLog(getActivity(), mGlobalVariables.SYS_LOG,TAG + FileStorageUtils.TABS + msg);

        //  Output to console
        sTxtReply.append(msg + FileStorageUtils.LINE_SEPARATOR);
    }

}
