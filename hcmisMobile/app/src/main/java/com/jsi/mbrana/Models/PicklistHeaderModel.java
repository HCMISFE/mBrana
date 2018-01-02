package com.jsi.mbrana.Models;

/**
 * Created by pc-6 on 7/4/2016.
 */
public class PicklistHeaderModel {

    private int ID;
    private String OrderStatusCode;
    private String RefNo;
    private String PickedDate;
    private int OrderId;
    private int EnvironmentID;
    private String EnvironmentCode;
    private int RequestedBy;


    public void setRequestedBy(int requestedBy) {
        RequestedBy = requestedBy;
    }

    public int getRequestedBy() {
        return RequestedBy;
    }

    public int getID() {
        return ID;
    }

    public String getOrderStatusCode() {
        return OrderStatusCode;
    }

    public String getRefNo() {
        return RefNo;
    }

    public String getPickedDate() {
        return PickedDate;
    }

    public int getOrderId() {
        return OrderId;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        OrderStatusCode = orderStatusCode;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public void setPickedDate(String pickedDate) {
        PickedDate = pickedDate;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }
}
