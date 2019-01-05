package com.billdoerr.android.carputer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.billdoerr.android.carputer.utils.RPiUtils;

public class SSHFragment extends Fragment {

    private static final String TAG = "SSHFragment";

    private static final String PREF_RASPBERRYPI_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_ENABLED";
    private static final String PREF_RASPBERRYPI_IP = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_IP";
    private static final String PREF_RASPBERRYPI_SSH_PORT = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_SSH_PORT";
    private static final String PREF_RASPBERRYPI_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_USERNAME";
    private static final String PREF_RASPBERRYPI_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_RASPBERRYPI_AUTH_PASSWORD";

    //  Widgets
    private EditText txtExecuteCommand;
    private TextView txtReply;

    //  Hosts info
    private String mIP;
    private String mPort;
    private String mUser;
    private String mPwd;

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

        //  Button:  Poweroff
        Button btnPoweroff = (Button) view.findViewById(R.id.btnPoweroff);
        btnPoweroff.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                final String cmd = "sudo poweroff";
                txtExecuteCommand.setText(cmd);
                txtReply.setText(R.string.txt_carputer_mgmt_ssh_command_processing);
//                new ExecuteCommandTask().execute(txtExecuteCommand.getText().toString());
                new ExecuteCommandTask().execute(cmd);
            }
        });


        //  Button:  Execute command
        Button btnExecuteCommand = (Button) view.findViewById(R.id.btnExecuteCommand);
        btnExecuteCommand.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
//                final String cmd = "top";
                String cmd = txtExecuteCommand.getText().toString();
                txtExecuteCommand.setText(cmd);
                txtReply.setText(R.string.txt_carputer_mgmt_ssh_command_processing);
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

        // Spinner:  Command History
        //  TODO:  Need to add starter set of commands in SharedPreferences.  Add them in when executed.
        List<String> commandHistory = new ArrayList<>();
        commandHistory.add("top;q");
        commandHistory.add("ps -eaf");
        commandHistory.add("cd /etc;ls -l");

        final Spinner spinner = (Spinner) view.findViewById(R.id.spinnerCommandHistory);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, commandHistory);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



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

    private void getArgs() {
        Bundle bundle = getArguments();
        mIP = bundle.getString(PREF_RASPBERRYPI_IP);
        mPort = bundle.getString(PREF_RASPBERRYPI_SSH_PORT);
        mUser = bundle.getString(PREF_RASPBERRYPI_AUTH_USERNAME);
        mPwd = bundle.getString(PREF_RASPBERRYPI_AUTH_PASSWORD);
    }

    //  Async Task to perform ping command
    private class PingTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "PingTask";

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                RPiUtils utils = new RPiUtils();
                result = utils.ping(mIP);
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
                    utils.initialize(mIP, mPort, mUser, mPwd);
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

}
