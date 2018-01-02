package com.jsi.mbrana.Models;

/**
 * Created by user on 21/3/2017.
 */

public class RRFHeaderModel {
    String EnvironmentCode;
    int EnvironmentID;
    int UserID;
    String ActivityCode;
    String UserName;
    int PeriodID;
    int SupplierID;
    int MonthsToSupply;
    boolean IsFilled;
    String PurchaseOrderStatusCode;
    String PurchaseOrderType;

    public String getPurchaseOrderType() {
        return PurchaseOrderType;
    }

    public void setPurchaseOrderType(String purchaseOrderType) {
        PurchaseOrderType = purchaseOrderType;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public int getPeriodID() {
        return PeriodID;
    }

    public void setPeriodID(int periodID) {
        PeriodID = periodID;
    }

    public boolean isFilled() {
        return IsFilled;
    }

    public void setFilled(boolean filled) {
        IsFilled = filled;
    }

    public String getPurchaseOrderStatusCode() {
        return PurchaseOrderStatusCode;
    }

    public void setPurchaseOrderStatusCode(String purchaseOrderStatusCode) {
        PurchaseOrderStatusCode = purchaseOrderStatusCode;
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

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getMonthsToSupply() {
        return MonthsToSupply;
    }

    public void setMonthsToSupply(int monthsToSupply) {
        MonthsToSupply = monthsToSupply;
    }
}
