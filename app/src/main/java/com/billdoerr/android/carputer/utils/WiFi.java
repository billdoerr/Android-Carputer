package com.billdoerr.android.carputer.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

public class WiFi {

    private static final String TAG = "WiFi";

    //  TODO : Should be passed in as parameters which are stored in SharedPreferences
    private static final String NETWORK_SSID = "PINET";
    private static final String NETWORK_PASSWORD = "scoobydoo";

    private boolean mConnectionMade = false;

    //  Connect to PINET (WPA)
    //  Refer to:  https://developer.android.com/reference/android/net/wifi/WifiConfiguration
    //  Refer to:  https://stackoverflow.com/questions/8818290/how-do-i-connect-to-a-specific-wi-fi-network-in-android-programmatically
    //
    public boolean connectToPINET(Context context) {

        //  You need to create WifiConfiguration instance like this:
        String networkSSID = NETWORK_SSID;
        String networkPass = NETWORK_PASSWORD;


        WifiConfiguration conf = new WifiConfiguration();
        //  The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"),
        // or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405).
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        //  For WPA network you need to add passphrase like this:
        conf.preSharedKey = "\""+ networkPass +"\"";

        //  Then, you need to add it to Android wifi manager settings:
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        //  And finally, you might need to enable it, so Android connects to it:
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                mConnectionMade = wifiManager.reconnect();

                break;
            }
        }
            return mConnectionMade;
    }
}
