package com.jsi.mbrana.Models;

/**
 * Created by surafel on 1/23/2017.
 */
public class VersionModel {
    private int EnvironmentID;
    private String EnvironmentCode;
    private int VersionCode;
    private String VersionName;
    private String Version;
    private int UserId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }
}
