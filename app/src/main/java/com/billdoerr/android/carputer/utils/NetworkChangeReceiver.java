package com.billdoerr.android.carputer.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.billdoerr.android.carputer.R;

/**
 * Broadcast receiver that logs changes with Wifi connectivity.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Calling Application class (see application tag in AndroidManifest.xml)
//        mGlobalVariables = (GlobalVariables) context.getApplicationContext();

        //  Get network SSID
//        mSSID = mGlobalVariables.getNetworkName();

        String msg;     //  System log

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

//        NetworkInfo activeNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        boolean isConnected = ( (activeNetworkInfo != null) && (activeNetworkInfo.isConnectedOrConnecting()) );
//        if (isConnected) {
//            msg = context.getString(R.string.msg_network_connected_to) + activeNetworkInfo.getExtraInfo();
//            Log.d(TAG,msg);
//            writeSystemLog(context, msg);
//        } else {
//            msg = context.getString(R.string.msg_network_connection_dropped);
//            Log.d(TAG,msg);
//            writeSystemLog(context, msg);
//        }

        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if ( (networkInfo != null) && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) ){
                if (networkInfo.isConnected()){
                    msg = context.getString(R.string.msg_network_connected_to) + networkInfo.getExtraInfo();
                    writeSystemLog(context, msg);
                } else {
                    msg = context.getString(R.string.msg_network_connection_dropped);
                    writeSystemLog(context, msg);
                }
            }
        }
    }

    /**
     * Update system log.
     * @param context  Context:  Application context.
     * @param msg  String.  Message to be written to system log.
     */
    private void writeSystemLog(Context context, String msg) {
        //  Output to system log
        FileStorageUtils.writeSystemLog(context, GlobalVariables.SYS_LOG,TAG + FileStorageUtils.TABS + msg);
    }

}