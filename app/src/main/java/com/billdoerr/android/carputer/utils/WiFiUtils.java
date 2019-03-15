package com.billdoerr.android.carputer.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * WIFI network utilities.
 */
public class WiFiUtils {

    private static final String TAG = "WiFiUtils";
//    private static final String WIFI_LOCK = "WIFI_LOCK";

    private static WifiManager.WifiLock mWifiLock = null;

    private boolean mConnectionMade = false;
    private int mNetworkId = -1;

    /**
     * Connect to network (WPA).  Also performs WiFiUtils lock if shared preference enabled.
     * Refer to:  https://developer.android.com/reference/android/net/wifi/WifiConfiguration.
     * Refer to:  https://stackoverflow.com/questions/8818290/how-do-i-connect-to-a-specific-wi-fi-network-in-android-programmatically.
     * @param context Context:  Application context.
     * @param networkSSID String:  The network's SSID. Can either be a UTF-8 string, which must be
     *                    enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex
     *                    digits, which are not enclosed in quotes (e.g., 01a243f405).
     * @param networkPassphrase String:  WPA passphrase.
     * @return boolean: True if successful connection.
     */
    public boolean connectWPA(Context context, String networkSSID, String networkPassphrase ) {

        boolean isConnected = false;

        //  Add network if not already configured
        if (!isNetworkConfigured(context, networkSSID)) {
            mNetworkId = addNetwork(context,networkSSID, networkPassphrase);
        }

        // If valid networkId
        if (mNetworkId > -1) {

            WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            //  And finally, you might need to enable it, so Android connects to it:
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

            //  Exit if no networks
            if (list == null) {
                return false;
            }

            for( WifiConfiguration i : list ) {
//                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                if (i.networkId == mNetworkId) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    isConnected = wifiManager.reconnect();
                    break;
                }
            }

        }

        return isConnected;
    }

    /**
     * Disconnect to network (WPA).  Also performs WiFiUtils lock if shared preference enabled.
     * Refer to:  https://developer.android.com/reference/android/net/wifi/WifiConfiguration.
     * Refer to:  https://stackoverflow.com/questions/8818290/how-do-i-connect-to-a-specific-wi-fi-network-in-android-programmatically.
     * @param context Context:  Application context.
     * @return boolean: True if successful disconnection.
     */
    public boolean disconnectWPA(Context context) {

        boolean isConnected = mConnectionMade;

        // If valid networkId
        if (mNetworkId > -1) {

            WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            //  And finally, you might need to enable it, so Android connects to it:
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

            //  Exit if no networks
            if (list == null) {
                return false;
            }

            //  Iterate over networks
            for( WifiConfiguration i : list ) {
                if (i.networkId == mNetworkId) {
                    isConnected = wifiManager.disconnect();
                    break;
                }
            }

        }

        return isConnected;
    }

    /**
     * Add network configuration.
     * @param context Context:  Application context.
     * @param networkSSID String:  The network's SSID. Can either be a UTF-8 string, which must be
     *                    enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex
     *                    digits, which are not enclosed in quotes (e.g., 01a243f405).
     * @param networkPassphrase String:  WPA passphrase.
     * @return int:  Network id of added network.
     */
    public int addNetwork(Context context, String networkSSID, String networkPassphrase) {

        WifiConfiguration conf = new WifiConfiguration();

        //  The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"),
        // or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405).
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        //  For WPA network you need to add passphrase like this:
        conf.preSharedKey = "\""+ networkPassphrase +"\"";

        //  Then, you need to add it to Android wifi manager settings:
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return wifiManager.addNetwork(conf);

    }

    /**
     *
     * @param context Context:  Applicaton context.
     * @param networkSSID String:  The network's SSID. Can either be a UTF-8 string, which must be enclosed
     *                    in double quotation marks (e.g., "MyNetwork"), or a string of hex digits,
     *                    which are not enclosed in quotes (e.g., 01a243f405).
     * @return return boolean:  True if network has been added and configured.
     */
    public boolean isNetworkConfigured(Context context, String networkSSID) {

        boolean isConnected = false;

        WifiConfiguration conf = new WifiConfiguration();

        //  The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"),
        // or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405).
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        //  Then, you need to add it to Android wifi manager settings:
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //  And finally, you might need to enable it, so Android connects to it:
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        //  Exit if no networks
        if (list == null) {
            return false;
        }

        //  Iterate over networks
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                //  Network must be configured
                isConnected = true;
                mNetworkId = i.networkId;
                break;
            }
        }

        return isConnected;
    }

//    /***
//     * Calling this method will aquire the lock on wifi. This is avoid wifi
//     * from going to sleep as long as <code>releaseWifiLock</code> method is called.
//     * @param context Context:  Application context.
//     **/
//    public void holdWifiLock(Context context) {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//
//        if( mWifiLock == null )
//            mWifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
//
//        mWifiLock.setReferenceCounted(false);
//
//        if( !mWifiLock.isHeld() )
//            mWifiLock.acquire();
//    }

//    /***
//     * Calling this method will release if the lock is already help. After this method is called,
//     * the Wifi on the device can goto sleep.
//     **/
//    public void releaseWifiLock() {
//
//        if( mWifiLock == null )
//            Log.w(TAG, "#releaseWifiLock mWifiLock was not created previously");
//
//        if( mWifiLock != null && mWifiLock.isHeld() ){
//            mWifiLock.release();
//            //mWifiLock = null;
//        }
//
//    }

}
