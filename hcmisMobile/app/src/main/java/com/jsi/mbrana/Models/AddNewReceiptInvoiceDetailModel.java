package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/17/2016.
 */
public class AddNewReceiptInvoiceDetailModel {
    private int ID;
    private int ManufacturerId;
    private int ItemID;
    private int VVMID;
    private String VVMCode;
    private int UnitID;
    private int EnvironmentID;
    private int SupplierID;
    private String EnvironmentCode;
    private String FullItemName;
    private String ProductCN;
    private String Unit;
    private String Manufacturer;
    private String ManufacturerSH;
    private String Model19;
    private String BatchNo;
    private String ExpDate;
    private String ActivityCode;
    private double InvoicedNoOfPack;
    private int Quantity;
    private double UnitPrice;
    private double Margin;
    private int PreviousReceive;
    private int UnitOfIssueID;

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getManufacturerId() {
        return ManufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        ManufacturerId = manufacturerId;
    }

    public String getFullItemName() {
        return FullItemName;
    }

    public void setFullItemName(String fullItemName) {
        FullItemName = fullItemName;
    }

    public String getProductCN() {
        return ProductCN;
    }

    public void setProductCN(String productCN) {
        ProductCN = productCN;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getModel19() {
        return Model19;
    }

    public void setModel19(String model19) {
        Model19 = model19;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public double getInvoicedNoOfPack() {
        return InvoicedNoOfPack;
    }

    public void setInvoicedNoOfPack(double invoicedNoOfPack) {
        InvoicedNoOfPack = invoicedNoOfPack;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public double getMargin() {
        return Margin;
    }

    public void setMargin(double margin) {
        Margin = margin;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }

    public String getVVMCode() {
        return VVMCode;
    }

    public void setVVMCode(String VVMCode) {
        this.VVMCode = VVMCode;
    }

    public int getVVMID() {
        return VVMID;
    }

    public void setVVMID(int VVMID) {
        this.VVMID = VVMID;
    }

    public int getPreviousReceive() {
        return PreviousReceive;
    }

    public void setPreviousReceive(int previousReceive) {
        PreviousReceive = previousReceive;
    }

    public String getManufacturerSH() {
        return ManufacturerSH;
    }

    public void setManufacturerSH(String manufacturerSH) {
        ManufacturerSH = manufacturerSH;
    }

    public int getUnitOfIssueID() {
        return UnitOfIssueID;
    }

    public void setUnitOfIssueID(int unitOfIssueID) {
        UnitOfIssueID = unitOfIssueID;
    }
}

