package com.billdoerr.android.carputer.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Message events for the preference settings activity and fragments.
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
     * @return Node:  Object of type Node.
     */
    public com.billdoerr.android.carputer.settings.Node getNode() {
        return mNode;
    }

    /**
     *
     * @return int:  Action to be performed.
     */
    public int getAction() {
        return mAction;
    }

    /**
     *
     * @param action int:  Set action to be performed.
     */
    public void setAction(int action) {
        mAction = action;
    }

    /**
     *
     * @return int:  Get index of device.
     */
    public int getIndex() {
        return mIndex;
    }

    /**
     *
     * @return Camera:  Object of type Camera.
     */
    public Camera getCamera() {
        return mCamera;
    }

    //  TODO:  Remove int device param since always of type Camera
    /**
     * Send message to event bus.
     * @param action int:  Value for action to be taken.
     * @param device int:  Identifier for type of device.
     * @param camera Camera:  Object of type Camera.
     * @param index int:  Index of list of devices.
     */
    public void sendMessage(int action, int device, Camera camera, int index) {
        mAction = action;
        mDevice = device;
        mCamera = camera;
        mIndex = index;
    }

    //  TODO:  Remove int device param since always of type Node
    /**
     * Send message to event bus.
     * @param action int:  Value for action to be taken.
     * @param device int:  Identifier for type of device.
     * @param node Node:   Object of type Camera.
     * @param index int:  Index of list of devices.
     */
    public void sendMessage(int action, int device, Node node, int index) {
        mAction = action;
        mDevice = device;
        mNode = node;
        mIndex = index;
    }

}

