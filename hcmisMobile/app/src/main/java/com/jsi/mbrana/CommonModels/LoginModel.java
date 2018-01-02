package com.jsi.mbrana.CommonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("EnvironmentId")
    @Expose
    private Integer environmentId;
    @SerializedName("DeviceType")
    @Expose
    private String deviceType;
    @SerializedName("NetworkType")
    @Expose
    private String networkType;
    @SerializedName("SignalStrength")
    @Expose
    private String signalStrength;
    @SerializedName("PhoneModel")
    @Expose
    private String phoneModel;
    @SerializedName("IMEID1")
    @Expose
    private String iMEID1;
    @SerializedName("IMEID2")
    @Expose
    private String iMEID2;
    @SerializedName("AndroidVersion")
    @Expose
    private String androidVersion;
    @SerializedName("Status")
    @Expose
    private String status;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getIMEID1() {
        return iMEID1;
    }

    public void setIMEID1(String iMEID1) {
        this.iMEID1 = iMEID1;
    }

    public String getIMEID2() {
        return iMEID2;
    }

    public void setIMEID2(String iMEID2) {
        this.iMEID2 = iMEID2;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
