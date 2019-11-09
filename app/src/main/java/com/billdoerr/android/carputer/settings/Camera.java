package com.billdoerr.android.carputer.settings;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

/**
 * Camera object.
 */
public class Camera implements Serializable {

    @StringDef({
            PrefKey.PREF_KEY_CAMERAS
    })

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrefKey {
        String PREF_KEY_CAMERAS = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERAS";
    }

    private String mName;
    private String mUrl;
//    private boolean mUseAuthentication;
//    private String mUser;
//    private String mPassword;

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

// --Commented out by Inspection START (11/6/2019 1:35 PM):
//    public boolean isUseAuthentication() {
//        return mUseAuthentication;
//    }
// --Commented out by Inspection STOP (11/6/2019 1:35 PM)

// --Commented out by Inspection START (11/6/2019 1:36 PM):
//    public void setUseAuthentication(boolean useAuthentication) {
//        mUseAuthentication = useAuthentication;
//    }
// --Commented out by Inspection STOP (11/6/2019 1:36 PM)

// --Commented out by Inspection START (11/6/2019 1:35 PM):
//    public String getUser() {
//        return mUser;
//    }
// --Commented out by Inspection STOP (11/6/2019 1:35 PM)

// --Commented out by Inspection START (11/6/2019 1:36 PM):
//    public void setUser(String User) {
//        mUser = User;
//    }
// --Commented out by Inspection STOP (11/6/2019 1:36 PM)

// --Commented out by Inspection START (11/6/2019 1:35 PM):
//    public String getPassword() {
//        return mPassword;
//    }
// --Commented out by Inspection STOP (11/6/2019 1:35 PM)

// --Commented out by Inspection START (11/6/2019 1:35 PM):
//    public void setPassword(String Password) {
//        mPassword = Password;
//    }
// --Commented out by Inspection STOP (11/6/2019 1:35 PM)

//    /**
//     * Converts object to string.
//     * Usage:  Array.toString(List<Camera>.toArray().
//     * @return String:  Output values of array.
//     */
//    @NonNull
//    @Override
//    public String toString() {
//
//        String c = "Name:  " + mName + "\n";
//        c = c + "Url:  " + mUrl + "\n";
//        c = c + "Use Authentication:  " + mUseAuthentication + "\n";
//        c = c + "User:  " + mUser + "\n";
//        c = c + "Password:  " + mPassword + "\n";
//
//        return c;
//    }

}
