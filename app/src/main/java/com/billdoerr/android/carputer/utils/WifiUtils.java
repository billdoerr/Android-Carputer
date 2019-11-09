package com.billdoerr.android.carputer.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * WIFI network utilities.  Singleton class.
 */
public class WifiUtils {

    private static WifiUtils sWifiUtils;

    private static boolean mIsConnected = false;

//    private final boolean mConnectionMade = false;
//    private int mNetworkId = -1;

    /**
     * WiFi connection utility class.
     * Singleton class
     * @return WifiUtils:  WifiUtils object.
     */
    public static WifiUtils getInstance() {
        if (sWifiUtils == null) {
            sWifiUtils = new WifiUtils();
        }
        return sWifiUtils;
    }

    private WifiUtils() {
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
    @SuppressWarnings("unused")
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

//    /**
//     * Disconnect to network (WPA).  Also performs WifiUtils lock if shared preference enabled.
//     * Refer to:  https://developer.android.com/reference/android/net/wifi/WifiConfiguration.
//     * Refer to:  https://stackoverflow.com/questions/8818290/how-do-i-connect-to-a-specific-wi-fi-network-in-android-programmatically.
//     * @param context Context:  Application context.
//     * @return boolean: True if successful disconnection.
//     */
//    public boolean disconnectWPA(Context context) {
//
//        boolean isConnected = mConnectionMade;
//
//        // If valid networkId
//        if (mNetworkId > -1) {
//
//            WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//            //  And finally, you might need to enable it, so Android connects to it:
//            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
//
//            //  Exit if no networks
//            if (list == null) {
//                return false;
//            }
//
//            //  Iterate over networks
//            for( WifiConfiguration i : list ) {
//                if (i.networkId == mNetworkId) {
//                    isConnected = wifiManager.disconnect();
//                    break;
//                }
//            }
//
//        }
//
//        return isConnected;
//    }

//    /**
//     * Add network configuration.
//     * @param context Context:  Application context.
//     * @param networkSSID String:  The network's SSID. Can either be a UTF-8 string, which must be
//     *                    enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex
//     *                    digits, which are not enclosed in quotes (e.g., 01a243f405).
//     * @param networkPassphrase String:  WPA passphrase.
//     * @return int:  Network id of added network.
//     */
//    public int addNetwork(Context context, String networkSSID, String networkPassphrase) {
//
//        WifiConfiguration conf = new WifiConfiguration();
//
//        //  The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"),
//        // or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405).
//        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
//
//        //  For WPA network you need to add passphrase like this:
//        conf.preSharedKey = "\""+ networkPassphrase +"\"";
//
//        //  Then, you need to add it to Android wifi manager settings:
//        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        return wifiManager.addNetwork(conf);
//
//    }

//    /**
//     *
//     * @param context Context:  Application context.
//     * @param networkSSID String:  The network's SSID. Can either be a UTF-8 string, which must be enclosed
//     *                    in double quotation marks (e.g., "MyNetwork"), or a string of hex digits,
//     *                    which are not enclosed in quotes (e.g., 01a243f405).
//     * @return return boolean:  True if network has been added and configured.
//     */
//    public boolean isNetworkConfigured(Context context, String networkSSID) {
//
//        boolean isConnected = false;
//
//        WifiConfiguration conf = new WifiConfiguration();
//
//        //  The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"),
//        // or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405).
//        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
//
//        //  Then, you need to add it to Android wifi manager settings:
//        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        //  And finally, you might need to enable it, so Android connects to it:
//        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
//
//        //  Exit if no networks
//        if (list == null) {
//            return false;
//        }
//
//        //  Iterate over networks
//        for( WifiConfiguration i : list ) {
//            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
//                //  Network must be configured
//                isConnected = true;
//                mNetworkId = i.networkId;
//                break;
//            }
//        }
//
//        return isConnected;
//    }

//    public static void setAPN(Context paramContext, boolean enable) {
//        try {
//            ConnectivityManager connectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//            Method setMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//            setMobileDataEnabledMethod.setAccessible(true);
//            setMobileDataEnabledMethod.invoke(connectivityManager, enable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    /*
//     * https://stackoverflow.com/questions/32604826/how-to-disable-enable-mobile-data-from-within-app-android
//     * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
//     */
//    public static void setMobileDataEnabled(Context context, boolean enabled) {
//        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        Class conmanClass = null;
//        try {
//            conmanClass = Class.forName(conman.getClass().getName());
//
//            Field iConnectivityManagerField = null;
//            try {
//                iConnectivityManagerField = conmanClass.getDeclaredField("mService");
//                iConnectivityManagerField.setAccessible(true);
//
//                Object iConnectivityManager = null;
//                try {
//                    iConnectivityManager = iConnectivityManagerField.get(conman);
//
//                    Class iConnectivityManagerClass = null;
//                    try {
//                        iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
//
//                        Method setMobileDataEnabledMethod = null;
//                        try {
//                            setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//                            setMobileDataEnabledMethod.setAccessible(true);
//
//                            try {
//                                setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
//                            } catch (IllegalAccessException e) {
//                                Log.e(TAG, e.getMessage());
//                            } catch (InvocationTargetException e) {
//                                Log.e(TAG, e.getMessage());
//                            }
//
//                        } catch (NoSuchMethodException e) {
//                            e.printStackTrace();
//                        }
//
//                    } catch (ClassNotFoundException e) {
//                        Log.e(TAG, e.getMessage());
//                    }
//
//                } catch (IllegalAccessException e) {
//                    Log.e(TAG, e.getMessage());
//                }
//
//            } catch (NoSuchFieldException e) {
//                Log.e(TAG, e.getMessage());
//            }
//
//        } catch (ClassNotFoundException e) {
//            Log.e(TAG, e.getMessage());
//        }
//
//    }


}
