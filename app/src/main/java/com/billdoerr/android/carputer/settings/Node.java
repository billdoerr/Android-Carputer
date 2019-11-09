package com.billdoerr.android.carputer.settings;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

/**
 * Node object.
 */
public class Node implements Serializable {

    @StringDef({
            PrefKey.PREF_KEY_NODES
    })

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrefKey {
        String PREF_KEY_NODES = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODES";
    }

    private String mName;
    private String mIp;
    private String mSSHPort;

    private boolean mUseAuthentication;
    private String mUser;
    private String mPassword;

    private boolean mUsePhpSysInfo;
    private String mPhpSysInfoUrl;

    private boolean mUseMotionEye;
    private String mMotionEyeUrl;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getIp() {
        return mIp;
    }

    public void setIp(String ip) {
        mIp = ip;
    }

    public String getSSHPort() {
        return mSSHPort;
    }

    public void setSSHPort(String port) {
        mSSHPort = port;
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

    public void setUser(String user) {
        mUser = user;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean isUsePhpSysInfo() {
        return mUsePhpSysInfo;
    }

    public void setUsePhpSysInfo(boolean usePhpSysInfo) {
        mUsePhpSysInfo = usePhpSysInfo;
    }

    public String getPhpSysInfoUrl() {
        return mPhpSysInfoUrl;
    }

    public void setPhpSysInfoUrl(String phpSysInfoUrl) {
        mPhpSysInfoUrl = phpSysInfoUrl;
    }

    public boolean isUseMotionEye() {
        return mUseMotionEye;
    }

    public void setUseMotionEye(boolean useMotionEye) {
        mUseMotionEye = useMotionEye;
    }

    public String getMotionEyeUrl() {
        return mMotionEyeUrl;
    }

    public void setMotionEyeUrl(String motionEyeUrl) {
        mMotionEyeUrl = motionEyeUrl;
    }

    /**
     * Converts object to string.
     * Usage:  Array.toString(List<Node>.toArray().
     * @return String:  Output values of array.
     */
    @NonNull
    @Override
    public String toString() {

        String c = "Name:  " + mName + "\n";
        c = c + "Ip:  " + mIp + "\n";
        c = c + "SSH Port:  " + mSSHPort + "\n";

        c = c + "User Authentication:  " + mUseAuthentication + "\n";
        c = c + "User:  " + mUser + "\n";
        c = c + "Password:  " + mPassword + "\n";

        c = c + "Use phpSysInfo:  " + mUsePhpSysInfo + "\n";
        c = c + "phpSysInfo Url:  " + mPhpSysInfoUrl + "\n";


        c = c + "Use motionEye:  " + mUseMotionEye + "\n";
        c = c + "motionEye Url:  " + mMotionEyeUrl + "\n";

        return c;
    }

}