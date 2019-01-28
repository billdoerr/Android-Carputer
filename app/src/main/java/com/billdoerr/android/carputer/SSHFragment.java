package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.billdoerr.android.carputer.utils.RPiUtils;

//  TODO:  THIS FRAGMENT IS A TOTAL HACK!!!!
//  TODO :  Hack until I get SettingsActivity to be more robust
public class SSHFragment extends Fragment {

    private static final String TAG = "SSHFragment";

    private static final String PREF_RASPBERRYPI_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_ENABLED";
    private static final String PREF_RASPBERRYPI_IP = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_IP";
    private static final String PREF_RASPBERRYPI_SSH_PORT = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_SSH_PORT";
    private static final String PREF_RASPBERRYPI_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_USERNAME";
    private static final String PREF_RASPBERRYPI_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_PASSWORD";

    //  TODO :  Hack until I get SettingsActivity to be more robust
    private static final String PREF_RASPBERRYPI_IP_2 = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_IP_2";

    //  Widgets
    private EditText txtExecuteCommand;
    private TextView txtReply;
    private Spinner spinnerNodes;

    //  Hosts info
    private String mIP;
    private String mPort;
    private String mUser;
    private String mPwd;
    //  TODO :  Hack until I get SettingsActivity to be more robust
    private String mIP2;
    private String currentNode;

    public static SSHFragment newInstance() {
        return new SSHFragment();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgs();
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
                txtReply.setText(R.string.txt_carputer_mgmt_ssh_command_processing);
//                new ExecuteCommandTask().execute(txtExecuteCommand.getText().toString());
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
                String reply = "";

                //  TODO : Total Hack!!!
                currentNode = mIP2;
                txtExecuteCommand.setText(cmd);
                reply = getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode;
                txtReply.setText(reply);
                new ExecuteCommandTask().execute(cmd);
                Log.d(TAG, getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode);

                currentNode = mIP;
                txtExecuteCommand.setText(cmd);
                reply = reply + "\n" + getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode;
                txtReply.setText(reply);
                new ExecuteCommandTask().execute(cmd);
                Log.d(TAG, getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode);
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
                txtReply.setText(R.string.txt_carputer_mgmt_ssh_command_processing);

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
                txtReply.setText(R.string.txt_carputer_mgmt_ssh_command_processing);   //  Clear current text
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
                txtReply.setText(R.string.txt_carputer_mgmt_ssh_command_processing);
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
        spinnerNodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentNode = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*
        *  Spinner:  Nodes
         */
        //  TODO:  Need to add starter set of commands in SharedPreferences.  Add them in when executed.
        List<String> nodes = new ArrayList<>();
        nodes.add(mIP);
        nodes.add(mIP2);

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
                String cmd = "";
                cmd = parent.getItemAtPosition(position).toString();
                txtExecuteCommand.setText(cmd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*
         *  Spinner:  Command History
         */
        //  TODO:  Need to add starter set of commands in SharedPreferences.  Add them in when executed.
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

        //  Sync Dates
        syncDateAll();

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

    public void syncDateAll() {
        String date = getDateTime();
        //  Sync Android date/time with Pi.  Follow with 'date' command to view system date/time.
        final String cmd = "sudo date -s \"" + date + "\" ;date";
        String reply = "";

        //  TODO : Total Hack!!!
        currentNode = mIP2;
        txtExecuteCommand.setText(cmd);
        reply = getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode;
        txtReply.setText(reply);
        new ExecuteCommandTask().execute(cmd);
        Log.d(TAG, getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode);

        currentNode = mIP;
        txtExecuteCommand.setText(cmd);
        reply = reply + "\n" + getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode;
        txtReply.setText(reply);
        new ExecuteCommandTask().execute(cmd);
        Log.d(TAG, getResources().getString(R.string.txt_carputer_mgmt_ssh_command_processing).toString() + " on node -> " + currentNode);
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        mIP = bundle.getString(PREF_RASPBERRYPI_IP);
        mPort = bundle.getString(PREF_RASPBERRYPI_SSH_PORT);
        mUser = bundle.getString(PREF_RASPBERRYPI_AUTH_USERNAME);
        mPwd = bundle.getString(PREF_RASPBERRYPI_AUTH_PASSWORD);
        //  TODO :  Hack until I get SettingsActivity to be more robust
        mIP2 = bundle.getString(PREF_RASPBERRYPI_IP_2);
    }

    //  Async Task to perform ping command
    private class PingTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "PingTask";

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                RPiUtils utils = new RPiUtils();
                //  TODO :  Hack until I get SettingsActivity to be more robust
//                result = utils.ping(mIP);
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

    //  Async Task to perform ping command
    private class ExecuteCommandTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "ExecuteCommandTask";

        @SuppressLint("StaticFieldLeak")
        @Override
        protected String doInBackground(String... params) {
            String result = "";
                try {
                    RPiUtils utils = new RPiUtils();
                    //  TODO :  Hack until I get SettingsActivity to be more robust
//                    utils.initialize(mIP, mPort, mUser, mPwd);
                    utils.initialize(currentNode, mPort, mUser, mPwd);
                    result = utils.executeRemoteCommand(params[0]);
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

    //  Generate date/time stamp that will be used to create a unique filename
    //  https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
    //  "Thu Jan 17 03:19:37 PST 2019"
    //  "Thu Jan  17 14:37:00 PST 2019"
    private String getDateTime() {
        String dateFormat = "EEE MMM dd hh:mm:ss z yyyy";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(c.getTime());
    }

}
