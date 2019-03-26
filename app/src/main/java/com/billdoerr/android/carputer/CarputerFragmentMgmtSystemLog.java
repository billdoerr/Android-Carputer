package com.billdoerr.android.carputer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.billdoerr.android.carputer.utils.FileStorageUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class CarputerFragmentMgmtSystemLog extends Fragment {

    private static final String TAG = "FragmentSystemLog";

    // Calling Application class (see application tag in AndroidManifest.xml)
    private GlobalVariables mGlobalVariables;

    //  System log viewer
    private TextView txtSystemLog;

    public static CarputerFragmentMgmtSystemLog newInstance() {
        return new CarputerFragmentMgmtSystemLog();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Enable options menu
        setHasOptionsMenu(true);

        // Calling Application class (see application tag in AndroidManifest.xml)
        mGlobalVariables = (GlobalVariables) getActivity().getApplicationContext();

        //  Initialize system logging
        FileStorageUtils.initializeSystemLog(getActivity(), mGlobalVariables.SYS_LOG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_system_log, container, false);

        txtSystemLog = (TextView) view.findViewById(R.id.txt_system_log);

        //  Display system log
        readSystemLog();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.system_log, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            //  Reload system log
            case R.id.action_refresh:
                txtSystemLog.setText("");
                //  Display system log
                readSystemLog();
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_system_log_refresh), Toast.LENGTH_LONG).show();
                return true;

            //  Clear system log
            case R.id.action_clear:
                clearSystemLog();
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_system_log_clear), Toast.LENGTH_LONG).show();
                return true;

            default:
                break;

        }

        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        //  Display system log
        txtSystemLog.setText("");
        readSystemLog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Read the system log and display contents
     */
    private void readSystemLog() {
        //  Read system log
        String sysLog = "";
        sysLog = FileStorageUtils.readSystemLog();
        txtSystemLog.setText(sysLog);
    }

    /**
     * Clear the system log
     */
    private void clearSystemLog() {
        FileStorageUtils.clearSystemLog();
        txtSystemLog.setText("");
        readSystemLog();
    }

}