package com.billdoerr.android.carputer.settings;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

public class Camera implements Serializable {

    private static final String TAG = "Camera";

    @StringDef({
            PrefKey.PREF_KEY_CAMERAS
    })

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrefKey {
        String PREF_KEY_CAMERAS = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERAS";
    }

    private String mName;
    private String mUrl;
    private boolean mUseAuthentication;
    private String mUser;
    private String mPassword;

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
