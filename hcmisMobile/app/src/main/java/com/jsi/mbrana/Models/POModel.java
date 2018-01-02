package com.jsi.mbrana.Models;

import java.io.Serializable;

/**
 * Created by pc-6 on 6/18/2016.
 */
public class POModel implements Serializable {

    public int FillRate;
    private int ID;
    private String PONumber;
    private String EnvironmentCode;
    private int EnvironmentID;
    private String PurchaseOrderStatus;
    private String PurchaseOrderStatusCode;
    private int DetailCount;
    private String Supplier;
    private String ModifiedDate;
    private int SupplierID;
    private String ActivityCode;

    public int getFillRate() {
        return FillRate;
    }

    public void setFillRate(int fillrate) {
        FillRate = fillrate;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPONumber() {
        return PONumber;
    }

    public void setPONumber(String PONumber) {
        this.PONumber = PONumber;
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

    public String getPurchaseOrderStatus() {
        return PurchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(String purchaseOrderStatus) {
        PurchaseOrderStatus = purchaseOrderStatus;
    }

    public String getPurchaseOrderStatusCode() {
        return PurchaseOrderStatusCode;
    }

    public void setPurchaseOrderStatusCode(String purchaseOrderStatusCode) {
        PurchaseOrderStatusCode = purchaseOrderStatusCode;
    }

    public int getDetailCount() {
        return DetailCount;
    }

    public void setDetailCount(int detailCount) {
        DetailCount = detailCount;
    }

    public String getSupplier() {
        return Supplier;
    }

    public void setSupplier(String supplier) {
        Supplier = supplier;
    }

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }
}
