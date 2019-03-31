package com.billdoerr.android.carputer.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * WIFI network utilities.  Singleton class.
 */
public class WifiUtils {

    private static final String TAG = "WifiUtils";

    private static WifiUtils sWifiUtils;

    private static boolean mIsConnected = false;

    private final boolean mConnectionMade = false;
    private int mNetworkId = -1;

    /**
     * WiFi connection utility class.
     * Singleton class
     * @param context Context:  Application context.
     * @return WifiUtils:  WifiUtils object.
     */
    public static WifiUtils getInstance(Context context) {
        if (sWifiUtils == null) {
            sWifiUtils = new WifiUtils(context);
        }
        return sWifiUtils;
    }

    private WifiUtils(Context context) {
        //  Required
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    /**
     * Connect to network (WPA).  Also performs WifiUtils lock if shared preference enabled.
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

//        //  Add network if not already configured
//        if (!isNetworkConfigured(context, networkSSID)) {
//            mNetworkId = addNetwork(context,networkSSID, networkPassphrase);
//        }

        // If valid networkId
//        if (mNetworkId > -1) {

        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //  And finally, you might need to enable it, so Android connects to it:
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        //  Exit if no networks
        if (list == null) {
            return false;
        }

        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
//                if (i.networkId == mNetworkId) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                isConnected = wifiManager.reconnect();
                break;
            }
        }

//        }

        return mIsConnected = isConnected;
    }

    /**
     * Disconnect to network (WPA).  Also performs WifiUtils lock if shared preference enabled.
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


}
