package com.billdoerr.android.carputer.settings;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


import androidx.annotation.StringDef;

public class Camera implements Serializable {

//    private static final String PREF_KEY_CAMERAS = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERAS";
//    private static final String PREF_KEY_CAMERA_NUM = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_NUM_";
//    private static final String PREF_KEY_CAMERA_NAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_NAME";
//    private static final String PREF_KEY_CAMERA_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_URL";
    //  These are not implemented
//    private static final String PREF_KEY_CAMERA_USE_AUTH = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_USE_AUTH";
//    private static final String PREF_KEY_CAMERA_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_USERNAME";
//    private static final String PREF_KEY_CAMERA_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_PASSWORD";

//    private static final String PREF_KEY_CAMERA_MULTI_PANE_VIEW = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_MULTI_PANE_VIEW";

    @StringDef({
            PrefKey.PREF_KEY_CAMERAS,
            PrefKey.PREF_KEY_CAMERA_NUM,
            PrefKey.PREF_KEY_CAMERA_NAME,
            PrefKey.PREF_KEY_CAMERA_URL,
    })

    @Retention(RetentionPolicy.SOURCE)
    @interface PrefKey {
        String PREF_KEY_CAMERAS = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERAS";
        String PREF_KEY_CAMERA_NUM = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_NUM_";
        String PREF_KEY_CAMERA_NAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_NAME";
        String PREF_KEY_CAMERA_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERA_URL";
    }

    private String mName;
    private String mUrl;
    private boolean mUseAuthentication;
    private String mUser;
    private String mPassword;
    private boolean mMultipaneView;

    public boolean isMultipaneView() {
        return mMultipaneView;
    }

    public void setMultipaneView(boolean multipaneView) {
        mMultipaneView = multipaneView;
    }

    public String getName() {
        return mName;
    }

    public void setName(String Name) {
        mName = Name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String Url) {
        mUrl = Url;
    }

    public boolean isUseAuthentication() {
        return mUseAuthentication;
    }

    public void setUseAuthentication(boolean useAuthentication) {
        mUseAuthentication = useAuthentication;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String User) {
        mUser = User;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String Password) {
        mPassword = Password;
    }

}
