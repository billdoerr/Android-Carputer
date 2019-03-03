package com.billdoerr.android.carputer.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

class SettingsMessageEvent {

    private static final String TAG = "SettingsMessageEvent";

    @IntDef({Action.ADD, Action.DELETE, Action.UPDATE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Action {
        int ADD = 0;
        int DELETE = 1;
        int UPDATE = 2;
    }

    @IntDef({Device.CAMERA, Device.NODE, Device.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    @interface Device {
        int CAMERA = 0;
        int NODE = 1;
        int OTHER = 2;
    }

    private int mAction;
    private int mDevice;
    private int mIndex;

    private Camera mCamera;
    private Node mNode;

    public com.billdoerr.android.carputer.settings.Node getNode() {
        return mNode;
    }

    public void setRaspberryPi(Node node) {
        mNode = node;
    }

    public int getAction() {
        return mAction;
    }

    public void setAction(int action) {
        mAction = action;
    }

    public int getDevice() {
        return mDevice;
    }

    public void setDevice(int device) {
        mDevice = device;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
    }

    private String mMessage;    //  For simple testing

    public SettingsMessageEvent() {
    }

    public SettingsMessageEvent(String message) {
        mMessage = message;
    }

    public void sendMessage(String message) {
        mMessage = message;
    }

    public void sendMessage(int action, int device, Camera camera, int index) {
        mAction = action;
        mDevice = device;
        mCamera = camera;
        mIndex = index;
    }

    public void sendMessage(int action, int device, Node node, int index) {
        mAction = action;
        mDevice = device;
        mNode = node;
        mIndex = index;
    }

    public String getMessage() {
        return mMessage;
    }

}

