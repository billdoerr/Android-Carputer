package com.billdoerr.android.carputer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.billdoerr.android.carputer.settings.Camera;
import com.billdoerr.android.carputer.settings.Node;
import com.billdoerr.android.carputer.utils.FileStorageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;

/**
 * Define global variables is by extending the Application class.
 * This is the base class for maintaining global application state.
 */
public class GlobalVariables extends Application {

    private static final String TAG = "GlobalVariables";

    //  Shared preferences
    public static final String PREF_KEY_CAMERAS                = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERAS";
    public static final String PREF_KEY_NODES                  = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODES";
    public static final String PREF_KEY_NETWORK_ENABLED        = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NETWORK_ENABLED";
    public static final String PREF_KEY_NETWORK_NAME           = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NETWORK_NAME";
    public static final String PREF_KEY_NETWORK_PASSPHRASE     = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NETWORK_PASSPHRASE";
    public static final String PREF_KEY_KEEP_DEVICE_AWAKE      = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_KEEP_DEVICE_AWAKE";

    //  System log filename
    public static final String SYS_LOG = "system_log.log";

    //  Variables to store shared preferences
    private static List<Camera> mCameras = new ArrayList<Camera>();

    private static String mCamerasJson;
    private static List<Node> mNodes = new ArrayList<Node>();
    private static String mNodesJson;
    private static boolean mNetworkEnabled = false;
    private static String mNetworkName;
    private static String mNetworkPassphrase;
    private static boolean mKeepDeviceAwake = false;

    //  We need application context.  Set in onCreate()
    private static Context mContext;

    //  Setters/Getters - some not used but there for future use

    public List<Camera> getCameras() {
        return mCameras;
    }

    public void setCameras(List<Camera> cameras) {
        mCameras = cameras;
        saveCamerasToSharedPrefs(mContext);
    }

    public List<Node> getNodes() {
        return mNodes;
    }

    public void setNodes(List<Node> nodes) {
        mNodes = nodes;
        saveNodesToSharedPrefs(mContext);
    }

    public static String getCamerasJson() {
        return mCamerasJson;
    }

    public static void setCamerasJson(String mCamerasJson) {
        GlobalVariables.mCamerasJson = mCamerasJson;
    }

    public static String getNodesJson() {
        return mNodesJson;
    }

    public static void setNodesJson(String mNodesJson) {
        GlobalVariables.mNodesJson = mNodesJson;
    }

    public static boolean isNetworkEnabled() {
        return mNetworkEnabled;
    }

    public static void setNetworkEnabled(boolean mNetworkEnabled) {
        GlobalVariables.mNetworkEnabled = mNetworkEnabled;
    }

    public static String getNetworkName() {
        return mNetworkName;
    }

    public static void setNetworkName(String mNetworkName) {
        GlobalVariables.mNetworkName = mNetworkName;
    }

    public static String getNetworkPassphrase() {
        return mNetworkPassphrase;
    }

    public static void setNetworkPassphrase(String mNetworkPassphrase) {
        GlobalVariables.mNetworkPassphrase = mNetworkPassphrase;
    }

    public static boolean isKeepDeviceAwake() {
        return mKeepDeviceAwake;
    }

    public static void setKeepDeviceAwake(boolean mKeepDeviceAwake) {
        GlobalVariables.mKeepDeviceAwake = mKeepDeviceAwake;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //  We need the application context
        mContext = getApplicationContext();

        //  Read in the shared preferences
        getSharedPreferences(mContext);

    }


    /**
     * Get shared preferences.
     * @param context Context:  Application context.
     */
    private static void getSharedPreferences(Context context) {

        SharedPreferences appSharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

        mCameras = getCamerasFromSharedPrefs(context);
        mNodes = getNodesFromSharedPrefs(context);

        mCamerasJson = appSharedPrefs.getString(PREF_KEY_CAMERAS, "");
        mNodesJson = appSharedPrefs.getString(PREF_KEY_NODES, "");

        mNetworkEnabled = appSharedPrefs.getBoolean(PREF_KEY_NETWORK_ENABLED, false);
        mNetworkName = appSharedPrefs.getString(PREF_KEY_NETWORK_NAME, "");
        mNetworkPassphrase = appSharedPrefs.getString(PREF_KEY_NETWORK_PASSPHRASE, "");
        mKeepDeviceAwake = appSharedPrefs.getBoolean(PREF_KEY_KEEP_DEVICE_AWAKE, false);

    }

    /**
     * Retrieve list of node's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     * @return List<Node>:  Returns List<Node> of configured nodes.
     */
    private static List<Node> getNodesFromSharedPrefs(Context context) {
        List<Node> nodes = new ArrayList<>();
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(Node.PrefKey.PREF_KEY_NODES, "");
        nodes = gson.fromJson(json, new TypeToken<ArrayList<Node>>(){}.getType());
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        return nodes;
    }

    /**
     * Save list of node's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     */
    private static void saveNodesToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mNodes); //tasks is an ArrayList instance variable
        prefsEditor.putString(Node.PrefKey.PREF_KEY_NODES, json);
        prefsEditor.apply();
    }

    /**
     * Retrieve list of camera's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     * @return List<Camera>:  Returns List<Camera> of configured cameras.
     */
    private static List<Camera> getCamerasFromSharedPrefs(Context context) {
        List<Camera> cameras = new ArrayList<>();
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(Camera.PrefKey.PREF_KEY_CAMERAS, "");
        cameras = gson.fromJson(json, new TypeToken<ArrayList<Camera>>(){}.getType());
        if (cameras == null) {
            cameras = new ArrayList<Camera>();
        }
        return cameras;
    }

    /**
     * Save list of camera's that are stored in SharedPreferences as a JSON string.
     * @param context Context:  Application context.
     */
    private static void saveCamerasToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mCameras); //tasks is an ArrayList instance variable
        prefsEditor.putString(Camera.PrefKey.PREF_KEY_CAMERAS, json);
        prefsEditor.apply();
    }

}
