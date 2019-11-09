package com.billdoerr.android.carputer.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.billdoerr.android.carputer.settings.Camera;
import com.billdoerr.android.carputer.settings.Node;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;

/**
 * Define global variables is by extending the Application class.
 * This is the base class for maintaining global application state.
 */
@SuppressWarnings("unused")
public class GlobalVariables extends Application {

    private static final String TAG = "GlobalVariables";

    //  Shared preferences
    public static final String PREF_KEY_CAMERAS                = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERAS";
    public static final String PREF_KEY_NODES                  = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODES";
    public static final String PREF_KEY_NETWORK_ENABLED        = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NETWORK_ENABLED";
    public static final String PREF_KEY_NETWORK_NAME           = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NETWORK_NAME";
    public static final String PREF_KEY_NETWORK_PASSPHRASE     = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NETWORK_PASSPHRASE";
    public static final String PREF_KEY_KEEP_DEVICE_AWAKE      = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_KEEP_DEVICE_AWAKE";
    @SuppressWarnings("WeakerAccess")
    public static final String PREF_IMAGE_ARCHIVE_URL          = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_IMAGE_ARCHIVE_URL";

    //  System log filename
    public static final String SYS_LOG = "system_log.log";

    //  Variables to store shared preferences
    private static List<Camera> sCameras = new ArrayList<>();

    private static String sCamerasJson;
    private static List<Node> sNodes = new ArrayList<>();
    private static String sNodesJson;
    private static boolean sNetworkEnabled = false;
    private static String sNetworkName;
    private static String sNetworkPassphrase;
    private static boolean sKeepDeviceAwake = false;
    private static String sImageArchiveUrl;

    //  We need application context.  Set in onCreate()
    private Context sContext;

    private static boolean mIsInitialized = false;

    //  Setters/Getters - some not used but there for future use

    public boolean getIsInitialized() {
        return mIsInitialized;
    }

    public void setIsInitialized(boolean b) {
        mIsInitialized = b;
    }

    public Context getContext() {
        return sContext;
    }

    public List<Camera> getCameras() {
        return sCameras;
    }

    public void setCameras(List<Camera> cameras) {
        sCameras = cameras;
        saveCamerasToSharedPrefs(sContext);
    }

    public List<Node> getNodes() {
        return sNodes;
    }

    public void setNodes(List<Node> nodes) {
        sNodes = nodes;
        saveNodesToSharedPrefs(sContext);
    }

    public String getCamerasJson() {
        return sCamerasJson;
    }

    public void setCamerasJson(String mCamerasJson) {
        GlobalVariables.sCamerasJson = mCamerasJson;
    }

    public String getNodesJson() {
        return sNodesJson;
    }

    public void setNodesJson(String mNodesJson) {
        GlobalVariables.sNodesJson = mNodesJson;
    }

    public boolean isNetworkEnabled() {
        return sNetworkEnabled;
    }

    public void setNetworkEnabled(boolean mNetworkEnabled) {
        GlobalVariables.sNetworkEnabled = mNetworkEnabled;
    }

    public String getNetworkName() {
        return sNetworkName;
    }

    public static void setNetworkName(String mNetworkName) {
        GlobalVariables.sNetworkName = mNetworkName;
    }

    public String getNetworkPassphrase() {
        return sNetworkPassphrase;
    }

    public void setNetworkPassphrase(String mNetworkPassphrase) {
        GlobalVariables.sNetworkPassphrase = mNetworkPassphrase;
    }

    public boolean isKeepDeviceAwake() {
        return sKeepDeviceAwake;
    }

    public void setKeepDeviceAwake(boolean mKeepDeviceAwake) {
        GlobalVariables.sKeepDeviceAwake = mKeepDeviceAwake;
    }

    public String getImageArchiveUrl() {
        return sImageArchiveUrl;
    }

    public void setImageArchiveUrl(String imageArchiveUrl) {
        sImageArchiveUrl = imageArchiveUrl;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //  We need the application context
        sContext = getApplicationContext();

        //  Read in the shared preferences
        getSharedPreferences(sContext);

    }

    /**
     * Get shared preferences.
     * @param context Context:  Application context.
     */
    private void getSharedPreferences(Context context) {

        SharedPreferences appSharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

        sCameras = getCamerasFromSharedPrefs(context);
        sNodes = getNodesFromSharedPrefs(context);

        sCamerasJson = appSharedPrefs.getString(PREF_KEY_CAMERAS, "");
        sNodesJson = appSharedPrefs.getString(PREF_KEY_NODES, "");

        sNetworkEnabled = appSharedPrefs.getBoolean(PREF_KEY_NETWORK_ENABLED, false);
        sNetworkName = appSharedPrefs.getString(PREF_KEY_NETWORK_NAME, "");
        sNetworkPassphrase = appSharedPrefs.getString(PREF_KEY_NETWORK_PASSPHRASE, "");
        sKeepDeviceAwake = appSharedPrefs.getBoolean(PREF_KEY_KEEP_DEVICE_AWAKE, false);
        sImageArchiveUrl = appSharedPrefs.getString(PREF_IMAGE_ARCHIVE_URL, "");

    }

    /**
     * Retrieve list of node's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     * @return List<Node>:  Returns List<Node> of configured nodes.
     */
    private List<Node> getNodesFromSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(Node.PrefKey.PREF_KEY_NODES, "");
        List<Node> nodes = gson.fromJson(json, new TypeToken<ArrayList<Node>>(){}.getType());
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        return nodes;
    }

    /**
     * Save list of node's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     */
    private void saveNodesToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sNodes); //tasks is an ArrayList instance variable
        prefsEditor.putString(Node.PrefKey.PREF_KEY_NODES, json);
        prefsEditor.apply();
    }

    /**
     * Retrieve list of camera's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     * @return List<Camera>:  Returns List<Camera> of configured cameras.
     */
    private List<Camera> getCamerasFromSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(Camera.PrefKey.PREF_KEY_CAMERAS, "");
        List<Camera> cameras = gson.fromJson(json, new TypeToken<ArrayList<Camera>>(){}.getType());
        if (cameras == null) {
            cameras = new ArrayList<>();
        }
        return cameras;
    }

    /**
     * Save list of camera's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     */
    private void saveCamerasToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sCameras); //tasks is an ArrayList instance variable
        prefsEditor.putString(Camera.PrefKey.PREF_KEY_CAMERAS, json);
        prefsEditor.apply();
    }

}
