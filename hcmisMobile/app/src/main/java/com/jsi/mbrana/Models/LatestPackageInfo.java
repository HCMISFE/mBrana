package com.jsi.mbrana.Models;

/**
 * Created by user on 2/10/2017.
 */

public class LatestPackageInfo {
    public int LastestVersionCode;
    public int MinVersioncode;
    public String UpdateUrl;

    public int getLastestVersionCode() {
        return LastestVersionCode;
    }

    public void setLastestVersionCode(int lastestVersionCode) {
        LastestVersionCode = lastestVersionCode;
    }

    public int getMinVersioncode() {
        return MinVersioncode;
    }

    public void setMinVersioncode(int minVersioncode) {
        MinVersioncode = minVersioncode;
    }

    public String getUpdateUrl() {
        return UpdateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        UpdateUrl = updateUrl;
    }
}