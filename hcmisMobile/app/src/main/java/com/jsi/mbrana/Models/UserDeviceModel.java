package com.jsi.mbrana.Models;

/**
 * Created by pc-6 on 8/15/2016.
 */
public class UserDeviceModel {

    private  int UserDeviceID;
    private  int UserID;
    private String DeviceIdentifier;
    private String DeviceType;
    private String PushNotificationIdentifier;
    private String OtherInfo;

    public void setUserDeviceID(int userDeviceID) {
        UserDeviceID = userDeviceID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        DeviceIdentifier = deviceIdentifier;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public void setPushNotificationIdentifier(String pushNotificationIdentifier) {
        PushNotificationIdentifier = pushNotificationIdentifier;
    }

    public void setOtherInfo(String otherInfo) {
        OtherInfo = otherInfo;
    }

    public int getUserDeviceID() {
        return UserDeviceID;
    }

    public int getUserID() {
        return UserID;
    }

    public String getDeviceIdentifier() {
        return DeviceIdentifier;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public String getPushNotificationIdentifier() {
        return PushNotificationIdentifier;
    }

    public String getOtherInfo() {
        return OtherInfo;
    }
}
