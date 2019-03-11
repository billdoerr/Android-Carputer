package com.billdoerr.android.carputer.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 *
 */
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

    /**
     *
     */
    public SettingsMessageEvent() {
    }

    /**
     *
     * @return
     */
    public com.billdoerr.android.carputer.settings.Node getNode() {
        return mNode;
    }

    /**
     *
     * @return
     */
    public int getAction() {
        return mAction;
    }

    /**
     *
     * @param action
     */
    public void setAction(int action) {
        mAction = action;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return mIndex;
    }

    /**
     *
     * @return
     */
    public Camera getCamera() {
        return mCamera;
    }


    /**
     *
     * @param action
     * @param device
     * @param camera
     * @param index
     */
    public void sendMessage(int action, int device, Camera camera, int index) {
        mAction = action;
        mDevice = device;
        mCamera = camera;
        mIndex = index;
    }

    /**
     *
     * @param action
     * @param device
     * @param node
     * @param index
     */
    public void sendMessage(int action, int device, Node node, int index) {
        mAction = action;
        mDevice = device;
        mNode = node;
        mIndex = index;
    }

}

