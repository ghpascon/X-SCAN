package com.ubx.uhf.utils;

import android.device.DeviceManager;

public class DeviceHelper {

    /**
     *  Whether to turn on the handle button and scan the QR code (scan head emits light)
     *  Call this method before your own business logic needs to be closed or opened (note: it cannot be called after pressing the handle button, because the light scan code will be triggered after the button is pressed)。
     * @param isopen     true:turn on   false:close
     */
    public static void setOpenScan523(boolean isopen) {
        DeviceManager mDeviceManager = new DeviceManager();
        if (mDeviceManager != null) {
            if (isopen) {
                //TODO Set trigger 523 key value (handle button) scan light
                mDeviceManager.setSettingProperty("persist-persist.sys.rfid.key", "0-");
                mDeviceManager.setSettingProperty("persist-persist.sys.scan.key", "520-521-522-523-");//Key value are passed in as input parameters here. When the key value is pressed, the scanning head will be activated to emit light.
            }else {
                //TODO Set trigger 523 key value (handle button) without scanning for light
                mDeviceManager.setSettingProperty("persist-persist.sys.rfid.key", "0-");
                mDeviceManager.setSettingProperty("persist-persist.sys.scan.key", "520-521-522-");//Key values are passed in as input parameters here. When the key value is pressed, the scanning head will be activated but not emit light
            }
        }
    }

    /**
     *
     * Set the corresponding key value and scan the QR code (scan head emits light)
     * @param keyList     Key values for effective scanning, separated by -
     */
    public static void setScanKey(String keyList) {
        DeviceManager mDeviceManager = new DeviceManager();
        if (keyList==null){
            keyList = "";
        }
        if (mDeviceManager != null) {
            //TODO Set the trigger corresponding key value to scan the light
            mDeviceManager.setSettingProperty("persist-persist.sys.rfid.key", "0-");
            mDeviceManager.setSettingProperty("persist-persist.sys.scan.key", keyList);//Key value are passed in as input parameters here. When the key value is pressed, the scanning head will be activated to emit light.
        }
    }

}
