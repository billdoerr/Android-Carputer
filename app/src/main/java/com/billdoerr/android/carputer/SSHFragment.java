package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
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
import com.billdoerr.android.carputer.utils.NodeUtils;
import com.billdoerr.android.carputer.utils.WiFiUtils;

/**
 *  Child fragment of CarputerFragmentMgmt.
 *  Use to perform simple remote operations on configured nodes.
 */
public class SSHFragment extends Fragment {

    private static final String TAG = "SSHFragment";

    private static final String ARGS_NODE_DETAIL = "ARGS_NODE_DETAIL";

    // Calling Application class (see application tag in AndroidManifest.xml)
    private GlobalVariables mGlobalVariables;

    private static List<Node> mNodes = new ArrayList<Node>();
    private WiFiUtils mWiFiUtils;

    //  Widgets
    private EditText txtExecuteCommand;
    private TextView txtReply;
    private Spinner spinnerNodes;

    //  Misc
    private static boolean mDateSynced = false;
    private static String mCmdHistory = "";

    //  Class:  Task
    private static class Payload {
        public String task;
        public List<Node> nodes;
    }

    public static SSHFragment newInstance() {
        return new SSHFragment();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //  Button:  Poweroff (Single)
        Button btnPoweroffSingle = (Button) view.findViewById(R.id.btnPoweroffSingle);
        btnPoweroffSingle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                final String cmd = "sudo shutdown -h 0";
                txtExecuteCommand.setText(cmd);
                updateCommandHistory(getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing));
                new ExecuteCommandTask().execute(cmd);
            }
        });

        //  Button:  Poweroff (All)
        Button btnPoweroffAll = (Button) view.findViewById(R.id.btnPoweroffAll);
        btnPoweroffAll.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                final String cmd = "sudo shutdown -h 0";
                Payload p = new Payload();
                p.nodes = mNodes;
                p.task = cmd;
                updateCommandHistory(getString(R.string.txt_carputer_mgmt_ssh_poweroff_all));
                new ExecuteCommandTaskNew().execute(p);
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
                updateCommandHistory(getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing));

                //  Hide soft keyboard
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                //  Execute task
                new ExecuteCommandTask().execute(cmd);
            }
        });

        //  Button:  Ping
        Button btnPing = (Button) view.findViewById(R.id.btnPing);
        btnPing.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                updateCommandHistory(getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing));
                // Perform ping
                new PingTask().execute();
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
                updateCommandHistory(getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing));

                new ExecuteCommandTask().execute(cmd);

            }
        });

        //  Button:  SyncDate - All Nodes
        Button btnSyncDateAll = (Button) view.findViewById(R.id.btnSyncDateAll);
        btnSyncDateAll.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
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

        // Drop down layout style - list view with radio button
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
                txtExecuteCommand.setText(cmd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //  Button:  System Status
        Button btnSystemStatus = (Button) view.findViewById(R.id.btnSystemStatus);
        btnSystemStatus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                //  Display dialog
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CarputerMgmtFragmentSystemStatus dialogSystemStatus = CarputerMgmtFragmentSystemStatus.newInstance(getString(R.string.fragment_system_status));
                dialogSystemStatus.show(fm, getString(R.string.fragment_system_status));
            }
        });

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

    /**
     * Async Task to perform ping command.
     */
    private class PingTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "PingTask";

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                NodeUtils utils = new NodeUtils();
                String currentNode = spinnerNodes.getSelectedItem().toString();
                result = utils.ping(currentNode);
                Log.d(TAG, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            txtReply.setText(result);
        }
    }

    /**
     * TODO : Get rid of this.  Rename ExecuteCommandTaskNew.
     * Async Task to perform ping command.
     * android.os.AsyncTask<Params, Progress, Result>.
     */
    private class ExecuteCommandTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "ExecuteCommandTask";

        @SuppressLint("StaticFieldLeak")
        @Override
        protected String doInBackground(String... params) {
            String result = "";
                try {
                    NodeUtils utils = new NodeUtils();
                    int i = spinnerNodes.getSelectedItemPosition();
                    utils.initialize(mNodes.get(i).getIp(), mNodes.get(i).getSSHPort(),
                            mNodes.get(i).getUser(), mNodes.get(i).getPassword());
                    result = utils.executeRemoteCommand(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            updateCommandHistory(result);
        }
    }

    /**
     * Async Task to perform ping command.
     * android.os.AsyncTask<Params, Progress, Result>.
     *
     */
    private class ExecuteCommandTaskNew extends AsyncTask<Payload, Void, String> {

        private static final String TAG = "PowerOffAll";

        @SuppressLint("StaticFieldLeak")
        @Override
        protected String doInBackground(Payload... params) {
            String result = "";
            Payload p = params[0];
            for (int i = 0; i < p.nodes.size(); i++) {
                try {
                    NodeUtils utils = new NodeUtils();
                    utils.initialize(p.nodes.get(i).getIp(), p.nodes.get(i).getSSHPort(),
                            p.nodes.get(i).getUser(), p.nodes.get(i).getPassword());
                    result = utils.executeRemoteCommand(p.task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            updateCommandHistory(result);
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
     * Sync Android date/time with Pi.  Follow with 'date' command to view system date/time.
     */
    private void syncDateAll() {
        String date = getDateTime();
        String reply = "";
        final String cmd = "sudo date -s \"" + date + "\" ;date";

        //  Check if date already synced.
        if (mDateSynced) {
            reply = reply + "syncDateAll: Date already synced!";
            updateCommandHistory(reply);
            return;
        }

        reply = reply + "Syncing date on all nodes...\n";
        updateCommandHistory(reply);

        Payload p = new Payload();
        p.nodes = mNodes;
        p.task = cmd;
        new ExecuteCommandTaskNew().execute(p);

        mDateSynced = true;
    }

    /**
     * Command history to EditText.
     * @param msg String: Message that will be added to command history.
     */
    private void updateCommandHistory(String msg) {
        mCmdHistory = mCmdHistory + "\n" + msg + "\n";
        txtReply.setText(mCmdHistory);
    }

    //  TODO:  Create initialization function and add the below.  Move to main activity.
    /**
     * Steps performed when app is launched.
     */
    private void startUp() {

        //  Networking
        mWiFiUtils = WiFiUtils.getInstance(getActivity());

        // Sync dates
        if (mWiFiUtils.isConnected()) {
            updateCommandHistory(getString(R.string.msg_network_connected_date_sync));
            syncDateAll();
        }

    }

}
