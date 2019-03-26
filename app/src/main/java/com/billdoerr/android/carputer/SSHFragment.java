package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
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
import com.billdoerr.android.carputer.taskutils.AsyncTaskResponse;
import com.billdoerr.android.carputer.taskutils.ExecuteCommandTask;
import com.billdoerr.android.carputer.taskutils.ExecutePingTask;
import com.billdoerr.android.carputer.taskutils.TaskCanceler;
import com.billdoerr.android.carputer.taskutils.TaskRequest;
import com.billdoerr.android.carputer.taskutils.TaskResult;
import com.billdoerr.android.carputer.utils.FileStorageUtils;
import com.billdoerr.android.carputer.utils.WiFiUtils;

/**
 *  Child fragment of CarputerFragmentMgmt.
 *  Use to perform simple remote operations on configured nodes.
 */
public class SSHFragment extends Fragment implements AsyncTaskResponse {

    private static final String TAG = "SSHFragment";
    private static final long TIMEOUT = 30*1000;     //  Time in milliseconds

    // Calling Application class (see application tag in AndroidManifest.xml)
    private GlobalVariables mGlobalVariables;

    private List<Node> mNodes = new ArrayList<Node>();
    private WiFiUtils mWiFiUtils;

    //  Widgets
    private EditText txtExecuteCommand;
    private TextView txtReply;
    private Spinner spinnerNodes;

    //  Misc
    private boolean mDateSynced = false;

    ProgressDialog progressDialog;
    TaskResult taskResults;

    ExecuteCommandTask mExecuteCommandTask = new ExecuteCommandTask();
    ExecutePingTask mExecutePingTask = new ExecutePingTask();


