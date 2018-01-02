package com.jsi.mbrana.Modules.Receive.Helper.Status;

/**
 * Created by Surafel Nigussie on 12/21/2017.
 */

public class StatusModel {
    private String StatusName;
    private String StatusCode;
    private int StatusColor;
    private String StatusFirstLetter;

    public StatusModel(String statusName, String statusCode, String statusFirstLetter, int statusColor) {
        this.StatusName = statusName;
        this.StatusCode = statusCode;
        this.StatusColor = statusColor;
        this.StatusFirstLetter = statusFirstLetter;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public int getStatusColor() {
        return StatusColor;
    }

    public void setStatusColor(int statusColor) {
        StatusColor = statusColor;
    }
}
