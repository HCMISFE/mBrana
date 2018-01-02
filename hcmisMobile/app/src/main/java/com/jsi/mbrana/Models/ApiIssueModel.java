package com.jsi.mbrana.Models;

import java.io.Serializable;

/**
 * Created by pc-6 on 6/22/2016.
 */
public class ApiIssueModel implements Serializable {
    private int ID;
    private int STVID;
    private String RefNO;
    private String IssNo;
    private String FullItemName;
    private String ProductCN;
    private String ExpireDate;
    private String BatchNo;
    private int Quantity;
    private String EnvironmentCode;
    private int EnvironmentID;
    private String FacilityName;
    private String Unit;
    private int VVMID;
    private String VVMCode;
    private String Manufacturer;
    private int UnitCost;
    private int TotalCost;
    private int ItemID;
    private String PrintedDate;
    private int OrderID;
    private String IDPrinted;
    private Boolean IsVoided;

    public Boolean getVoided() {
        return IsVoided;
    }

    public void setVoided(Boolean voided) {
        IsVoided = voided;
    }

    public void setIDPrinted(String IDPrinted) {
        this.IDPrinted = IDPrinted;
    }

    public String getIDPrinted() {
        return IDPrinted;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public void setPrintedDate(String printedDate) {
        PrintedDate = printedDate;
    }

    public String getPrintedDate() {
        return PrintedDate;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setSTVID(int STVID) {
        this.STVID = STVID;
    }

    public int getSTVID() {
        return STVID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setUnitCost(int unitCost) {
        UnitCost = unitCost;
    }

    public void setTotalCost(int totalCost) {
        TotalCost = totalCost;
    }

    public int getTotalCost() {
        return TotalCost;
    }

    public int getUnitCost() {
        return UnitCost;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setVVMID(int VVMID) {
        this.VVMID = VVMID;
    }

    public void setVVMCode(String VVMCode) {
        this.VVMCode = VVMCode;
    }

    public int getVVMID() {
        return VVMID;
    }

    public String getVVMCode() {
        return VVMCode;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnit() {
        return Unit;
    }

    public void setFacilityName(String facilityName) {
        FacilityName = facilityName;
    }

    public String getFacilityName() {
        return FacilityName;
    }

    public void setRefNO(String refNO) {
        RefNO = refNO;
    }

    public void setIssNo(String issNo) {
        IssNo = issNo;
    }

    public void setFullItemName(String fullItemName) {
        FullItemName = fullItemName;
    }

    public void setProductCN(String productCN) {
        ProductCN = productCN;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public String getRefNO() {
        return RefNO;
    }

    public String getIssNo() {
        return IssNo;
    }

    public String getFullItemName() {
        return FullItemName;
    }

    public String getProductCN() {
        return ProductCN;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }
}
