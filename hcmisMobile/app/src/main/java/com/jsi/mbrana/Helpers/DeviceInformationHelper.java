package com.jsi.mbrana.Helpers;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by pc-2 on 3/17/2016.
 */
public class DeviceInformationHelper {
    private Context context;

    public DeviceInformationHelper(Context context){
        this.context = context;
    }

    public String getUniqueDeviceID(){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return TextUtils.isEmpty(androidId) ? deviceUuid.toString() : androidId;
    }

    public String getDeviceMACAddress(){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if(model.startsWith(manufacturer))
            return model;
        return (manufacturer + " " + model).replace(" ", "_");
    }

    public String getDeviceAndroidVersion(){
        return Build.VERSION.RELEASE;
    }
}
