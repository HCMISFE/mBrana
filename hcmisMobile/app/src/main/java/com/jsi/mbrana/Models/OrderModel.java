package com.jsi.mbrana.Models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class OrderModel implements Serializable {

    public String ActivityCode;
    private int ID;
    private String RefNo;
    private String dateCreated;
    private String modifiedDate;
    private String orderStatus;
    private String OrderStatusCode;
    private String facility;
    private int RequestedBy;
    private String contactPerson;
    private String contactPersonMobileNumber;
    private int RequestedNoOfItems;
    private int ProcessedNoOfItems;
    private String EnvironmentCode;
    private int EnvironmentID;
    private Date Date;
    private String STVs;
    private boolean IsCampaign;
    private int UserID;
    private String UserName;
    private Boolean isWoredaToZoneSync;

    public Boolean getWoredaToZoneSync() {
        return isWoredaToZoneSync;
    }

    public void setWoredaToZoneSync(Boolean woredaToZoneSync) {
        isWoredaToZoneSync = woredaToZoneSync;
    }

    public int getRequestedNoOfItems() {
        return RequestedNoOfItems;
    }

    public void setRequestedNoOfItems(int requestedNoOfItems) {
        RequestedNoOfItems = requestedNoOfItems;
    }

    public int getProcessedNoOfItems() {
        return ProcessedNoOfItems;
    }

    public void setProcessedNoOfItems(int processedNoOfItems) {
        ProcessedNoOfItems = processedNoOfItems;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getSTVs() {
        return STVs;
    }

    public void setSTVs(String STVs) {
        this.STVs = STVs;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }

    public String getOrderStatusCode() {
        return OrderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        OrderStatusCode = orderStatusCode;
    }

    public int getRequestedBy() {
        return RequestedBy;
    }

    public void setRequestedBy(int requestedBy) {
        this.RequestedBy = requestedBy;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonMobileNumber() {
        return contactPersonMobileNumber;
    }

    public void setContactPersonMobileNumber(String contactPersonMobileNumber) {
        this.contactPersonMobileNumber = contactPersonMobileNumber;
    }

    public boolean isCampaign() {
        return IsCampaign;
    }

    public void setCampaign(boolean campaign) {
        IsCampaign = campaign;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String ActivityCode) {
        this.ActivityCode = ActivityCode;
    }
}
