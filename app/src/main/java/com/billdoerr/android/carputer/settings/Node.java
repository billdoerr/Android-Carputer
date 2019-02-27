package com.billdoerr.android.carputer.settings;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

public class Node implements Serializable {

    @StringDef({
            PrefKey.PREF_KEY_NODES,
            PrefKey.PREF_KEY_NODE_NUM
//            PrefKey.PREF_KEY_NODE_IP,
//            PrefKey.PREF_KEY_NODE_SSH_PORT,
//            PrefKey.PREF_KEY_NODE_AUTH_PASSWORD,
//            PrefKey.PREF_KEY_NODE_MOTIONEYE_ENABLED,
//            PrefKey.PREF_KEY_NODE_MOTIONEYE_URL,
//            PrefKey.PREF_KEY_NODE_MOTIONEYE_AUTH_USERNAME,
//            PrefKey.PREF_KEY_NODE_MOTIONEYE_AUTH_PASSWORD,
//            PrefKey.PREF_KEY_NODE_PHPSYSINFO_ENABLED,
//            PrefKey.PREF_KEY_NODE_PHPSYSINFO_URL
    })

    @Retention(RetentionPolicy.SOURCE)
    @interface PrefKey {
        String PREF_KEY_NODES = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODES";
        String PREF_KEY_NODE_NUM = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_NUM_";
//        String PREF_KEY_NODE_IP = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_IP";
//        String PREF_KEY_NODE_SSH_PORT = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_SSH_PORT";
//        String PREF_KEY_NODE_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_AUTH_PASSWORD";
//        String PREF_KEY_NODE_MOTIONEYE_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_MOTIONEYE_ENABLED";
//        String PREF_KEY_NODE_MOTIONEYE_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_MOTIONEYE_URL";
//        String PREF_KEY_NODE_MOTIONEYE_AUTH_USERNAME = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_MOTIONEYE_AUTH_USERNAME";
//        String PREF_KEY_NODE_MOTIONEYE_AUTH_PASSWORD = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_MOTIONEYE_AUTH_PASSWORD";
//        String PREF_KEY_NODE_PHPSYSINFO_ENABLED = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_PHPSYSINFO_ENABLED";
//        String PREF_KEY_NODE_PHPSYSINFO_URL = "com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODE_PHPSYSINFO_URL";
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

}