    public static SSHFragment newInstance() {
        return new SSHFragment();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        View view = inflater.inflate(R.layout.fragment_ssh, container, false);

        //  TextView:  Reply
        txtReply = (TextView) view.findViewById(R.id.txtReply);

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
                p.taskName = getString(R.string.msg_executing_ping);

                // Perform ping
//                ExecutePingTask pingTask = new ExecutePingTask();
//                new ExecutePingTask().execute(p);
                executePingTask(p);
            }
        });

        //  Button:  Poweroff (Single)
        Button btnPoweroffSingle = (Button) view.findViewById(R.id.btnPoweroffSingle);
        btnPoweroffSingle.setOnClickListener(new View.OnClickListener() {
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

        //  Button:  Poweroff (All)
        Button btnPoweroffAll = (Button) view.findViewById(R.id.btnPoweroffAll);
        btnPoweroffAll.setOnClickListener(new View.OnClickListener() {
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
                TaskRequest p = new TaskRequest();
                p.nodes = mNodes;
                p.cmd = cmd;
                p.taskName = getString(R.string.msg_power_off_all_nodes);

                //  Execute command
                executeCommandTask(p);
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
                TaskRequest p = new TaskRequest();
                p.nodes = new ArrayList<>();
                p.nodes.add(mNodes.get(index));
                p.cmd = cmd;
                p.taskName = msg;

                //  Execute command
                executeCommandTask(p);
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
                TaskRequest p = new TaskRequest();
                p.nodes = new ArrayList<>();
                p.nodes.add(mNodes.get(index));
                p.cmd = cmd;
                p.taskName = getString(R.string.msg_sync_date_single_node) + spinnerNodes.getSelectedItem().toString();

                //  Execute command
                executeCommandTask(p);

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

        //  Nodes
        spinnerNodes = (Spinner) view.findViewById(R.id.spinnerNodes);
//        spinnerNodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                mCurrentNodeIndex = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        /*
         *  Spinner:  Nodes
         */
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < mNodes.size(); i++) {
            nodes.add(mNodes.get(i).getIp());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterNodes = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nodes);

        // Drop down layout style
        dataAdapterNodes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerNodes.setAdapter(dataAdapterNodes);

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

//        //  TODO:  UNDER CONSTRUCTION - SystemStatus
//        //  Button:  System Status
//        Button btnSystemStatus = (Button) view.findViewById(R.id.btnSystemStatus);
//        btnSystemStatus.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            public void onClick(View v) {
//                //  Display dialog
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                CarputerMgmtFragmentSystemStatus dialogSystemStatus = CarputerMgmtFragmentSystemStatus.newInstance(getString(R.string.fragment_system_status));
//                dialogSystemStatus.show(fm, getString(R.string.fragment_system_status));
//            }
//        });

        /*
         *  Spinner:  Command History
         */
        List<String> commandHistory = new ArrayList<>();
        commandHistory.add("date");
        commandHistory.add("df -h /dev/sda1");
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

    //  This override the implemented method from asyncTask
//    @Override
    public void processFinish(TaskResult taskResults){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.

        progressDialog.hide();
        progressDialog = null;

        txtReply.append("Wheeeeeeeeee!");
        updateConsoleAndSystemLog("Wheeeeeeeeee!");

    }

    /**
     * Wraps AsynTask in a handler to the request can be canceled after period of time.
     * @param p TaskRequest:
     */
    private void executeCommandTask(TaskRequest p) {

        //  Using progress dialog even though is has been depreciated.
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.msg_executing_command));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        ExecuteCommandTask task = new ExecuteCommandTask();
        task.delegate = this;

        Handler handler = new Handler();
        TaskCanceler taskCanceler;
        taskCanceler = new TaskCanceler(task);
        handler.postDelayed(taskCanceler, TIMEOUT);

        //  TODO:  I don't know if this is good technique.  Need to cancel Handler if remote command fails before timeout.
        task.h = handler;
        task.r = taskCanceler;

        //  Execute task
        task.execute(p);
    }

    /**
     * Wraps AsynTask in a handler to the request can be canceled after period of time.
     * @param p TaskRequest:
     */
    private void executePingTask(TaskRequest p) {

        //  Using progress dialog even though is has been depreciated.
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.msg_executing_ping));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        ExecutePingTask task = new ExecutePingTask();
        task.delegate = this;

        Handler handler = new Handler();
        TaskCanceler taskCanceler;
        taskCanceler = new TaskCanceler(task);
        handler.postDelayed(taskCanceler, TIMEOUT);

        //  TODO:  I don't know if this is good technique.  Need to cancel Handler if remote command fails before timeout.
        task.h = handler;
        task.r = taskCanceler;

        //  Execute task
        task.execute(p);
    }


    /**
     * Sync Android date/time with Pi.  Follow with 'date' command to view system date/time.
     */
    private void syncDateAll() {
        final String date = getDateTime();
        final String cmd = "sudo date -s \"" + date + "\" ;date";

        //  Prepare system log and console message
        String msg = getString(R.string.msg_executing_command) + FileStorageUtils.TABS + cmd + FileStorageUtils.LINE_SEPARATOR;
        msg = msg + getString(R.string.msg_sync_date_all_nodes) + FileStorageUtils.LINE_SEPARATOR;
        updateConsoleAndSystemLog(msg);

        //  All nodes
        TaskRequest p = new TaskRequest();
        p.nodes = mNodes;
        p.cmd = cmd;
        p.taskName = getString(R.string.msg_sync_date_all_nodes);

        //  Check if date already synced.
        if (mDateSynced) {
            msg = msg + "syncDateAll: Date already synced!";
            updateConsoleAndSystemLog(msg);
            return;
        }

        //  Execute command
        executeCommandTask(p);

        mDateSynced = true;
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
            msg = getString(R.string.msg_network_connected_date_sync) + FileStorageUtils.LINE_SEPARATOR;
            updateConsoleAndSystemLog(msg);
//            syncDateAll();
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
        FileStorageUtils.writeSystemLog(TAG + FileStorageUtils.TABS + msg);

        //  Output to console
        txtReply.append(msg + FileStorageUtils.LINE_SEPARATOR);
    }

    private void updateConsoleAndSystemLog(String tag, String msg) {
        //  Output to system log
        FileStorageUtils.writeSystemLog(tag + FileStorageUtils.TABS + msg);

        //  Output to console
        txtReply.append(msg + FileStorageUtils.LINE_SEPARATOR);
    }

}
